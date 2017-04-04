package nat.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nat on 2/23/2017.
 */

public class FlashcardCompetitionDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FlashcardCompetition.db";

    private static final String SQL_CREATE_STUDYSET =
            "CREATE TABLE " + FlashcardCompetitionContract.Studyset.TABLE_NAME + " (" +
                    FlashcardCompetitionContract.Studyset.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    FlashcardCompetitionContract.Studyset.COLUMN_NAME_NAME + " TEXT," +
                    FlashcardCompetitionContract.Studyset.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    FlashcardCompetitionContract.Studyset.COLUMN_NAME_CREATED + " TEXT," +
                    FlashcardCompetitionContract.Studyset.COLUMN_NAME_UPDATED + " TEXT," +
                    FlashcardCompetitionContract.Studyset.COLUMN_NAME_SUPPORTED_LANGUAGE + " TEXT," +
                    " UNIQUE (" + FlashcardCompetitionContract.Studyset.COLUMN_NAME_ID + "))";

    private static final String SQL_DELETE_STUDYSET =
            "DROP TABLE IF EXISTS " + FlashcardCompetitionContract.Studyset.TABLE_NAME;

    private static final String SQL_CREATE_CARD =
            "CREATE TABLE " + FlashcardCompetitionContract.CardInfo.TABLE_NAME + " (" +
                    FlashcardCompetitionContract.CardInfo.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    FlashcardCompetitionContract.CardInfo.COLUMN_NAME_STUDYSET_ID + " TEXT," +
                    FlashcardCompetitionContract.CardInfo.COLUMN_NAME_CARD_ID + " TEXT," +
                    FlashcardCompetitionContract.CardInfo.COLUMN_NAME_LANGUAUGE + " TEXT," +
                    FlashcardCompetitionContract.CardInfo.COLUMN_NAME_WORD + " TEXT," +
                    " UNIQUE (" + FlashcardCompetitionContract.CardInfo.COLUMN_NAME_ID + "))";

    private static final String SQL_DELETE_CARD =
            "DROP TABLE IF EXISTS " + FlashcardCompetitionContract.CardInfo.TABLE_NAME;

    private static final String SQL_CREATE_CARDMETA =
            "CREATE TABLE " + FlashcardCompetitionContract.CardMeta.TABLE_NAME + " (" +
                    FlashcardCompetitionContract.CardMeta.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    FlashcardCompetitionContract.CardMeta.COLUMN_NAME_CARD_ID + " TEXT," +
                    FlashcardCompetitionContract.CardMeta.COLUMN_NAME_IS_ACTIVE + " INTEGER," +
                    " UNIQUE (" + FlashcardCompetitionContract.CardMeta.COLUMN_NAME_CARD_ID + "))";

    private static final String SQL_DELETE_CARDMETA =
            "DROP TABLE IF EXISTS " + FlashcardCompetitionContract.CardMeta.TABLE_NAME;

    public FlashcardCompetitionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STUDYSET);
        db.execSQL(SQL_CREATE_CARD);
        db.execSQL(SQL_CREATE_CARDMETA);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_STUDYSET);
        db.execSQL(SQL_DELETE_CARD);
        db.execSQL(SQL_DELETE_CARDMETA);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
