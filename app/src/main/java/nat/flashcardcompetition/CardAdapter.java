package nat.flashcardcompetition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nat on 2/15/2017.
 */

public class CardAdapter extends ArrayAdapter<Card> {

    public CardAdapter(Context context, ArrayList<Card> cards) {
        super(context, 0, cards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Card card = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_card_item, parent, false);
        }

        TextView first = (TextView) convertView.findViewById(R.id.card_first);
        TextView second = (TextView) convertView.findViewById(R.id.card_second);
//        TextView pronunciation = (TextView) convertView.findViewById(R.id.card_pronunciation);

        first.setText(card.first);
        second.setText(card.second);
        convertView.setId(card.cardID);

        return convertView;
    }

}
