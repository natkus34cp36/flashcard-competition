package nat.flashcardcompetition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GameOptionViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_option_view);
    }

    public void start(View view){
        Intent intent = new Intent(this, MatchingGameViewActivity.class);
        startActivity(intent);
    }
}
