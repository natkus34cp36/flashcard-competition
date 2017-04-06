package nat.flashcardcompetition;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import static nat.flashcardcompetitionModel.Studyset.STUDYSET_ID;
import static nat.flashcardcompetitionModel.Studyset.STUDYSET_NAME;
import static nat.flashcardcompetitionModel.Studyset.STUDYSET_SUPPORTED_LANGUAGES;

/**
 * A placeholder fragment containing a simple view.
 */
public class CardSettingsActivityFragment extends PreferenceFragment {

    String studySetId;
    String lang1,lang2;
    String[] studySetSupportedLanguages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getActivity();
        PreferenceScreen rootPreferenceScreen = getPreferenceManager().createPreferenceScreen(context);
        setPreferenceScreen(rootPreferenceScreen);

        if(getArguments() != null) {
//            Log.i("BUNDLE", "GOT BUNDLE!");
            studySetSupportedLanguages = getArguments().getStringArray(STUDYSET_SUPPORTED_LANGUAGES);
            lang1 = getArguments().getString("lang1");
            lang2 = getArguments().getString("lang2");
            studySetId = getArguments().getString("studySetId");
//            Log.i("BUNDLE", "lang1: " + lang1 +", lang2: "+ lang2);
        } else {
//            Log.i("BUNDLE", "NO BUNDLE :(");
            studySetSupportedLanguages = new String[]{"En", "Sp", "Jp"};
            studySetId = "0";
            lang1 = "En";
            lang2 = "Th";
        }


        PreferenceCategory preferenceFrontCategory = new PreferenceCategory(context);
        preferenceFrontCategory.setTitle("Front");

        PreferenceCategory preferenceBackCategory = new PreferenceCategory(context);
        preferenceBackCategory.setTitle("Back");

        getPreferenceScreen().addPreference(preferenceFrontCategory);
        getPreferenceScreen().addPreference(preferenceBackCategory);


        int n = studySetSupportedLanguages.length;
        for(int i = 0 ; i < n ; i++){
            CheckBoxPreference checkBoxPreference = new CheckBoxPreference(context);
            checkBoxPreference.setTitle(studySetSupportedLanguages[i]);
            checkBoxPreference.setKey(studySetId+":front:"+studySetSupportedLanguages[i]);
            checkBoxPreference.setChecked(false);
            if(lang1.equals(studySetSupportedLanguages[i])){
                checkBoxPreference.setChecked(true);
            }
            preferenceFrontCategory.addPreference(checkBoxPreference);
        }

        for(int i = 0 ; i < n ; i++){
            CheckBoxPreference checkBoxPreference = new CheckBoxPreference(context);
            checkBoxPreference.setTitle(studySetSupportedLanguages[i]);
            checkBoxPreference.setKey(studySetId+":back:"+studySetSupportedLanguages[i]);
            checkBoxPreference.setChecked(false);
            if(lang2.equals(studySetSupportedLanguages[i])){
                checkBoxPreference.setChecked(true);
            }
            preferenceBackCategory.addPreference(checkBoxPreference);
        }
    }

    public CardSettingsActivityFragment() {
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_card_settings, container, false);
//    }
}
