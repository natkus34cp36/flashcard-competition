package nat.flashcardcompetition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import nat.flashcardcompetitionModel.Scoreboard;
import nat.flashcardcompetitionModel.Studyset;
import nat.sqlite.DBManager;

public class MatchingGameScoreActivity extends AppCompatActivity implements OnTaskCompleted{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_game_score);

        TextView prefix = (TextView) findViewById(R.id.matching_scoreview_prefix);
        TextView matching_scoreview_score = (TextView) findViewById(R.id.matching_scoreview_score);

        Intent intent = getIntent();
        int score = intent.getIntExtra(MatchingGameViewActivity.SCORE,0);
        int studySetId = intent.getIntExtra(Studyset.STUDYSET_ID, 0);
        String lang1 = intent.getStringExtra("lang1");
        String lang2 = intent.getStringExtra("lang2");

        // lexi ordering (playing matching game with En,Jp and Jp,En should have the same score board;
        if(lang1.compareTo(lang2) >= 0){
            String temp = lang1;
            lang1 = lang2;
            lang2 = temp;
        }

        int time_limit = intent.getIntExtra("time_limit",60000);

        int option;
        if(time_limit == 30000)
            option = 0;
        else if(time_limit == 60000)
            option = 1;
        else
            option = 2;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Scoreboard scoreboard = new Scoreboard();

        scoreboard.setId(0);
        scoreboard.setName(sharedPreferences.getString("name","unknown"));
        scoreboard.setAndroid_id(sharedPreferences.getString("android_id","unknown"));
        scoreboard.setStudyset(studySetId);
        scoreboard.setMode(0);
        scoreboard.setOption(option);
        scoreboard.setLang1(lang1);
        scoreboard.setLang2(lang2);
        scoreboard.setScore(score);

        DBManager dbManager = new DBManager(this);
        dbManager.open();

        APICaller apiCaller = new APICaller(dbManager, this);
        apiCaller.preparePostScore(scoreboard);
        apiCaller.execute();

        if(score<400) {
            prefix.setText("Come on, you can do better than that.");
        }else if(score <= 800) {
            prefix.setText("Good Job. Keep doing great!");
        } else if(score <= 1600){
            prefix.setText("Marvelous!");
        } else if(score > 1600){
            prefix.setText("Genius.");
        }
        matching_scoreview_score.setText(""+score);

    }

    public void close(View view){
        finish();
    }

    @Override
    public void onTaskCompleted() {
        return ;
    }
}
