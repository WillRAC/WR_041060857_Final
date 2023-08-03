package uk.co.willrich.FinalProjectWR;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import uk.co.willrich.FinalProjectWR.DB.ArticleDBManager;

public class ArticleDetailActivity extends AppCompatActivity {

    private static final String INTENT_EXTRA_NEWS_ARTICLE = "uk.co.willrich.FinalProjectWR.INTENT_EXTRA_NEWS_ARTICLE";
    private NewsArticle newsArticle;
    private ArticleDBManager articleDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Toolbar toolbar = this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        toolbar.setTitle(this.getResources().getString(R.string.activity_title_article_detail));

        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView textViewURL = findViewById(R.id.textViewUrl);
        TextView textViewSection = findViewById(R.id.textViewSection);

        Button btnFavourite = findViewById(R.id.btnFavouriteArticle);

        Intent inLaunchIntent = this.getIntent();
        newsArticle = (NewsArticle) inLaunchIntent.getSerializableExtra(INTENT_EXTRA_NEWS_ARTICLE);
        if (newsArticle != null) {

            if (newsArticle.getArticleTitle() !=null && !newsArticle.getArticleTitle().equalsIgnoreCase("") ){
                textViewTitle.setText(newsArticle.getArticleTitle());
            }
            if (newsArticle.getArticleUrl() !=null && !newsArticle.getArticleUrl().equalsIgnoreCase("") ){
                textViewURL.setText(newsArticle.getArticleUrl());
            }
            if (newsArticle.getArticleSection() !=null && !newsArticle.getArticleSection().equalsIgnoreCase("") ){
                textViewSection.setText(newsArticle.getArticleSection());
            }

            if (newsArticle.isArticleIsFavourite()) {
                btnFavourite.setVisibility(View.GONE);
            }

        }

        btnFavourite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String articleTitle = newsArticle.getArticleTitle();
                String articleUrl = newsArticle.getArticleUrl();
                String articleSection = newsArticle.getArticleSection();

                articleDBManager = new ArticleDBManager(ArticleDetailActivity.this);
                articleDBManager.openDatabaseConnection();
                articleDBManager.insertArticle(articleTitle, articleUrl, articleSection);

            }

        });

    }


    /**
     * This method display an AlertDialog to show instructions of how to use this Activity.
     */
    private void showArticleDetailHelpAlertDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ArticleDetailActivity.this);
        dialogBuilder.setTitle("Help");
        dialogBuilder.setMessage("In this Activity you can view the details of your favourite article or an article that you have fetched from the internet.");
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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.menu_article_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.mnuArticleDetailHelp) {
            showArticleDetailHelpAlertDialog();
        }

        return super.onOptionsItemSelected(item);
    }
}