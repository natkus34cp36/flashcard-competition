package nat.flashcardcompetitionModel;

/**
 * Created by Nat on 4/5/2017.
 */

public class Scoreboard {

    private int id;
    private String name;
    private String android_id;
    private int studyset;
    private int mode;
    private int option;
    private String lang1;
    private String lang2;
    private int score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }

    public int getStudyset() {
        return studyset;
    }

    public void setStudyset(int studyset) {
        this.studyset = studyset;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public String getLang1() {
        return lang1;
    }

    public void setLang1(String lang1) {
        this.lang1 = lang1;
    }

    public String getLang2() {
        return lang2;
    }

    public void setLang2(String lang2) {
        this.lang2 = lang2;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String toString(){
        return android_id + ":" + name + "," + studyset + ":" + mode + ":" + option + ":" + lang1 + ":" + lang2 + " score: " + score;
    }

}
