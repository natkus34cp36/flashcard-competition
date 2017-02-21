package nat.flashcardcompetition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MatchingGameScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_game_score);

        TextView prefix = (TextView) findViewById(R.id.matching_scoreview_prefix);
        TextView matching_scoreview_score = (TextView) findViewById(R.id.matching_scoreview_score);

        Intent intent = getIntent();
        int score = intent.getIntExtra(MatchingGameViewActivity.SCORE,0);

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
}
