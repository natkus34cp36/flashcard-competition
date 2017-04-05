package nat.flashcardcompetition;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import nat.flashcardcompetitionModel.CardMeta;
import nat.flashcardcompetitionModel.Scoreboard;
import nat.flashcardcompetitionModel.Studyset;
import nat.sqlite.DBManager;

import static nat.flashcardcompetitionModel.Studyset.STUDYSET_ID;
import static nat.flashcardcompetitionModel.Studyset.STUDYSET_NAME;
import static nat.flashcardcompetitionModel.Studyset.STUDYSET_SUPPORTED_LANGUAGES;

public class StudysetViewActivity extends AppCompatActivity implements OnTaskCompleted{

    private DBManager dbManager;
    private StudysetAdapter studysetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyset_view);

        dbManager = new DBManager(this);
        dbManager.open();

        if(isNetworkAvailable()){
            APICaller apiCaller = new APICaller(dbManager, this);
            apiCaller.prepareGetStudyset();
            apiCaller.execute();

//            Scoreboard scoreboard = new Scoreboard();
//            scoreboard.setId(1);
//            scoreboard.setName("Nat");
//            scoreboard.setAndroid_id("ABC");
//            scoreboard.setStudyset(1);
//            scoreboard.setMode(0);
//            scoreboard.setOption(0);
//            scoreboard.setLang1("En");
//            scoreboard.setLang2("Jp");
//            scoreboard.setScore(2000);
//
//            APICaller apiCaller2 = new APICaller(dbManager, this);
//            apiCaller2.prepareGetScore(scoreboard);
//            apiCaller2.execute();
////
//            APICaller apiCaller3 = new APICaller(dbManager, this);
//            apiCaller3.preparePostScore(scoreboard);
//            apiCaller3.execute();
        }

        studysetAdapter = new StudysetAdapter(this, dbManager.getStudySet());

        ListView listView = (ListView) findViewById(R.id.studyset_listview);
        listView.setAdapter(studysetAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                Intent intent = new Intent(StudysetViewActivity.this, CardViewActivity.class);
                int studysetID = view.getId();
                String studysetName = ((TextView)view.findViewById(R.id.studyset_name)).getText().toString();
                String supportedLanguages = ((TextView)view.findViewById(R.id.studyset_language)).getText().toString();
                intent.putExtra(STUDYSET_ID, studysetID);
                intent.putExtra(STUDYSET_NAME, studysetName);
                intent.putExtra(STUDYSET_SUPPORTED_LANGUAGES, supportedLanguages);
                startActivity(intent);
            }
        });



    }

    public List<Studyset> getStudySet(){
        return dbManager.getStudySet();
    }

    @Override
    public void onTaskCompleted() {
        studysetAdapter.clear();
        studysetAdapter.addAll(dbManager.getStudySet());
        studysetAdapter.notifyDataSetChanged();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
