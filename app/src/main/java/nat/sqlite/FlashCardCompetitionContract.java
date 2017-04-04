package nat.sqlite;

import android.provider.BaseColumns;

/**
 * Created by Nat on 2/23/2017.
 */

public final class FlashcardCompetitionContract {

    private FlashcardCompetitionContract(){}

    public static class CardInfo implements BaseColumns {
        public static final String TABLE_NAME = "card";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_STUDYSET_ID = "studyset_id";
        public static final String COLUMN_NAME_CARD_ID = "card_id";
        public static final String COLUMN_NAME_LANGUAUGE = "language";
        public static final String COLUMN_NAME_WORD = "word";
    }

    public static class CardMeta implements BaseColumns {
        public static final String TABLE_NAME = "cardmeta";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CARD_ID = "card_id";
        public static final String COLUMN_NAME_IS_ACTIVE = "isactive";
    }

    public static class Studyset implements BaseColumns{
        public static final String TABLE_NAME = "studyset";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_CREATED = "created";
        public static final String COLUMN_NAME_UPDATED = "updated";
        public static final String COLUMN_NAME_SUPPORTED_LANGUAGE = "suprtedLanguages";
    }
}
