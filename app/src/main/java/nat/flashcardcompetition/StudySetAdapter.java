package nat.flashcardcompetition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nat on 2/14/17.
 */

public class StudysetAdapter extends ArrayAdapter<Studyset>{

    public StudysetAdapter(Context context, ArrayList<Studyset> studysets) {
        super(context, 0, studysets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Studyset studyset = getItem(position);
        String supported_languages = "";
        for(int i=0 ;i < studyset.supported_language.size()-1; i++){
            supported_languages += studyset.supported_language.get(i) + ", ";
        }
        supported_languages += studyset.supported_language.get(studyset.supported_language.size()-1);


        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_studyset_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.studyset_name);
        TextView language = (TextView) convertView.findViewById(R.id.studyset_language);
        TextView highscore = (TextView) convertView.findViewById(R.id.studyset_highscore);

        name.setText(studyset.name);
        language.setText(supported_languages);
        highscore.setText(studyset.highscore+"");
        convertView.setId(studyset.studysetID);

        return convertView;
    }





}
