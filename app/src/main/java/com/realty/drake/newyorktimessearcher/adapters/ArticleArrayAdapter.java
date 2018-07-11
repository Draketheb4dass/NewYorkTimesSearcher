package com.realty.drake.newyorktimessearcher.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.realty.drake.newyorktimessearcher.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
    }
}
