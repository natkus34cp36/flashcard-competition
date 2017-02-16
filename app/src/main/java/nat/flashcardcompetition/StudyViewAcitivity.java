package nat.flashcardcompetition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class StudyViewAcitivity extends AppCompatActivity {

    ArrayList<Card> cards;
    TextView card_front, card_back;
    int pos,n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_view);

        cards = getCards();
        card_front = (TextView) findViewById(R.id.study_card_front);
        card_back = (TextView) findViewById(R.id.study_card_back);
        pos = 0;
        n = cards.size();
        setText();
    }

    public void flip(View view){
        boolean isFrontVisible = card_front.getAlpha() > 0;
        if(isFrontVisible) {
            card_front.setAlpha(0);
            card_back.setAlpha(1);
        }else{
            card_front.setAlpha(1);
            card_back.setAlpha(0);
        }
    }

    public void prev(View view){
        if( --pos<0 ) pos = n - 1;
        setText();
    }

    public void next(View view){
        if( ++pos>=n ) pos = 0;
        setText();
    }

    private void setText(){
        card_front.setText(cards.get(pos).first);
        card_back.setText(cards.get(pos).second);
    }

    private ArrayList<Card> getCards(){
        ArrayList<Card> cards = new ArrayList<>();
        String[] firsts = {"Lust","Gluttony","Greed","Sloth","Wrath","Envy","Pride"};
        String[] seconds = {"ความหื่น","ความตะกละ","ความโลภ","ความขี้เกียจ","ความโกรธ","ความขี้อิจฉา","ความยะโส"};
        for(int i=0;i<firsts.length;i++){
            Card temp = new Card(i,firsts[i],seconds[i],firsts[i],true);
            cards.add(temp);
        }
        return cards;
    }
}
