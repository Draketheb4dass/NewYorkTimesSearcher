package com.realty.drake.newyorktimessearcher.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.realty.drake.newyorktimessearcher.Article;
import com.realty.drake.newyorktimessearcher.R;

public class ArticleActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        //Set custom AppBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.article_toolbar);
        myToolbar.inflateMenu(R.menu.menu_detail);
        setSupportActionBar(myToolbar);
        myToolbar.setOnMenuItemClickListener(this);

        Article article = getIntent().getParcelableExtra("article");
        WebView webView =(WebView) findViewById(R.id.wvArticle);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
             view.loadUrl(url);
             return true;
            }
        });
        webView.loadUrl(article.getWebUrl());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_share:
                Toast.makeText(this, "Shared", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }
}
