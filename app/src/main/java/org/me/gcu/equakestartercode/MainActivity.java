package org.me.gcu.equakestartercode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private static final String FEED_DATA = "items";
    // create a reference to the listView widget from the activity main
    private ListView xmlListView;

    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private ItemAdapter feedAdapter;
    private static final int initialDelay = 0;
    private static final int schedulePeriod = 15;// seconds
    private final ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(3);
    private Future<?> future;
    map_fragment map_fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xmlListView = (ListView) findViewById(R.id.xmlListView);
        startService();
    }

    private void startService(){
        future = scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            Log.d(TAG, "in startService: starting AsyncTask");
                            new DownloadData().execute(urlSource);
                            Log.d(TAG, "in startService: done");
                        }
                    });
                }
            }, initialDelay,schedulePeriod,TimeUnit.SECONDS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;// to tell android that we have inflated the menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.searchMenu){
            Intent searchIntent = new Intent(getApplicationContext(), SearchData.class);
            searchIntent.putExtra("items", (Serializable) feedAdapter.getItems());
            startActivity(searchIntent);

        }else if(id == R.id.exitAppMenu){
            // exit the application
            MainActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: in");
        startService();// start background refresh
        super.onRestart();
        Log.d(TAG, "onRestart: out");
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: in");
        super.onRestoreInstanceState(savedInstanceState);
        // super method will retrieve the data from the saved instance then we can access it.
        //this.items = (ArrayList<Item>) savedInstanceState.getSerializable(FEED_DATA);
        Log.d(TAG, "onRestoreInstanceState: out");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: in");
        super.onResume();
        Log.d(TAG, "onResume: out");
    }

    @Override
    protected void onPause() {
        //onPause is called just before the activity moves to background and also before onSaveInstanceState.
        Log.d(TAG, "onPause: in");
        super.onPause();
        future.cancel(true);// cancel the refresh schedule
        Log.d(TAG, "onPause: out");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: in");
        // bundle contains the list of key value pairs
        // saving the current value into the bundle
        //outState.putSerializable(FEED_DATA, (Serializable) feedAdapter.getItems());
        // super method will take care of saving process
        future.cancel(true);// cancel the refresh schedule
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: out");
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: in");
        future.cancel(true);// cancel the refresh schedule
        super.onStop();
        Log.d(TAG, "onStop: out");
    }
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: in");
        scheduleTaskExecutor.shutdown();// shout down the scheduler
        super.onDestroy();
        Log.d(TAG, "onDestroy: out");
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }



    public class DownloadData extends AsyncTask<String, Void, String> {
        //1 (String) the type of information will be a string (pass URL to the RSS feed)
        //2 (Void) normally used if you want to display a progress bar
        //  (we used Void as we don't need progress bar as our information is quiet small)
        //3 (String) which contains all the XML data.

        private static final String TAG = "DownloadData";
        ProgressDialog progressDialog;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: in onPostExecute");
            progressDialog.dismiss();

            ParseXMLData parseXMLData = new ParseXMLData();
            parseXMLData.parseXML(s);// s is the xml the android framework has sent at this point

            feedAdapter = new ItemAdapter(MainActivity.this, R.layout.list_record, parseXMLData.getApplications());
            ///::::::::::::::
            map_fragment = new map_fragment(feedAdapter.getItems());
            // Open fragment
            try{
                getSupportFragmentManager().beginTransaction().replace(R.id.map_frame, map_fragment).commit();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }

            xmlListView.setAdapter(feedAdapter);
            feedAdapter.notifyDataSetChanged();
            xmlListView.invalidateViews();
            xmlListView.scrollBy(0, 0);

            //::::::::::: listen on item click::::::::::::::::::::::::::::
            xmlListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Item selectedItem = feedAdapter.getItems().get(position);
                    Intent moreInfoIntent = new Intent(getApplicationContext(), MoreInfo.class);
                    moreInfoIntent.putExtra("item", selectedItem);
                    startActivity(moreInfoIntent);
                }
            });

        }

        @Override
        protected String doInBackground(String... strings) {
            // everything in here will run asynchronously
            Log.d(TAG, "doInBackground: starts with: "+ strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if(rssFeed==null){
                Log.e(TAG, "doInBackground: Error downloading");
            }
            return rssFeed;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Fetch earth quake data");
            progressDialog.setMessage("Fetching....Please wait");
            progressDialog.show();
        }

        private String downloadXML(String urlPath){
            StringBuilder xmlResult = new StringBuilder();
            try{
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: the response code was: "+response);

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                int charsRead;
                char[] inputBuffer = new char[500];
                while (true){
                    charsRead = reader.read(inputBuffer);
                    if(charsRead <0) break;
                    if(charsRead>0){
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();
                return xmlResult.toString();
            }catch (MalformedURLException e){
                Log.e(TAG, "downloadXML: Invalid URL: "+e.getMessage());
            }catch (IOException e){
                Log.e(TAG, "downloadXML: IO exception reading data: "+ e.getMessage());
            }catch (SecurityException e){
                Log.e(TAG, "downloadXML: Security exception! need permission? "+e.getMessage());
            }
            return null;
        }
    }
}