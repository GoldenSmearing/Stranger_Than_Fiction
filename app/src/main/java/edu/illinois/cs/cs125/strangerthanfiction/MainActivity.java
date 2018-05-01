package edu.illinois.cs.cs125.strangerthanfiction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.*;

import edu.illinois.cs.cs125.userinterfacetest.R;

public class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "User Interface: Main";
    public boolean topTrue = true;
    public int numCorrect = 0;
    public int totalRounds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Set up the text boxes.
         */
        final TextView prompt = findViewById(R.id.prompt);
        final TextView topHeadline = findViewById(R.id.topHeadline);
        final TextView bottomHeadline = findViewById(R.id.bottomHeadline);
        topHeadline.setText("''Big Don talks with Kim Jong''");
        topHeadline.setTextSize(20);
        bottomHeadline.setText("''Handshake between Donald and Kim goes well''");
        bottomHeadline.setTextSize(20);

        /*
         * Set up handlers for each button in our UI. These run when the buttons are clicked.
         */
        final Button topButton = findViewById(R.id.topButton);
        final Button bottomButton = findViewById(R.id.bottomButton);
        final Button next = findViewById(R.id.next);

        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Top button clicked");
                System.out.println("Top bottom clicked");
                if (topTrue) {
                    numCorrect++;
                    bottomHeadline.setVisibility(View.INVISIBLE);
                    bottomButton.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                } else {
                    topHeadline.setVisibility(View.INVISIBLE);
                    topButton.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
            }
        });
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Bottom button clicked");
                System.out.println("Bottom button clicked");
                if (!topTrue) {
                    numCorrect++;
                    topHeadline.setVisibility(View.INVISIBLE);
                    topButton.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                } else {
                    bottomHeadline.setVisibility(View.INVISIBLE);
                    bottomButton.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                }
            }
        });

        next.setVisibility(View.INVISIBLE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Next button clicked");
                System.out.println("Next button clicked");
                newHeadlines();
            }
        });
    }
    public void newHeadlines() {
        final Button next = findViewById(R.id.next);
        next.setVisibility(View.INVISIBLE);
        final TextView topHeadline = findViewById(R.id.topHeadline);
        final TextView bottomHeadline = findViewById(R.id.bottomHeadline);
        final Button topButton = findViewById(R.id.topButton);
        final Button bottomButton = findViewById(R.id.bottomButton);
        final TextView prompt = findViewById(R.id.prompt);
        topHeadline.setVisibility(View.VISIBLE);
        bottomHeadline.setVisibility(View.VISIBLE);
        topButton.setVisibility(View.VISIBLE);
        bottomButton.setVisibility(View.VISIBLE);
        if (totalRounds > 9) {
            String message = "Congrats! You got " + numCorrect + " out of 10!";
            topHeadline.setText(message);
            bottomHeadline.setVisibility(View.INVISIBLE);
            topButton.setVisibility(View.INVISIBLE);
            bottomButton.setVisibility(View.INVISIBLE);
            prompt.setVisibility(View.INVISIBLE);
            next.setVisibility(View.VISIBLE);
            totalRounds = 0;
            numCorrect = 0;
            return;
        }
        totalRounds++;
        int choice = (int) (Math.random() * 2);
        if (choice == 0) {
            topTrue = true;
            String url = "https://www.reddit.com/r/nottheonion/random.json?limit=1";
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JsonParser parser = new JsonParser();
                    JsonArray result = parser.parse(response.toString()).getAsJsonArray();
                    JsonObject article = result.get(0).getAsJsonObject();
                    JsonObject data = article.get("data").getAsJsonObject();
                    JsonArray children = data.get("children").getAsJsonArray();
                    JsonObject child = children.get(0).getAsJsonObject();
                    JsonObject data2 = child.get("data").getAsJsonObject();
                    String title = data2.get("title").getAsString();
                    topHeadline.setText(title);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("run", error.toString());
                }
            });
            queue.add(stringRequest1);
            url = "https://www.reddit.com/r/theonion/random.json?limit=1";
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JsonParser parser = new JsonParser();
                    JsonArray result = parser.parse(response.toString()).getAsJsonArray();
                    JsonObject article = result.get(0).getAsJsonObject();
                    JsonObject data = article.get("data").getAsJsonObject();
                    JsonArray children = data.get("children").getAsJsonArray();
                    JsonObject child = children.get(0).getAsJsonObject();
                    JsonObject data2 = child.get("data").getAsJsonObject();
                    String title = data2.get("title").getAsString();
                    bottomHeadline.setText(title);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("run", error.toString());
                }
            });
            queue.add(stringRequest2);
        } else {
            topTrue = false;
            String url = "https://www.reddit.com/r/theonion/random.json?limit=1";
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JsonParser parser = new JsonParser();
                    JsonArray result = parser.parse(response.toString()).getAsJsonArray();
                    JsonObject article = result.get(0).getAsJsonObject();
                    JsonObject data = article.get("data").getAsJsonObject();
                    JsonArray children = data.get("children").getAsJsonArray();
                    JsonObject child = children.get(0).getAsJsonObject();
                    JsonObject data2 = child.get("data").getAsJsonObject();
                    String title = data2.get("title").getAsString();
                    topHeadline.setText(title);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("run", error.toString());
                }
            });
            queue.add(stringRequest1);
            url = "https://www.reddit.com/r/nottheonion/random.json?limit=1";
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JsonParser parser = new JsonParser();
                    JsonArray result = parser.parse(response.toString()).getAsJsonArray();
                    JsonObject article = result.get(0).getAsJsonObject();
                    JsonObject data = article.get("data").getAsJsonObject();
                    JsonArray children = data.get("children").getAsJsonArray();
                    JsonObject child = children.get(0).getAsJsonObject();
                    JsonObject data2 = child.get("data").getAsJsonObject();
                    String title = data2.get("title").getAsString();
                    bottomHeadline.setText(title);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.v("run", error.toString());
                }
            });
            queue.add(stringRequest2);
        }
    }



}
