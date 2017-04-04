package nat.flashcardcompetition;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nat.flashcardcompetitionModel.Card;
import nat.sqlite.DBManager;

import static nat.flashcardcompetitionModel.Studyset.STUDYSET_ID;

public class MatchingGameViewActivity extends AppCompatActivity {

    public static String SCORE = "SCORE";
    private TextView time, score;
    private GridView gridView;
    private int width, height, grid_margin;

    DBManager dbManager;
    int studySetId;
    String lang1, lang2;

    // Grid Params
    int item_width, grid_max_height, grid_padding;

    // Game Controller
    private CountDownTimer countDownTimer;
    private long min,sec,mil;
    private int time_limit;
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

        Intent intent = getIntent();
        time_limit = intent.getIntExtra("time_limit",60000);
        studySetId = intent.getIntExtra(STUDYSET_ID, 1);
        lang1 = intent.getStringExtra("lang1");
        lang2 = intent.getStringExtra("lang2");


        dbManager = new DBManager(this);
        dbManager.open();
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
        // using 93% for width
        width *= 0.94;
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
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp = (TextView)view.findViewById(R.id.matching_game_word);
                temp.setClickable(false);
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
                            temp.startAnimation(shake);
                            gridView.getChildAt(choosing_position).startAnimation(shake);

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
    }

    private void makeTimer(){
        // Make a timer.
        time = (TextView) findViewById(R.id.matching_time);
        countDownTimer = new CountDownTimer(time_limit, 30) {

            public void onTick(long millisUntilFinished) {
                Log.i("TIME", ""+millisUntilFinished);
                min = millisUntilFinished / 60000;
                sec = (millisUntilFinished % 60000) / 1000;
                mil = (millisUntilFinished%1000)/10;
                time.setText("Remaining: " + min + ":" + sec + ":" + mil);
            }

            public void onFinish() {
                time.setText("done!");
                Intent intent = new Intent(getApplication(),MatchingGameScoreActivity.class);
                intent.putExtra(SCORE,matching_score);
                startActivity(intent);
                finish();
            }


        }.start();
    }

    private void resetGame(){
        isClickable = new HashMap<>();
        state = READY;
        matched_pair = 0;

        int game_size = 16;
        for(int i=0;i<16;i++)
            isClickable.put(i,true);

        List<Card> cards = getCards();

        answerMap = new HashMap<>();
        for(int i=0;i<cards.size();i++){
            answerMap.put(cards.get(i).first,cards.get(i).second);
            answerMap.put(cards.get(i).second,cards.get(i).first);
        }

        MatchingGameAdapter matchingGameAdapter = new MatchingGameAdapter(this,cards,item_width);
        gridView.setAdapter(matchingGameAdapter);
    }

    private List<Card> getCards(){
        return dbManager.getCardByStudySetId(studySetId,lang1,lang2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
    }

    private final String tag = "TESTING";
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Log.v(tag,"I am in onpause");
        countDownTimer.cancel();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        Log.v(tag,"I am in onRestart");
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Log.v(tag,"I am in onresume");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        Log.v(tag,"I am in onstart");
        makeTimer();

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        Log.v(tag,"I am in onstop");
    }

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        countDownTimer = new CountDownTimer(time_limit, 30) {
//
//            public void onTick(long millisUntilFinished) {
//                Log.i("TIME", ""+millisUntilFinished);
//                time.setText("Remaining: " + millisUntilFinished / 60000 + ":" + (millisUntilFinished % 60000) / 1000 + ":" +(millisUntilFinished%1000)/10);
//            }
//
//            public void onFinish() {
//                time.setText("done!");
//                Intent intent = new Intent(getApplication(),MatchingGameScoreActivity.class);
//                intent.putExtra(SCORE,matching_score);
//                startActivity(intent);
//                finish();
//            }
//        }.start();
//    }
}
