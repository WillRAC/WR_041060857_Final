package uk.co.willrich.FinalProjectWR;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AdapterListNewsArticle extends ArrayAdapter<NewsArticle> {

    private final Context coxContext;

    private List<NewsArticle> listNews;

    private TextView txtArticleName;


    public AdapterListNewsArticle(@NonNull Context context, int resource, @NonNull List<NewsArticle> objects) {
        super(context, resource, objects);

        this.coxContext = context;

        this.listNews = objects;

    }

    @Override
    public int getCount() {

        return listNews.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null) {

            convertView = LayoutInflater.from(coxContext).inflate(R.layout.rowlayout_list_article, parent, false);

            txtArticleName = convertView.findViewById(R.id.txtArticleName);

        }

        NewsArticle currentArticle = listNews.get(position);
        String sArticleTitle = currentArticle.getArticleTitle();
        txtArticleName.setText(sArticleTitle);

        return convertView;
    }

}
