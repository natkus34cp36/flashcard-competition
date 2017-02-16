package nat.flashcardcompetition;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nat on 2/16/2017.
 */

public class MatchingGameAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> front;
    private ArrayList<String> back;
    private int m,n;
    private final int MAXIMUM_WORDS = 10;
    public MatchingGameAdapter(Context c, ArrayList<String> front, ArrayList<String> back) {
        mContext = c;
        this.front = front;
        this.back = back;
    }

    public int getCount() {
        return front.size() + back.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            textView = new TextView(mContext);
            textView.setText(""+position);
            textView.setLayoutParams(new GridView.LayoutParams(85, 85));
            textView.setPadding(8, 8, 8, 8);
        } else {
            textView = (TextView) convertView;
        }

        return textView;
    }

    public void setGrid(ArrayList<Card> cards){
        int number_of_cards = cards.size() * 2;

    }

    public void setGrid(ArrayList<Card> cards, int m, int n){
        if(m * n % 2 != 0  && m*n /2 <= MAXIMUM_WORDS){
            Log.i("Matching Grid", "Has to be even number for grid with 10 maximum number of words");
            return ;
        }
        this.m = m;
        this.n = n;
    }



}

