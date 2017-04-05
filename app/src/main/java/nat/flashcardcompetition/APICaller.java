package nat.flashcardcompetition;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import nat.flashcardcompetitionModel.CardInfo;
import nat.flashcardcompetitionModel.Scoreboard;
import nat.flashcardcompetitionModel.Studyset;
import nat.sqlite.DBManager;

/**
 * Created by Nat on 2/25/2017.
 */

public class APICaller extends AsyncTask {

    private final String BASE_URL = "http://104.236.175.89:12000/";
    private final String STUDY_SET = "studyset/";
    private final String SCOREBOARD = "scoreboard/";

    private DBManager dbManager;
    private OnTaskCompleted listener1;
    private OnTaskCompleted2 listener2;
    private REQUEST_TYPE request_type;

    private String name,android_id,lang1,lang2;
    private int studyset,mode,option,score;

    private Scoreboard scoreboard;
    private List<Scoreboard> get_scoreboards;

    public enum REQUEST_TYPE{
        GET_STUDYSETS, GET_SCORE, POST_SCORE
    }

    public APICaller(DBManager dbManager, OnTaskCompleted listener1) {
        super();
        this.dbManager = dbManager;
        this.listener1 = listener1;
    }

    public APICaller(DBManager dbManager, OnTaskCompleted2 listener2) {
        super();
        this.dbManager = dbManager;
        this.listener2 = listener2;
    }

    public void prepareGetStudyset(){
        request_type = REQUEST_TYPE.GET_STUDYSETS;
    }

    public void prepareGetScore(Scoreboard scoreboard){
        request_type = REQUEST_TYPE.GET_SCORE;
        this.scoreboard = scoreboard;
    }

    public void preparePostScore(Scoreboard scoreboard){
        request_type = REQUEST_TYPE.POST_SCORE;
        this.scoreboard = scoreboard;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {

        switch (request_type){
            case GET_STUDYSETS:
                getStudySet();
                break;
            case GET_SCORE:
                return getScore();
            case POST_SCORE:
                postScore();
                break;
            default:
                getStudySet();
                break;
        }
        return null;
    }

    private void getStudySet(){
        try {
            URL url = new URL(BASE_URL+STUDY_SET);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            JsonStreamParser jsonStreamParser = new JsonStreamParser(bufferedReader);
            JsonElement json = jsonStreamParser.next();
            if(json.isJsonArray()){
                Iterator<JsonElement> studysets = json.getAsJsonArray().iterator();
                while(studysets.hasNext()){
                    JsonObject studyset = studysets.next().getAsJsonObject();

                    Studyset new_studyset = new Studyset();

                    new_studyset.id = Integer.parseInt(studyset.get("id").getAsString());
                    new_studyset.description = studyset.get("description").getAsString();
                    new_studyset.name = studyset.get("name").getAsString();
                    new_studyset.created = studyset.get("created").getAsString();
                    new_studyset.updated = studyset.get("updated").getAsString();
                    new_studyset.supported_language = new ArrayList<String>(Arrays.asList(studyset.get("supportedLanguages").getAsString().trim().split(",")));
                    new_studyset.highscore = 0;
                    Log.i("STUDYSET",new_studyset.toString());

                    Iterator<JsonElement> cards = studyset.getAsJsonArray("cards").iterator();
                    while(cards.hasNext()){
                        JsonObject card = cards.next().getAsJsonObject();

                        CardInfo cardInfo = new CardInfo();
                        cardInfo.id = Integer.parseInt(card.get("id").getAsString());
                        cardInfo.studySetId = Integer.parseInt(card.get("studySetId").getAsString());
                        cardInfo.cardId = Integer.parseInt(card.get("cardId").getAsString());
                        cardInfo.language = card.get("language").getAsString();
                        cardInfo.word = card.get("word").getAsString();

                        dbManager.insertCardInfo(cardInfo);
                    }
                    dbManager.insertStudySet(new_studyset);
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Scoreboard> getScore(){
        if(scoreboard.getLang1() == null || scoreboard.getLang2() == null || scoreboard.getStudyset() == 0)
            return null;

        try {
            String get_url = BASE_URL+SCOREBOARD;

            if(scoreboard.getLang1().compareTo(scoreboard.getLang2()) > 0){
                String temp = scoreboard.getLang1();
                scoreboard.setLang1(scoreboard.getLang2());
                scoreboard.setLang2(temp);
            }

            get_url += scoreboard.getStudyset() + "/" + scoreboard.getLang1() + "/" + scoreboard.getLang2();
            URL url = new URL(get_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            JsonStreamParser jsonStreamParser = new JsonStreamParser(bufferedReader);
            JsonElement json = jsonStreamParser.next();
            if(json.isJsonArray()){
                get_scoreboards = new ArrayList<>();
                Iterator<JsonElement> scoreboards = json.getAsJsonArray().iterator();
                while(scoreboards.hasNext()){
                    JsonObject scoreboard = scoreboards.next().getAsJsonObject();
                    Scoreboard new_scoreboard = new Scoreboard();

                    new_scoreboard.setId(scoreboard.get("id").getAsInt());
                    new_scoreboard.setName(scoreboard.get("name").getAsString());
                    new_scoreboard.setAndroid_id(scoreboard.get("androidId").getAsString());
                    new_scoreboard.setStudyset(scoreboard.get("studySetId").getAsInt());
                    new_scoreboard.setMode(scoreboard.get("mode").getAsInt());
                    new_scoreboard.setOption(scoreboard.get("option").getAsInt());
                    new_scoreboard.setLang1(scoreboard.get("language1").getAsString());
                    new_scoreboard.setLang2(scoreboard.get("language2").getAsString());
                    new_scoreboard.setScore(scoreboard.get("score").getAsInt());

                    Log.i("SCOREBOARD",new_scoreboard.toString());
                    get_scoreboards.add(new_scoreboard);
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return get_scoreboards;
    }

    private void postScore(){
        try {
            URL url = new URL(BASE_URL+SCOREBOARD);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            JSONObject json = new JSONObject();
            json.put("name", scoreboard.getName());
            json.put("androidId", scoreboard.getAndroid_id());
            json.put("studySetId", scoreboard.getStudyset());
            json.put("mode", scoreboard.getMode());
            json.put("option", scoreboard.getOption());
            json.put("language1", scoreboard.getLang1());
            json.put("language2", scoreboard.getLang2());
            json.put("score", scoreboard.getScore());

            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(json.toString());
            wr.close();

            int status = urlConnection.getResponseCode();
            Log.i("POST STATUS", status+"");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(listener1 != null)
            listener1.onTaskCompleted();
        if(listener2 != null)
            listener2.onTaskCompleted(o);
    }


}
