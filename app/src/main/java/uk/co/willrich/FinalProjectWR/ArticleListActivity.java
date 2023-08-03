package uk.co.willrich.FinalProjectWR;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.co.willrich.FinalProjectWR.DB.ArticleDBManager;


public class ArticleListActivity extends AppCompatActivity {

    private static final String NEWS_URL = "https://content.guardianapis.com/search";

    private static final String INTENT_EXTRA_NEWS_ARTICLE = "uk.co.willrich.FinalProjectWR.INTENT_EXTRA_NEWS_ARTICLE";

    private ProgressBar progressBar;

    private LinearLayout linearLayoutArticlesList;
    private DrawerLayout dlayDrawerLayout;

    private ListView listViewArticles;

    private List<NewsArticle> articles;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);

        progressBar = this.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        linearLayoutArticlesList = this.findViewById(R.id.linearLayoutArticlesList);

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setTitle(this.getResources().getString(R.string.activity_title_article_list));

        dlayDrawerLayout = this.findViewById(R.id.dlayDrawerLayoutArticleList);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(ArticleListActivity.this, dlayDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        dlayDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navNavigationDrawer = this.findViewById(R.id.navArticleListDrawer);
        navNavigationDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.mnuDrawerArticleList) {
                    // Do nothing; we are on current screen

                    dlayDrawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                } else if (item.getItemId() == R.id.mnuDrawerAboutMe) {

                    dlayDrawerLayout.closeDrawer(GravityCompat.START);

                    Intent inLaunchAboutMe = new Intent(ArticleListActivity.this, AboutMeActivity.class);
                    startActivity(inLaunchAboutMe);

                    return true;
                } else if (item.getItemId() == R.id.mnuDrawerLogout) {

                    dlayDrawerLayout.closeDrawer(GravityCompat.START);

                    // Finish the current activity
                    finish();

                    return true;
                }

                return false;
            }
        });

        listViewArticles = findViewById(R.id.articleListView);

        EditText editTextSearch = this.findViewById(R.id.EditTextSearch);
        Button buttonSearch = this.findViewById(R.id.SearchButton);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(editTextSearch.getText())){
                    Snackbar.make(linearLayoutArticlesList, "Provide Search Query", Snackbar.LENGTH_LONG).show();
                } else {
                    String searchQuery = editTextSearch.getText().toString();
                    NewsListTask taskNewsList = new NewsListTask();
                    taskNewsList.execute(searchQuery);

                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        codeToFetchFavouriteArticles();

    }

    /**
     * This method contacts the DB Manager class to fetch the user's favourite articles, i.e. articles saved on the DB as favourite.
     * Once it fetches this data, it calls a method to display the data in the ListView.
     */
    private void codeToFetchFavouriteArticles() {

        progressBar.setVisibility(View.VISIBLE);
        linearLayoutArticlesList.setVisibility(View.GONE);

        ArticleDBManager articleDBManager = new ArticleDBManager(ArticleListActivity.this);
        articleDBManager.openDatabaseConnection();
        List<NewsArticle> favouriteArticles = articleDBManager.fetchAllArticles();
        if (favouriteArticles != null) {
            loadListItemsOntoListView(favouriteArticles);
        } else {
            Snackbar.make(linearLayoutArticlesList, "You have no favourite articles.", Snackbar.LENGTH_LONG).show();
        }

        articleDBManager.closeDatabaseConnection();

        progressBar.setVisibility(View.GONE);
        linearLayoutArticlesList.setVisibility(View.VISIBLE);

    }

    /**
     * This method displays new articles on the ListView with the help of an Adapter.
     *
     * @param articles (List<NewsArticle>) List of news articles to be displayed.
     */
    private void loadListItemsOntoListView(List<NewsArticle> articles) {

        listViewArticles.setAdapter(new AdapterListNewsArticle(ArticleListActivity.this, android.R.layout.simple_list_item_1, articles));
        listViewArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                NewsArticle selectedArticle = articles.get(position);
                startActivityArticleDetail(selectedArticle);

            }

        });

    }

    /**
     * This method starts the Article Detail Activity, and passes to it the news articles that has been clicked on the ListView.
     *
     * @param selectedNewsArticle (NewsArticle) Data object containing data of the selected news article.
     */
    private void startActivityArticleDetail(NewsArticle selectedNewsArticle) {

        Intent inLaunchArticlesDetail = new Intent(ArticleListActivity.this, ArticleDetailActivity.class);
        inLaunchArticlesDetail.putExtra(INTENT_EXTRA_NEWS_ARTICLE, selectedNewsArticle);
        this.startActivity(inLaunchArticlesDetail);
    }

    /**
     * This method display an AlertDialog to show instructions of how to use this Activity.
     */
    private void showArticleListHelpAlertDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ArticleListActivity.this);
        dialogBuilder.setTitle("Help");
        dialogBuilder.setMessage("In this Activity you can view a list of your favourite articles and you can also search the internet for more articles.\n" +
                "When this screen first loads, it displays your favourite articles.\n" +
                "You can enter a search item in the search bar to query the internet for more articles.");
        dialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog adgHelpDialog = dialogBuilder.create();
        adgHelpDialog.show();

    }


    @Override
    public void onBackPressed() {

        // Before the back press is handled, check if drawer is opened.
        // If it is opened, then close it.
        // If it is not, then go ahead and close the activity
        if (dlayDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            dlayDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_article_list, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.mnuArticleListHelp) {
            showArticleListHelpAlertDialog();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * AsyncTask class to run the network operation of fetching news articles from the internet.
     */
    private class NewsListTask extends AsyncTask<String, Integer, List<NewsArticle>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.setVisibility(View.VISIBLE);
            linearLayoutArticlesList.setVisibility(View.GONE);
        }

        @Override
        protected List<NewsArticle> doInBackground(String... args) {

            String searchUrl = args[0];

            try {

                URL url = new URL(NEWS_URL + "?q=" + searchUrl + "&api-key=test");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                Log.e("FRAGMENTS", "url: " + url);


                // Read the response
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String result = sb.toString();

                Log.e("FRAGMENTS", "result: " + result);    //  TODO: For TESTING ONLY


                JSONObject jsonResponse = new JSONObject(result);
                Log.e("FRAGMENTS", "jsonResponse: " + jsonResponse);    //  TODO: For TESTING ONLY

                JSONObject jsonAPIResponse =  jsonResponse.getJSONObject("response");
                Log.e("FRAGMENTS", "jsonAPIResponse: " + jsonAPIResponse);    //  TODO: For TESTING ONLY

                JSONArray resultsArray = jsonAPIResponse.getJSONArray("results");
                Log.e("FRAGMENTS", "resultsArray: " + resultsArray);    //  TODO: For TESTING ONLY

                articles = new ArrayList<>();

                for (int i = 0; i < resultsArray.length(); i++) {

                    JSONObject articleObject = resultsArray.getJSONObject(i);
                    String articleTitle = articleObject.getString("webTitle");
                    String articleSection = articleObject.getString("sectionName");
                    String articleUrl = articleObject.getString("webUrl");

                    NewsArticle newsArticle = new NewsArticle();
                    newsArticle.setArticleTitle(articleTitle);
                    newsArticle.setArticleSection(articleSection);
                    newsArticle.setArticleUrl(articleUrl);

                    articles.add(newsArticle);

                    Log.e("FRAGMENTS", "articles: " + articles.size());    //  TODO: For TESTING ONLY

                }


            } catch (MalformedURLException muex) {
                muex.printStackTrace();
            } catch (JSONException jsex) {
                jsex.printStackTrace();
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }

            return articles;
        }


        @Override
        public void onProgressUpdate(Integer... args) {
        }


        @Override
        protected void onPostExecute(List<NewsArticle> articles) {
            super.onPostExecute(articles);

            progressBar.setVisibility(View.GONE);
            linearLayoutArticlesList.setVisibility(View.VISIBLE);

            loadListItemsOntoListView(articles);

        }

    }
}

