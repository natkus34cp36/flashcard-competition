package nat.flashcardcompetition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class StudysetViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyset_view);

        ArrayList<Studyset> studysets = getStudyset();
        StudysetAdapter studysetAdapter = new StudysetAdapter(this, studysets);

        ListView listView = (ListView) findViewById(R.id.studyset_listview);
        listView.setAdapter(studysetAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent intent = new Intent(StudysetViewActivity.this, CardViewActivity.class);
                int studysetID = view.getId();
                intent.putExtra(EXTRA_MESSAGE, studysetID);
                startActivity(intent);
            }
        });

    }

    private ArrayList<Studyset> getStudyset(){
        ArrayList<Studyset> studysets = new ArrayList<>();

        //TODO GET STUDY SET FROM DB / SHARED PREFERENCES
        for(int i = 0; i < 5 ; i++){
            ArrayList<String> languages = new ArrayList<>();
            languages.add("En");
            languages.add("Th");
            languages.add("Jp");
            Studyset temp = new Studyset(i,"StudySet"+i, languages, 0.75);
            studysets.add(temp);
        }

        return studysets;
    }
}
