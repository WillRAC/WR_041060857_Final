package uk.co.willrich.FinalProjectWR.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.co.willrich.FinalProjectWR.NewsArticle;

public class ArticleDBManager {

    private static final String sLOG_TAG = ArticleDBManager.class.getName();

    private DBHelper dbHelper;

    private Context coxContext;

    private SQLiteDatabase sqliteDatabase;


    public ArticleDBManager(Context context) {
        this.coxContext = context;
    }


    /**
     * This method opens a connection to the SQLite database, but retrieving a writeable version of the database.
     * An SQLException may be thrown, but it is catched in this method.
     */
    public void openDatabaseConnection() {

        Log.e(sLOG_TAG, "openDatabaseConnection() CALLED!");

        try {

            dbHelper = new DBHelper(coxContext);
            sqliteDatabase = dbHelper.getWritableDatabase();

            Log.e(sLOG_TAG, "openDatabaseConnection() - Assuming SQLite Database connection acquired!");

        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
            Log.e(sLOG_TAG, "openDatabaseConnection() - sqlex: " + sqlex);
        }

    }


    /**
     * This method closes the current connection to the SQLite database.
     * It first checks if there's any connection.
     */
    public void closeDatabaseConnection() {

        Log.e(sLOG_TAG, "closeDatabaseConnection() CALLED!");

        if (sqliteDatabase.isOpen()) {
            sqliteDatabase.close();
        }

        Log.e(sLOG_TAG, "closeDatabaseConnection() - Assuming SQLite Database connection closed!");

    }


    /**
     * This method inserts news article data into the Favourite Article table.
     *
     * @param articleTitle (String) The title of the article.
     * @param articleUrl (String) The URL of the article.
     * @param articleSection (String) The section of the article.
     */
    public void insertArticle(String articleTitle, String articleUrl, String articleSection) {

        Log.e(sLOG_TAG, "insertArticle() - CALLED!");

        ContentValues cvContentValues = new ContentValues();
        cvContentValues.put(DBHelper.sCOLUMN_ARTICLE_TITLE, articleTitle);
        cvContentValues.put(DBHelper.sCOLUMN_ARTICLE_URL, articleUrl);
        cvContentValues.put(DBHelper.sCOLUMN_ARTICLE_SECTION, articleSection);

        if (sqliteDatabase != null) {

            Log.e(sLOG_TAG, "insertArticle() - SQLite Database is NOT NULL, so go ahead and write values!");

            sqliteDatabase.insert(DBHelper.sTABLE_NAME, null, cvContentValues);
        }

        Log.e(sLOG_TAG, "insertArticle() - Assuming values written to SQLite Database!");

    }


    /**
     * This method fetches favourite news articles from the Favourite Article table.
     * It uses a cursor to iterate over the retrieved data.
     *
     * @return articlesList (List<NewsArticle>) The retrieved article list.
     */
    public List<NewsArticle> fetchAllArticles() {

        Log.e(sLOG_TAG, "fetchAllArticles() - CALLED!");

        List<NewsArticle> articlesList = null;

        String[] columnsProjection = new String[] {DBHelper.sCOLUMN_ARTICLE_TITLE, DBHelper.sCOLUMN_ARTICLE_URL, DBHelper.sCOLUMN_ARTICLE_SECTION};
        if (sqliteDatabase != null) {

            Log.e(sLOG_TAG, "fetchAllArticles() - SQLite Database is NOT NULL, so go ahead and read values!");

            Cursor articlesCursor = sqliteDatabase.query(DBHelper.sTABLE_NAME, columnsProjection, null, null, null, null, null);
            if ((articlesCursor != null) && (articlesCursor.getCount() > 0)) {
                articlesCursor.moveToFirst();

                articlesList = new ArrayList<>();

                String sFirstArticleTitle = articlesCursor.getString(0);
                String sFirstArticleUrl = articlesCursor.getString(1);
                String sFirstArticleSection = articlesCursor.getString(2);
                boolean firstArticleIsFavourite = true;

                Log.e(sLOG_TAG, "fetchAllArticles() - sFirstArticleTitle: " + sFirstArticleTitle + "        sFirstArticleUrl: " + sFirstArticleUrl + "          sFirstArticleSection: " + sFirstArticleSection);

                NewsArticle firstArticle = new NewsArticle();
                firstArticle.setArticleTitle(sFirstArticleTitle);
                firstArticle.setArticleUrl(sFirstArticleUrl);
                firstArticle.setArticleSection(sFirstArticleSection);
                firstArticle.setArticleIsFavourite(firstArticleIsFavourite);

                articlesList.add(firstArticle);

                while (articlesCursor.moveToNext()) {
                    articlesCursor.moveToNext();

                    String sArticleTitle = articlesCursor.getString(0);
                    String sArticleUrl = articlesCursor.getString(1);
                    String sArticleSection = articlesCursor.getString(2);
                    boolean articleIsFavourite = true;

                    Log.e(sLOG_TAG, "fetchAllArticles() - sArticleTitle: " + sArticleTitle + "        sArticleUrl: " + sArticleUrl + "          sArticleSection: " + sArticleSection);

                    NewsArticle nextArticle = new NewsArticle();
                    nextArticle.setArticleTitle(sArticleTitle);
                    nextArticle.setArticleUrl(sArticleUrl);
                    nextArticle.setArticleSection(sArticleSection);
                    nextArticle.setArticleIsFavourite(articleIsFavourite);

                    articlesList.add(nextArticle);

                }

                articlesCursor.close();
            }

        }

        Log.e(sLOG_TAG, "fetchAllArticles() - Assuming values read from SQLite Database! articlesList: " + articlesList);

        return articlesList;
    }

}
