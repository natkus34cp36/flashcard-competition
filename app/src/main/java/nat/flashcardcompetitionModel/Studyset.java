package nat.flashcardcompetitionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nat on 2/14/17.
 */

public class Studyset {
    public int id;
    public String name;
    public String description;
    public String created;
    public String updated;
    public List<String> supported_language;
    public double highscore;

    public static final String STUDYSET_ID = "studyset_id";
    public static final String STUDYSET_NAME = "studyset_name";
    public static final String STUDYSET_SUPPORTED_LANGUAGES = "studyset_supported_language";

    public Studyset(){}

    public Studyset(int id, String name, List<String> languages, double highscore){
        this.id = id;
        this.name = name;
        this.supported_language = languages;
        this.highscore = highscore;
    }

    public String toString(){
        return this.id+": "+this.name;
    }

}
