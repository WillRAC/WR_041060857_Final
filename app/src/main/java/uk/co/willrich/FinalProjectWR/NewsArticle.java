package uk.co.willrich.FinalProjectWR;

import java.io.Serializable;

public class NewsArticle implements Serializable {

    private String articleTitle;

    private  String articleSection;

    private String articleUrl;

    private boolean articleIsFavourite;


    public NewsArticle() {}

    public String getArticleTitle() { return articleTitle; }
    public String setArticleTitle(String articleTitle) {this.articleTitle = articleTitle;
        return articleTitle;
    }

    public String getArticleSection() { return articleSection; }
    public String setArticleSection(String articleSection) {this.articleSection = articleSection;
        return articleSection;
    }

    public String getArticleUrl() { return articleUrl; }
    public String setArticleUrl(String articleUrl) {this.articleUrl = articleUrl;
        return articleUrl;
    }

    public boolean isArticleIsFavourite() {
        return articleIsFavourite;
    }

    public void setArticleIsFavourite(boolean articleIsFavourite) {
        this.articleIsFavourite = articleIsFavourite;
    }
}
