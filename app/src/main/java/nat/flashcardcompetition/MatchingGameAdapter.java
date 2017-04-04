package nat.flashcardcompetition;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nat.flashcardcompetitionModel.Card;

/**
 * Created by Nat on 2/16/2017.
 */

public class MatchingGameAdapter extends BaseAdapter {

    private Context context;
    private List<Card> cards;
    private List<String> shuffledCards;
    private int item_width;
    private final int MAXIMUM_WORDS = 8;


    public MatchingGameAdapter(Context c, List<Card> cards, int item_width) {
        context = c;
        this.cards = cards;
        shuffledCards = shuffleCards(cards);
        this.item_width = item_width;
        Log.i("NAT", ""+item_width);
    }

    public int getCount() {
        return shuffledCards.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView = inflater.inflate(R.layout.view_matching_game_item, null);

        TextView textView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes

            textView = (TextView) gridView.findViewById(R.id.matching_game_word);
            textView.setLayoutParams(new GridView.LayoutParams(item_width, item_width));
            textView.setBackground(context.getDrawable(R.drawable.game_item_ready));
            textView.setText(shuffledCards.get(position));

        } else {
            textView = (TextView) convertView;
        }

        return textView;
    }

    // input cards, return list of string
    public List<String> shuffleCards(List<Card> cards){

        // TODO check shuffle again. Its broken.
        // choose 8 cards from the deck
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i=0 ; i < cards.size() ; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);

        ArrayList<Card> selectedCards = new ArrayList<>();
        for (int i=0 ; i < MAXIMUM_WORDS ; i++) {
            selectedCards.add(cards.get(list.get(i)));
        }

        // random the order of cards,, shuffle to grid
        list.clear();
        for (int i=0 ; i < MAXIMUM_WORDS*2 ; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);

        // get the list of number 1-n // will compare it to cards list
        ArrayList<String> shuffledCards = new ArrayList<>();
        for (int i=0 ; i< MAXIMUM_WORDS*2 ; i++) {
            int index = list.get(i);
            if(index % 2 == 0) {
                shuffledCards.add(selectedCards.get(index/2).first);
            }else{
                shuffledCards.add(selectedCards.get(index/2).second);
            }
        }
        return shuffledCards;

    }



}

