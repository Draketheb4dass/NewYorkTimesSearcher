package com.realty.drake.newyorktimessearcher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.realty.drake.newyorktimessearcher.adapters.ArticleArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    EditText etQuery;
    GridView gvResults;
    Button btnSearch;
    String NYT_API_KEY = "c984070a9b894daf976427235eb46ea5";
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();





    }

    public void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);
    }

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
        //Toast.makeText(this, "Searching for " + query,
        //        Toast.LENGTH_SHORT).show();
        AsyncHttpClient client = new AsyncHttpClient();
        String SEARCH_API_URL =
                "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
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
}
