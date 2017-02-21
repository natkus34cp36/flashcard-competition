package nat.flashcardcompetition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static nat.flashcardcompetition.Studyset.STUDYSET_ID;
import static nat.flashcardcompetition.Studyset.STUDYSET_NAME;

public class CardViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        Intent intent = getIntent();
        String studySetID = intent.getStringExtra(STUDYSET_ID);
        String studySetName = intent.getStringExtra(STUDYSET_NAME);
        this.getSupportActionBar().setTitle(studySetName);

        ArrayList<Card> cards = getCards();
        CardAdapter cardAdapter = new CardAdapter(this,cards);

        ListView listView = (ListView) findViewById(R.id.card_listview);
        listView.setAdapter(cardAdapter);

    }

    public void StudyListener(View target){
        Intent intent = new Intent(this, StudyViewAcitivity.class);
        startActivity(intent);
    }

    public void MatchingListener(View target){
        Intent intent = new Intent(this, GameOptionViewActivity.class);
        startActivity(intent);
    }

    public void ScoreboardListener(View target){
        Intent intent = new Intent(this, StudyViewAcitivity.class);
        startActivity(intent);
    }

    private ArrayList<Card> getCards(){
        ArrayList<Card> cards = new ArrayList<>();
        String[] firsts = {"Lust","Gluttony","Greed","Sloth","Wrath","Envy","Pride"};
        String[] seconds = {"ความหื่น","ความตะกละ","ความโลภ","ความขี้เกียจ","ความโกรธ","ความขี้อิจฉา","ความยะโส"};
        for(int i=0;i<firsts.length;i++){
            Card temp = new Card(i,firsts[i],seconds[i],firsts[i],true);
            cards.add(temp);
        }
        return cards;
    }

}
