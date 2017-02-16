package nat.flashcardcompetition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.database.DataSetObserver;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class MatchingGameViewActivity extends AppCompatActivity {

    private TextView time;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_game_view);

        time = (TextView) findViewById(R.id.matching_time);
        gridView = (GridView) findViewById(R.id.matching_game);

        new CountDownTimer(60000, 30) {
            public void onTick(long millisUntilFinished) {
                time.setText("Remaining: " + millisUntilFinished / 1000 + ":" +(millisUntilFinished%1000)/10);
            }

            public void onFinish() {
                time.setText("done!");
            }
        }.start();



    }

    private ArrayList<Card> getCards(){
        ArrayList<Card> cards = new ArrayList<>();
        String[] firsts = {"Lust","Gluttony","Greed","Sloth","Wrath","Envy","Pride","Love"};
        String[] seconds = {"ความหื่น","ความตะกละ","ความโลภ","ความขี้เกียจ","ความโกรธ","ความขี้อิจฉา","ความยะโส","ความรัก"};
        for(int i=0;i<firsts.length;i++){
            Card temp = new Card(i,firsts[i],seconds[i],firsts[i],true);
            cards.add(temp);
        }
        return cards;
    }

    private void generateGrid(){

    }
}
