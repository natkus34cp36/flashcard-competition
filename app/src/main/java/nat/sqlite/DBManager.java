package nat.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import nat.flashcardcompetitionModel.Card;
import nat.flashcardcompetitionModel.CardInfo;
import nat.flashcardcompetitionModel.CardMeta;
import nat.flashcardcompetitionModel.Studyset;

/**
 * Created by Nat on 2/23/2017.
 */

public class DBManager {

    private FlashcardCompetitionDbHelper dbHelper;
    private Context context;
    private static SQLiteDatabase database;

    public DBManager(Context c){
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new FlashcardCompetitionDbHelper(context);
        database = dbHelper.getWritableDatabase();
//        dbHelper.onUpgrade(database,1,2);
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    /*** TESTING PURPOSE ***/
    // TODO Delete this
    public void deleteAllTables(){
        Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> tables = new ArrayList<>();

        // iterate over the result set, adding every table name to a list
        while (c.moveToNext()) {
            tables.add(c.getString(0));
        }

        // call DROP TABLE on every table name
        for (String table : tables) {
            String dropQuery = "DROP TABLE IF EXISTS " + table;
            database.execSQL(dropQuery);
        }
    }

    public List<Studyset> getStudySet(){
        Cursor cursor = database.query(FlashcardCompetitionContract.Studyset.TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {

        } else {
            return null;
        }
        List<Studyset> studySets = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.Studyset.COLUMN_NAME_ID)
            );
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.Studyset.COLUMN_NAME_NAME)
            );
            String description = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.Studyset.COLUMN_NAME_DESCRIPTION)
            );
            String created = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.Studyset.COLUMN_NAME_CREATED)
            );
            String updated = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.Studyset.COLUMN_NAME_UPDATED)
            );
            String supported_languages = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.Studyset.COLUMN_NAME_SUPPORTED_LANGUAGE)
            );
            Studyset studySet = new Studyset();
            studySet.id = id;
            studySet.name = name;
            studySet.description = description;
            studySet.created = created;
            studySet.updated = updated;
            studySet.supported_language = new ArrayList<>(Arrays.asList(supported_languages.trim().split(",")));
            studySet.highscore = 0;
            studySets.add(studySet);
        }
        cursor.close();
        return studySets;
    }

    public void insertStudySet(Studyset studyset){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FlashcardCompetitionContract.Studyset.COLUMN_NAME_ID, studyset.id);
        contentValues.put(FlashcardCompetitionContract.Studyset.COLUMN_NAME_NAME, studyset.name);
        contentValues.put(FlashcardCompetitionContract.Studyset.COLUMN_NAME_DESCRIPTION, studyset.description);
        contentValues.put(FlashcardCompetitionContract.Studyset.COLUMN_NAME_CREATED, studyset.created);
        contentValues.put(FlashcardCompetitionContract.Studyset.COLUMN_NAME_UPDATED, studyset.updated);

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : studyset.supported_language) {
            stringBuilder.append(s);
            stringBuilder.append(",");
        }
        contentValues.put(FlashcardCompetitionContract.Studyset.COLUMN_NAME_SUPPORTED_LANGUAGE, stringBuilder.toString());
        int id = (int) database.insertWithOnConflict(FlashcardCompetitionContract.Studyset.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            database.update(FlashcardCompetitionContract.Studyset.TABLE_NAME,
                    contentValues,
                    FlashcardCompetitionContract.Studyset.COLUMN_NAME_ID+"=?",
                    new String[] {studyset.id+""});
        }
    }

    public void insertCardInfo(CardInfo cardInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_ID, cardInfo.id);
        contentValues.put(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_CARD_ID, cardInfo.cardId);
        contentValues.put(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_STUDYSET_ID, cardInfo.studySetId);
        contentValues.put(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_LANGUAUGE, cardInfo.language);
        contentValues.put(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_WORD, cardInfo.word);
        int id = (int) database.insertWithOnConflict(FlashcardCompetitionContract.CardInfo.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            database.update(FlashcardCompetitionContract.CardInfo.TABLE_NAME,
                    contentValues,
                    FlashcardCompetitionContract.CardMeta.COLUMN_NAME_ID+"=?",
                    new String[] {cardInfo.id+""});
        }
    }

    public List<Card> getCardByStudySetId(int studySetId, String lang1, String lang2){

        String selection = FlashcardCompetitionContract.CardInfo.COLUMN_NAME_STUDYSET_ID + "=?";
        String[] selectionArgs = new String[] {studySetId+""};
//        String selectionArgs = studySetId+"";
        Cursor cursor = database.query(FlashcardCompetitionContract.CardInfo.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor != null) {

        } else {
            return null;
        }

        List<CardInfo> cardInfos = new ArrayList<>();
        while(cursor.moveToNext()) {
            CardInfo cardInfo = new CardInfo();
            cardInfo.id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_ID)
            );
            cardInfo.studySetId = studySetId;
            cardInfo.cardId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_CARD_ID)
            );
            cardInfo.language = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_LANGUAUGE)
            );
            cardInfo.word = cursor.getString(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_WORD)
            );
            cardInfos.add(cardInfo);
        }
        cursor.close();

        HashMap<Integer, List<CardInfo>> hashMap = new HashMap<>();
        for (CardInfo info : cardInfos){
            if(info.language.equals(lang1) || info.language.equals(lang2)){
                if(hashMap.containsKey(info.cardId)){
                    hashMap.get(info.cardId).add(info);
                } else {
                    List<CardInfo> temp = new ArrayList<>();
                    temp.add(info);
                    hashMap.put(info.cardId,temp);
                }
            }
        }

        List<Card> cards = new ArrayList<>();
        for(Integer key : hashMap.keySet()){

            List<CardInfo> temp = hashMap.get(key);
            if(temp.size() != 2) continue;

            Card card = new Card();
            CardInfo card1 = temp.get(0);
            CardInfo card2 = temp.get(1);
            card.cardID = card1.cardId;
//            Log.i("BEFORE CARD", "lang1: " + lang1 + ", lang2: " + lang2 + ", card1: " + card1.language + ", " + card1.word + " card2: " + card2.language + ", " + card2.word );
            if(card1.language.equals(lang1)){
                card.first = card1.word;
                card.second = card2.word;
            } else {
                card.first = card2.word;
                card.second = card1.word;
            }
//            Log.i("AFTER CARD", "lang1: " + lang1 + ", lang2: " + lang2 + ", card-front: " + card.first + " card-back: " + card.second);
            // TODO check card meta for the active.
            card.active = true;
            cards.add(card);
        }
        return cards;
    }

    public void insertCardMeta(CardMeta cardMeta){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FlashcardCompetitionContract.CardMeta.COLUMN_NAME_CARD_ID, cardMeta.cardID);
        contentValues.put(FlashcardCompetitionContract.CardMeta.COLUMN_NAME_IS_ACTIVE, cardMeta.isActive);
        int id = (int) database.insertWithOnConflict(FlashcardCompetitionContract.CardMeta.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        if (id == -1) {
            database.update(FlashcardCompetitionContract.CardMeta.TABLE_NAME,
                    contentValues,
                    FlashcardCompetitionContract.CardMeta.COLUMN_NAME_CARD_ID+"=?",
                    new String[] {cardMeta.cardID+""});  // number 1 is the _id here, update to variable for your code
        }

    }

    public ArrayList<CardMeta> getCardMeta(){
        String[] columns = new String[] {FlashcardCompetitionContract.CardMeta.COLUMN_NAME_ID, FlashcardCompetitionContract.CardMeta.COLUMN_NAME_CARD_ID, FlashcardCompetitionContract.CardMeta.COLUMN_NAME_IS_ACTIVE};
        Cursor cursor = database.query(FlashcardCompetitionContract.CardMeta.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {

        } else {
            return null;
        }

        ArrayList<CardMeta> cardMetas = new ArrayList<>();
        while(cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.CardMeta.COLUMN_NAME_ID)
            );

            int cardID = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.CardMeta.COLUMN_NAME_CARD_ID)
            );
            int isActive = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FlashcardCompetitionContract.CardMeta.COLUMN_NAME_IS_ACTIVE)
            );
            cardMetas.add(new CardMeta(id,cardID, isActive));
        }
        cursor.close();
        return cardMetas;
    }

    public void updateCardMeta(CardMeta cardMeta){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FlashcardCompetitionContract.CardMeta.COLUMN_NAME_IS_ACTIVE, cardMeta.isActive);

        String selection = FlashcardCompetitionContract.CardMeta.COLUMN_NAME_CARD_ID + " LIKE ?";
        String[] selectionArgs = { ""+cardMeta.cardID };
        int count = database.update(FlashcardCompetitionContract.CardMeta.TABLE_NAME,contentValues,selection,selectionArgs);
//        Log.i("UpdateCardMeta :", "Number of Update = " +count);
    }
}
