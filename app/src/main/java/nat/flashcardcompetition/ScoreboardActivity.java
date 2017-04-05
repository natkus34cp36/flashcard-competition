package nat.flashcardcompetition;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import nat.flashcardcompetitionModel.Scoreboard;
import nat.sqlite.DBManager;

import static nat.flashcardcompetitionModel.Studyset.STUDYSET_ID;

public class ScoreboardActivity extends AppCompatActivity implements OnTaskCompleted2{

    int studySetId;
    String lang1,lang2;

    Button option1_btn,option2_btn,option3_btn;

    ScoreboardAdapter scoreboardAdapter;
    List<Scoreboard> option1;
    List<Scoreboard> option2;
    List<Scoreboard> option3;

    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        studySetId = intent.getIntExtra(STUDYSET_ID, 1);
        lang1 = intent.getStringExtra("lang1");
        lang2 = intent.getStringExtra("lang2");

        dbManager = new DBManager(this);
        dbManager.open();

        ListView listView = (ListView) findViewById(R.id.scoreboard_listview);
        TextView emptyText = new TextView(this);
        emptyText.setText("There is no record in this scoreboard");
        listView.setEmptyView(emptyText);

        option1_btn = (Button) findViewById(R.id.option1_button);
        option2_btn = (Button) findViewById(R.id.option2_button);
        option3_btn = (Button) findViewById(R.id.option3_button);
        optionClicked(option2_btn);

        option2 = new ArrayList<>();
        scoreboardAdapter = new ScoreboardAdapter(this, option2);
        listView.setAdapter(scoreboardAdapter);

        if(isNetworkAvailable()){
            APICaller apiCaller = new APICaller(dbManager, this);
            Scoreboard scoreboard = new Scoreboard();
            scoreboard.setStudyset(studySetId);
            scoreboard.setLang1(lang1);
            scoreboard.setLang2(lang2);
            apiCaller.prepareGetScore(scoreboard);
            apiCaller.execute();
        }

    }

    @Override
    public void onTaskCompleted(Object object) {
        List<Scoreboard> scoreboards = (List<Scoreboard>) object;
        HashMap<Integer, List<Scoreboard>> hashMap = new HashMap<>();
        hashMap.put(0,new ArrayList<Scoreboard>());
        hashMap.put(1,new ArrayList<Scoreboard>());
        hashMap.put(2,new ArrayList<Scoreboard>());
        for (Scoreboard scoreboard: scoreboards){
            switch (scoreboard.getOption()) {
                case 0:
                    hashMap.get(0).add(scoreboard);
                    break;
                case 1:
                    hashMap.get(1).add(scoreboard);
                    break;
                case 2:
                    hashMap.get(2).add(scoreboard);
                    break;
                default:
                    break;
            }
        }
        option1 = hashMap.get(0);
        option2 = hashMap.get(1);
        option3 = hashMap.get(2);

        Collections.sort(option1, new CustomComparator());
        Collections.sort(option2, new CustomComparator());
        Collections.sort(option3, new CustomComparator());

        scoreboardAdapter.addAll(option2);
        scoreboardAdapter.notifyDataSetChanged();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showOption1(View target){
        scoreboardAdapter.clear();
        scoreboardAdapter.addAll(option1);
        scoreboardAdapter.notifyDataSetChanged();
        optionClicked(target);
    }

    public void showOption2(View target){
        scoreboardAdapter.clear();
        scoreboardAdapter.addAll(option2);
        scoreboardAdapter.notifyDataSetChanged();
        optionClicked(target);
    }

    public void showOption3(View target){
        scoreboardAdapter.clear();
        scoreboardAdapter.addAll(option3);
        scoreboardAdapter.notifyDataSetChanged();
        optionClicked(target);
    }

    public void optionClicked(View target){
        option1_btn.setBackground(getDrawable(R.drawable.button));
        option2_btn.setBackground(getDrawable(R.drawable.button));
        option3_btn.setBackground(getDrawable(R.drawable.button));
        target.setBackground(getDrawable(R.drawable.button_clicked));
    }

    public class CustomComparator implements Comparator<Scoreboard> {

        @Override
        public int compare(Scoreboard s1, Scoreboard s2) {
            if(s1.getScore() < s2.getScore())
                return 1;
            if(s1.getScore() > s2.getScore())
                return -1;
            else return 0;
        }
    }
}
