package com.realty.drake.newyorktimessearcher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

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
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    String NYT_API_KEY = "c984070a9b894daf976427235eb46ea5";
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    HashMap<String, String> filter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //Set custom AppBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.search_toolbar);
        myToolbar.inflateMenu(R.menu.menu_search);
        setSupportActionBar(myToolbar);
        myToolbar.setOnMenuItemClickListener(this);
        setupViews();

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onArticleSearch(v, filter);
            }
        });
    }

    public void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        //Hook up listener for grid click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent to display article
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                //get the article to display
                Article article = articles.get(position);
                //pass that article to intent
                i.putExtra("article", Parcels.wrap(article));
                //launch activity
                startActivity(i);
            }
        });
    }
    //TODO try to add hashmap as a param and listenr would call onArticle with that param
    //make API request with @params
    public void onArticleSearch(View view, HashMap<String, String> filter) {
        String query = etQuery.getText().toString();
        //Toast.makeText(this, "Searching for " + query,
        //        Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String SEARCH_API_URL =
                "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams(filter);
        params.put("api-key", NYT_API_KEY);
        params.put("page", 0);
        params.put("q", query);

        client.get(SEARCH_API_URL, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response
                            .getJSONObject("response")
                            .getJSONArray("docs");
                    adapter.addAll(Article.fromJsonArray(articleJsonResults));
                    Log.d("DEBUG", articles.toString());
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public boolean onMenuItemClick(MenuItem item){
        switch (item.getItemId()) {
            //Filter Intent
            case R.id.action_filter:
                FragmentManager fm = getSupportFragmentManager();
                FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance("Search Filter");
                filterDialogFragment.show(fm, "fragment_filter_search");
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }
}
