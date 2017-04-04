package nat.flashcardcompetition;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import static nat.flashcardcompetitionModel.Studyset.STUDYSET_ID;

public class GameOptionViewActivity extends AppCompatActivity {

    int timeLimit;
    int studySetId;
    String lang1,lang2;

    private final int THIRTY_SECONDS = 30000;
    private final int ONE_MINUTE = 60000;
    private final int TWO_MINUTES= 120000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_option_view);
        timeLimit = ONE_MINUTE;
        Intent intent = getIntent();
        studySetId = intent.getIntExtra(STUDYSET_ID, 1);
        lang1 = intent.getStringExtra("lang1");
        lang2 = intent.getStringExtra("lang2");

        if(isNetworkAvailable()){
            TextView notice = (TextView) findViewById(R.id.notice);
            notice.setAlpha(0);
        }
    }

    public void start(View view){
        Intent intent = new Intent(this, MatchingGameViewActivity.class);
        intent.putExtra("time_limit", timeLimit);
        intent.putExtra(STUDYSET_ID, studySetId);
        intent.putExtra("lang1",lang1);
        intent.putExtra("lang2",lang2);
        startActivity(intent);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.thirty_seconds:
                if (checked)
                    timeLimit = THIRTY_SECONDS;
                    break;
            case R.id.one_minute:
                if (checked)
                    timeLimit = ONE_MINUTE;
                    break;
            case R.id.two_minute:
                if (checked)
                    timeLimit = TWO_MINUTES;
                    break;
        }
    }
}
