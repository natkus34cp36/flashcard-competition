package nat.flashcardcompetition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.provider.Settings.Secure;
import android.widget.TextView;

import java.util.List;

import nat.flashcardcompetitionModel.Card;
import nat.sqlite.DBManager;

import static nat.flashcardcompetitionModel.Studyset.STUDYSET_ID;
import static nat.flashcardcompetitionModel.Studyset.STUDYSET_NAME;
import static nat.flashcardcompetitionModel.Studyset.STUDYSET_SUPPORTED_LANGUAGES;

public class CardViewActivity extends AppCompatActivity {

    DBManager dbManager;
    int studySetId;
    String lang1,lang2;
    String[] studySetSupportedLanguages;

    CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        dbManager = new DBManager(this);
        dbManager.open();

        Intent intent = getIntent();
        studySetId = intent.getIntExtra(STUDYSET_ID, 1);
        String studySetName = intent.getStringExtra(STUDYSET_NAME);
        studySetSupportedLanguages = intent.getStringExtra(STUDYSET_SUPPORTED_LANGUAGES).replaceAll("\\s+","").split(",");

        prepareLang();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.getSupportActionBar().setTitle(studySetName);

        TextView username = (TextView) findViewById(R.id.cardview_profile_textview);
        username.setText(sharedPreferences.getString("name","Username"));
        cardAdapter = new CardAdapter(this,getCards()); // getCards() must be called after prepareLang().

        ListView listView = (ListView) findViewById(R.id.card_listview);
        listView.setAdapter(cardAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareLang();
        cardAdapter.clear();
        List<Card> cards = getCards();
//        Log.i("CARD", "lang1: " + lang1 + ", lang2: " + lang2 + ", card-front: " + cards.get(0).first + "card-back: " + cards.get(0).second);
        cardAdapter.addAll(cards);
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_card_view, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, CardSettingsActivity.class);
                intent.putExtra(STUDYSET_ID, studySetId);
                intent.putExtra(STUDYSET_SUPPORTED_LANGUAGES, studySetSupportedLanguages);
                intent.putExtra("lang1", lang1);
                intent.putExtra("lang2", lang2);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void StudyListener(View target){
        Intent intent = new Intent(this, StudyViewAcitivity.class);
        intent.putExtra(STUDYSET_ID, studySetId);
        intent.putExtra("lang1",lang1);
        intent.putExtra("lang2",lang2);
        startActivity(intent);
    }

    public void MatchingListener(View target){
        Intent intent = new Intent(this, GameOptionViewActivity.class);
        intent.putExtra(STUDYSET_ID, studySetId);
        intent.putExtra("lang1",lang1);
        intent.putExtra("lang2",lang2);
        startActivity(intent);
    }

    public void ScoreboardListener(View target){
        Intent intent = new Intent(this, ScoreboardActivity.class);
        intent.putExtra(STUDYSET_ID, studySetId);
        intent.putExtra("lang1",lang1);
        intent.putExtra("lang2",lang2);
        startActivity(intent);
    }

    private List<Card> getCards(){
        return dbManager.getCardByStudySetId(studySetId, lang1, lang2);
    }

    public void prepareLang(){
//        lang1 = "En";
//        lang2 = "Jp";

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // first time in this page, no settings yet.
        if(!sharedPreferences.contains(studySetId+":front:"+studySetSupportedLanguages[0])){
            lang1 = studySetSupportedLanguages[0];
            lang2 = studySetSupportedLanguages[1];
            return ;
        }

        // 2nd time+
        for(String lang :studySetSupportedLanguages){
            if(sharedPreferences.getBoolean(studySetId+":front:"+lang, false)) lang1 = lang;
            if(sharedPreferences.getBoolean(studySetId+":back:"+lang, false)) lang2 = lang;
        }
    }

}
