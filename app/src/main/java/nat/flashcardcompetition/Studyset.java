package nat.flashcardcompetition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nat on 2/14/17.
 */

public class Studyset {
    int studysetID;
    String name;
    List<String> supported_language;
    double highscore;

    public Studyset(int studysetID, String name, List<String> languages, double highscore){
        this.studysetID = studysetID;
        this.name = name;
        this.supported_language = languages;
        this.highscore = highscore;
    }

}
