package uk.co.willrich.FinalProjectWR.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    // The database name
    public static final String sDB_NAME = "uk.co.willrich.FinalProjectWR.DATABASE";
    // The Favourite Article table name
    public static final String sTABLE_NAME = "favourite_article";

    // Column name for Id column
    public static final String sCOLUMN_ARTICLE_ID = "id";
    // Column name for Article Title column
    public static final String sCOLUMN_ARTICLE_TITLE = "article_title";
    // Column name for Article URL column
    public static final String sCOLUMN_ARTICLE_URL = "article_url";
    // Column name for Article Section column
    public static final String sCOLUMN_ARTICLE_SECTION = "article_section";

    // The current Database version
    public static final int iDB_VERSION = 1;

    // SQL Query to create the Favourite Article table.
    private static final String sSQL_CREATE_TABLE_FAVOURITE_ARTICLE = "CREATE TABLE " + sTABLE_NAME +
            " (" + sCOLUMN_ARTICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            sCOLUMN_ARTICLE_TITLE + " TEXT NOT NULL, " +
            sCOLUMN_ARTICLE_URL + " TEXT NOT NULL, " +
            sCOLUMN_ARTICLE_SECTION + " TEXT NOT NULL);";

    // SQL Query to drop the Favourite Article table.
    private static final String sSQL_DROP_TABLE_FAVOURITE_ARTICLE = "DROP TABLE IF EXISTS " + sTABLE_NAME;


    public DBHelper(@Nullable Context context) {
        super(context, sDB_NAME, null, iDB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sSQL_CREATE_TABLE_FAVOURITE_ARTICLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(sSQL_DROP_TABLE_FAVOURITE_ARTICLE);
        onCreate(sqLiteDatabase);
    }
}
