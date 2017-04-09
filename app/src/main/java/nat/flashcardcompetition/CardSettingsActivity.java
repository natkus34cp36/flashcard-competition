package nat.flashcardcompetition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;


import static nat.flashcardcompetitionModel.Studyset.STUDYSET_ID;
import static nat.flashcardcompetitionModel.Studyset.STUDYSET_NAME;
import static nat.flashcardcompetitionModel.Studyset.STUDYSET_SUPPORTED_LANGUAGES;

public class CardSettingsActivity extends AppCompatActivity {

    int studySetId;
    String lang1,lang2;
    String[] studySetSupportedLanguages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        studySetId = intent.getIntExtra(STUDYSET_ID,1);
        studySetSupportedLanguages = intent.getStringArrayExtra(STUDYSET_SUPPORTED_LANGUAGES);
        lang1 = intent.getStringExtra("lang1");
        lang2 = intent.getStringExtra("lang2");

        Bundle bundle = new Bundle();
        bundle.putStringArray(STUDYSET_SUPPORTED_LANGUAGES, studySetSupportedLanguages);
        bundle.putString("studySetId", studySetId+"");
        bundle.putString("lang1", lang1);
        bundle.putString("lang2", lang2);

//        CardSettingsActivityFragment cardSettingsActivityFragment = (CardSettingsActivityFragment) getFragmentManager().findFragmentById(R.id.card_settings_fragment);
        CardSettingsActivityFragment cardSettingsActivityFragment = new CardSettingsActivityFragment();
        cardSettingsActivityFragment.setArguments(bundle);
        getFragmentManager()
            .beginTransaction()
            .add(R.id.card_settings_fragment, cardSettingsActivityFragment)
            .commit();


    }

    @Override
    public void onBackPressed() {
        // check if shared preferences have some value
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        int countFront = 0, countBack = 0;
        for(String lang :studySetSupportedLanguages){
            Log.i("language", lang);
            Log.i("language", studySetId+":front:"+lang + " --- " + sharedPreferences.getBoolean(studySetId+":front:"+lang, false));
            Log.i("language", studySetId+":back:"+lang + " --- " + sharedPreferences.getBoolean(studySetId+":back:"+lang, false));
            if(sharedPreferences.getBoolean(studySetId+":front:"+lang, false)){
                countFront++;
                lang1 = lang;
            }
            if(sharedPreferences.getBoolean(studySetId+":back:"+lang, false)){
                countBack++;
                lang2 = lang;
            }
        }
        if(countFront == 1 && countBack == 1&& !lang1.equals(lang2)){
            super.onBackPressed();
        } else if(countFront == 1 && countBack == 1 && lang1.equals(lang2)) {
            Toast.makeText(this, "Language for front and back must be different.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Only one language for front and back must be checked.", Toast.LENGTH_SHORT).show();
        }

    }
}
