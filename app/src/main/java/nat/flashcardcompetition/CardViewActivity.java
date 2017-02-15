package nat.flashcardcompetition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CardViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

//        Button study_button = (Button)findViewById(R.id.study_button);
//        Button game_button = (Button)findViewById(R.id.game_button);
//        Button scoreboard_button = (Button)findViewById(R.id.scoreboard_button);


    }

    public void StudyListener(View target){
        Intent intent = new Intent(this, StudyViewAcitivity.class);
        startActivity(intent);
    }

    public void MatchingListener(View target){
        Intent intent = new Intent(this, StudyViewAcitivity.class);
        startActivity(intent);
    }

    public void ScoreboardListener(View target){
        Intent intent = new Intent(this, StudyViewAcitivity.class);
        startActivity(intent);
    }
}
