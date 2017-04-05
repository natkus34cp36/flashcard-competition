package nat.flashcardcompetition;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nat.flashcardcompetitionModel.Card;

/**
 * Created by Nat on 2/15/2017.
 */

public class CardAdapter extends ArrayAdapter<Card> {

    public CardAdapter(Context context, List<Card> cards) {
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

        // TODO create active status
//        ImageView active_status = (ImageView) convertView.findViewById(R.id.active_status);
//        active_status.setTag("isActive");
//        active_status.setOnClickListener(new ImageView.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(v.getTag() == "isActive"){
//                    v.setBackgroundResource(R.drawable.ic_radio_button_unchecked_black_24dp);
//                    v.setBackground(getContext().getDrawable(R.drawable.ic_radio_button_unchecked_black_24dp));
//                    v.setTag("isNotActive");
//                    Log.i("ACTIVE","SHOULD BE INACTIVE");
//                }else{
//                    v.setBackgroundResource(R.drawable.ic_radio_button_checked_black_24dp);
//                    v.setBackground(getContext().getDrawable(R.drawable.ic_radio_button_checked_black_24dp));
//                    v.setTag("isActive");
//                    Log.i("ACTIVE","SHOULD BE ACTIVE");
//                }
//                Log.i("ACTIVE STATUS",v.getTag()+"");
//            }
//        });

        first.setText(card.first);
        second.setText(card.second);
        convertView.setId(card.cardID);

        return convertView;
    }

}
