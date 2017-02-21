package nat.flashcardcompetition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MatchingGameViewActivity extends AppCompatActivity {

    private TextView time, score;
    private GridView gridView;
    private int width, height, grid_margin;

    // Grid Params
    int item_width, grid_max_height, grid_padding;

    // Game Controller
    private int state = 0;
    private final int READY = 0;
    private final int CHOSEN = 1;
    private int matching_score = 0;

    private int matched_pair = 0;
    private String choosing_word = "";
    private int choosing_position = 0;
    private HashMap<String, String> answerMap;
    private HashMap<Integer, Boolean> isClickable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_game_view);

        gridView = (GridView) findViewById(R.id.matching_game);
        score = (TextView) findViewById(R.id.matching_score);

        /*** CALCULATING GRID DIMENSION and ITEM WIDTH***/
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        width = metrics.widthPixels;
        height = metrics.heightPixels;

        // minus the action bar height
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            height -= actionBarHeight;
        }

        grid_margin = (int)getResources().getDimension(R.dimen.small_gap);
        Log.e("Display info","width: "+width+", height: "+height);

        // there would be 3 space between 4 items. (for some reason, I cant use 5 for outer margin)
        item_width = ( width - (3 * grid_margin) ) / 4;
        grid_max_height = (int)(height*0.9); // the time and score is 10% of screen height.
        grid_padding = ( grid_max_height - width ) / 2;
        Log.i("Grid info","grid_max_height: " + grid_max_height + ", grid_padding: " + grid_padding +  "item_width: " + item_width);

        gridView.setPadding(0,grid_padding,0,grid_padding);
        gridView.setVerticalScrollBarEnabled(false);
        /*** *** *** *** *** *** *** *** *** *** *** *** *** ***/

        resetGame(); // get card, shuffle cards, reset game params, reset grid and its adapter.

        final Handler handler = new Handler();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp = (TextView)view.findViewById(R.id.matching_game_word);
                String word = temp.getText().toString();

                if(isClickable.get(position)) {
                    if (state == READY) {
                        int col = ContextCompat.getColor(getApplicationContext(), R.color.blue_green);
                        temp.setBackgroundColor(col);
                        temp.setBackground(getDrawable(R.drawable.game_item_correct));

                        choosing_word = word;
                        choosing_position = position;

                        state = CHOSEN;
                    } else if (state == CHOSEN) {

                        // correct
                        if (answerMap.get(choosing_word) == word) {
                            gridView.getChildAt(position).setAlpha(0);
                            gridView.getChildAt(choosing_position).setAlpha(0);

                            matching_score += 100;
                            score.setText("" + matching_score);

                            matched_pair ++;
                            if(matched_pair == 8){
                                resetGame();
                            }

                            // click the same one
                        } else if (position == choosing_position) {
                            temp.setBackground(getDrawable(R.drawable.game_item_ready));
                            // choosing word and position will be change in ready state again

                            // wrong
                        } else {
                            temp.setBackground(getDrawable(R.drawable.game_item_wrong));
                            gridView.getChildAt(choosing_position).setBackground(getDrawable(R.drawable.game_item_wrong));

                            matching_score -= 100;
                            if (matching_score < 0) matching_score = 0;
                            score.setText("" + matching_score);

                            isClickable.put(position, false);
                            isClickable.put(choosing_position, false);
                            final int temp_position1 = position;
                            final int temp_position2 = choosing_position;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    isClickable.put(temp_position1, true);
                                    isClickable.put(temp_position2, true);
                                    gridView.getChildAt(temp_position1).setBackground(getDrawable(R.drawable.game_item_ready));
                                    gridView.getChildAt(temp_position2).setBackground(getDrawable(R.drawable.game_item_ready));
                                }
                            }, 1000);
                        }
                        state = READY;

                    }
                }
                Log.e("NAT", position+", "+word);
            }
        });


        // Make a timer.
        time = (TextView) findViewById(R.id.matching_time);
        new CountDownTimer(60000, 30) {
            public void onTick(long millisUntilFinished) {
                time.setText("Remaining: " + millisUntilFinished / 1000 + ":" +(millisUntilFinished%1000)/10);
            }

            public void onFinish() {
                time.setText("done!");
            }
        }.start();

    }

    private void resetGame(){
        isClickable = new HashMap<>();
        state = READY;
        matched_pair = 0;

        for(int i=0;i<16;i++)
            isClickable.put(i,true);

        MatchingGameAdapter matchingGameAdapter = new MatchingGameAdapter(this,getCards(),item_width);
        gridView.setAdapter(matchingGameAdapter);
    }

    private ArrayList<Card> getCards(){
        // TODO get the real data
        ArrayList<Card> cards = new ArrayList<>();
        String[] front = {"Lust","Gluttony","Greed","Sloth","Wrath","Envy","Pride","Love"};
        String[] back = {"ความหื่น","ความตะกละ","ความโลภ","ความขี้เกียจ","ความโกรธ","ความขี้อิจฉา","ความยะโส","ความรัก"};
        answerMap = new HashMap<>();
        int n = front.length;
        // in this case, if there are two words with the same meaning, we can mark it as true!
        for(int i=0;i<n;i++){
            answerMap.put(front[i],back[i]);
            answerMap.put(back[i],front[i]);
        }
        for(int i=0;i<n;i++){
            Card temp = new Card(i,front[i],back[i],front[i],true);
            cards.add(temp);
        }
        return cards;
    }

}
