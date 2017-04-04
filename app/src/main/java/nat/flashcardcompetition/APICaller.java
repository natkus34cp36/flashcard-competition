package nat.flashcardcompetition;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import nat.flashcardcompetitionModel.CardInfo;
import nat.flashcardcompetitionModel.Studyset;
import nat.sqlite.DBManager;

/**
 * Created by Nat on 2/25/2017.
 */

public class APICaller extends AsyncTask {

    private final String BASE_URL = "http://104.236.175.89:12000/";
    private final String STUDY_SET = "studyset/";
    private DBManager dbManager;
    private OnTaskCompleted listener;

    public enum REQUEST_TYPE{
        GET_STUDYSETS, GET_SCORE, POST_SCORE
    }

    public APICaller(DBManager dbManager, OnTaskCompleted listener) {
        super();
        this.dbManager = dbManager;
        this.listener = listener;
    }

    public APICaller(DBManager dbManager, OnTaskCompleted listener, REQUEST_TYPE request_type, Object args) {
        super();
        this.dbManager = dbManager;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        getStudySet();
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
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        listener.onTaskCompleted();
    }


}
