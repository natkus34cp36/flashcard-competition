package nat.flashcardcompetition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
}
