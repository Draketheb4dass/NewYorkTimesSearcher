package com.realty.drake.newyorktimessearcher.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.realty.drake.newyorktimessearcher.Article;
import com.realty.drake.newyorktimessearcher.R;
import com.realty.drake.newyorktimessearcher.adapters.ArticleArrayAdapter;
import com.realty.drake.newyorktimessearcher.fragments.FilterDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener, FilterDialogFragment.FilterDialogListener {
    EditText etQuery;
    GridView gvResults;
    static final String NYT_API_KEY = "c984070a9b894daf976427235eb46ea5";
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    static final String SEARCH_API_URL =
            "https://api.nytimes.com/svc/search/v2/articlesearch.json";
    static String query;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    Toolbar myToolbar;
    SearchView searchView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //Set custom AppBar
        myToolbar = findViewById(R.id.search_toolbar);
        //myToolbar.inflateMenu(R.menu.menu_search);
        initToolBarMenuIcons();
        setSupportActionBar(myToolbar);

        myToolbar.setOnMenuItemClickListener(this);
        setupViews();

        //Check internet connection
        new InternetCheck(internet -> {
            //Display no internet Toast if there's no internet
            if (!internet) {
                Toast.makeText(this, "no internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @MainThread
    @SuppressLint("NewApi") //suppress warning, since using DrawableCompat to set tint
    public void initToolBarMenuIcons() {
        myToolbar.inflateMenu(R.menu.menu_search);
        //Manually adding icon since it's a vector drawable and we can't currently inflate from XML into menuitems
        Drawable wrappedFilterIcon = DrawableCompat
                .wrap(Objects.requireNonNull(AppCompatResources
                        .getDrawable(getBaseContext(), R.drawable.ic_filter_list_white_24dp)));
        Drawable wrappedSearchIcon = DrawableCompat
                .wrap(Objects.requireNonNull(AppCompatResources
                        .getDrawable(getBaseContext(), R.drawable.ic_search_white_24dp)));
        //Tint it too
        DrawableCompat.setTint(wrappedFilterIcon, Color.WHITE);
        DrawableCompat.setTint(wrappedSearchIcon, Color.WHITE);
        //Set the tinted vector drawable to the item
        myToolbar.getMenu().findItem(R.id.action_filter).setIcon(wrappedFilterIcon);
        myToolbar.getMenu().findItem(R.id.action_search).setIcon(wrappedSearchIcon);
    }


    public void setupViews() {
        etQuery = findViewById(R.id.etQuery);
        gvResults = findViewById(R.id.gvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        //Hook up listener for grid click
        gvResults.setOnItemClickListener((parent, view, position, id) -> {
            //Intent to display article
            Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
            //get the article to display
            Article article = articles.get(position);
            //pass that article to intent
            i.putExtra("article", Parcels.wrap(article));
            //launch activity
            startActivity(i);
        });
    }

    @Override
    public void onFinishFilterDialog(HashMap<String, String> filter) {
        onArticleSearch(filter);
    }

    //TODO try to add hashmap as a param and listenr would call onArticle with that param
    //make API request with @params
    public void onArticleSearch(HashMap<String, String> filter) {
        //Check internet connection
        new InternetCheck(internet -> {
            //Display no internet Toast if there's no internet
            if (!internet) {
                Toast.makeText(this, "no internet", Toast.LENGTH_SHORT).show();
            }
        });
        query = searchView.getQuery().toString();
        //Toast.makeText(this, "Searching for " + query,
        //        Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams(filter);
        params.put("api-key", NYT_API_KEY);
        params.put("page", 0);
        params.put("q", query);

        client.get(SEARCH_API_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;
                //Clear the view for every search
                adapter.clear();

                try {
                    articleJsonResults = response
                            .getJSONObject("response")
                            .getJSONArray("docs");
                    adapter.addAll(Article.fromJsonArray(articleJsonResults));
                    if (articleJsonResults.length() == 0) {
                        //TODO Handling no article
                        Toast.makeText(SearchActivity.this,
                                "no article", Toast.LENGTH_SHORT).show();
                    }
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            //Filter Intent
            case R.id.action_filter:
                //Show FilterDialogFragment
                FragmentManager fm = getSupportFragmentManager();
                FilterDialogFragment filterDialogFragment =
                        FilterDialogFragment.newInstance("Filter Search by");
                filterDialogFragment.show(fm, "fragment_filter_search");
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onArticleSearch(null);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}