package nat.flashcardcompetition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nat.flashcardcompetitionModel.Scoreboard;
import nat.flashcardcompetitionModel.Studyset;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nat.flashcardcompetitionModel.Studyset;

/**
 * Created by Nat on 4/5/2017.
 */


public class ScoreboardAdapter extends ArrayAdapter<Scoreboard> {

    List<Scoreboard> scoreboards;

    public ScoreboardAdapter(Context context, List<Scoreboard> scoreboards) {
        super(context, 0, scoreboards);
        this.scoreboards = scoreboards;
    }

    @Override
    public int getCount() {
        return scoreboards.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Scoreboard scoreboard = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_scoreboard_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.scoreboard_name);
        TextView score = (TextView) convertView.findViewById(R.id.scoreboard_score);

        name.setText(scoreboard.getName());
        score.setText(String.valueOf(scoreboard.getScore()));
        convertView.setId(scoreboard.getId());
        return convertView;
    }

}
