package com.prizm.deep.mobilewiki;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import static java.net.URLEncoder.encode;

public class Wikimain extends AppCompatActivity implements View.OnClickListener {
    EditText phrase;
    Button search;
    TextView result;
    public String url="";
    public String jsonstring="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wikimain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        phrase=(EditText)findViewById(R.id.phrase);
        search=(Button)findViewById(R.id.submit);
        result=(TextView)findViewById(R.id.result);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url=phrase.getText().toString();
                try {
                    url="https://en.wikipedia.org/w/api.php?action=query&titles="+ encode(url, "utf-8")+"&prop=revisions&rvprop=content&imlimit=1&format=json";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                new JSONParse().execute();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wikimain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject>   {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Wikimain.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
           // JSONObject json = jParser.getJSONFromUrl("https://en.wikipedia.org/w/api.php?action=query&titles=San_Francisco&prop=revisions&rvprop=content&imlimit=1&format=json");
            JSONObject json = jParser.getJSONFromUrl(url);
            jsonstring=jParser.getJsonString();
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                //String js=json.toString();
                //result.setText(json.toString());
                /*
                String st="";
                JSONArray jo=json.getJSONArray("revisions");
                for (int i=0;i<jo.length();i++)
                {
                    JSONObject job=jo.getJSONObject(i);
                    st=job.optString("contentmodel");

                }*/
                JSONObject query=json.getJSONObject("query");
                JSONObject pages=query.getJSONObject("pages");
                Iterator<String> iter = pages.keys();
                String key=iter.next();
                JSONObject pid= (JSONObject) pages.get(key);
                String st="";
                st=st+"Page id : "+pid.getString("pageid")+'\n';
                st=st+"Title : "+pid.getString("title")+'\n';
                JSONArray jarray=pid.getJSONArray("revisions");
                JSONObject job=jarray.getJSONObject(0);
                st=st+"contentformat : "+job.optString("contentformat").toString()+'\n';
                st=st+"contentmodel : "+job.optString("contentmodel").toString()+'\n';
                st=st+"content : "+job.optString("*").toString();
                result.setText(st);
                /*

                while (iter.hasNext()) {
                    String key = iter.next();
                    try {
                        Object value = json.get(key);
                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }*/




                // Getting JSON Array



                //Set JSON Data in TextView


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
