package nat.flashcardcompetitionDAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import nat.flashcardcompetitionModel.Card;
import nat.sqlite.FlashcardCompetitionContract;

/**
 * Created by Nat on 3/31/2017.
 */

public class CardDAO {

    public static List<Card> getCardByStudyset(SQLiteDatabase database, int studyset_id){
        List<Card> cards = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT * FROM " + FlashcardCompetitionContract.CardInfo.TABLE_NAME + " WHERE " + FlashcardCompetitionContract.CardInfo.COLUMN_NAME_STUDYSET_ID + " = " +studyset_id, null);
        while (c.moveToNext()) {
            Card card = new Card();
//            card.cardID = c.getInt(c.getColumnIndex(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_CARD_ID));
//            card.first = c.getInt(c.getColumnIndex(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_CARD_ID));
//            card.second = c.getInt(c.getColumnIndex(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_CARD_ID));
//            card.cardID = c.getInt(c.getColumnIndex(FlashcardCompetitionContract.CardInfo.COLUMN_NAME_CARD_ID));
//            tables.add(c.getString(0));
        }


        return cards;
    }
}
