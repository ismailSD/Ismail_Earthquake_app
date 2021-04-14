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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**Created by ismail adam on 25/03/2021
 * Student ID: S1908016 */

/**MainActivity class, the starting point of the application*/
public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private static final String FEED_DATA = "feedData";
    private static ArrayList<Item> savedItems;
    // Create a reference to the listView widget from the activity main
    private ListView xmlListView;

    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private ItemAdapter feedAdapter; // aggregation
    private static final int initialDelay = 0;// seconds
    private static final int schedulePeriod = 15;// seconds
    private map_fragment map_fragment;// aggregation
    public volatile boolean IS_scheduleTaskExecutor_RUNNING = false;
    private ScheduledFuture<?> future;
    private final ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(3);
    private volatile long REMAINING_DELAY = 0;
    /** onCreate method:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * As the activity is launched for the first time, the onCreate() method is invoked.
     * Since this is the first time the activity is launched, there is no saved state,
     * so the bundle is null and nothing has to be restored.
     *
     * This method allows for the creation of the user interface as well as initializing
     * of the data elements
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate: in::::::::::::::::::::::::::::::::::::");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xmlListView = (ListView) findViewById(R.id.xmlListView);
        Log.e(TAG, "onCreate: out:::::::::::::::::::::::::::::::::::");
    }

    /**onRestoreInstanceState method::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * The onRestoreInstanceState(Bundle) method is not often invoked.
     * In fact, it is called only when Bundle is not null.
     * As a result, the first moment the app is launched,
     * this method would not be called so there is nothing to restore “bundle is null,”
     * so Android skips it.*/
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState: in");
        super.onRestoreInstanceState(savedInstanceState);
        savedItems = (ArrayList<Item>) savedInstanceState.getSerializable(FEED_DATA);
        feedAdapter.setItems(savedItems);
        map_fragment.setItems(savedItems);
        Log.d(TAG, "onRestoreInstanceState: out");
    }

    /** onStart and onResume methods::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * After the onCreate() method has been called, the following methods would be called:
     * onStart() and onResume().
     * The activity is visible after onStart() is called, but it might not be in a condition
     * where the user could interact with it.
     * The activity is in the foreground and running after onResume() has been called which means
     * it is in front of all other activities, and the user will happily interact with it.*/
    @Override
    protected void onStart() {
        Log.e(TAG, "onStart: in::::::::::::::::::::");
        if(feedAdapter == null){
            feedAdapter = new ItemAdapter(MainActivity.this, R.layout.list_record);
        }else {
            Log.e(TAG, "onStart: feedAdapter already initialized");
        }
        if (map_fragment == null){
            map_fragment = new map_fragment();
        }else {
            Log.e(TAG, "onStart: map_fragment already initialized");
        }
        if(!IS_scheduleTaskExecutor_RUNNING){
            startService();
        }else {
            Log.e(TAG, "onStart: schedule service is already running");
        }
        super.onStart();
        Log.e(TAG, "onStart: out::::::::::::::::::::");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: in::::::::::::::::::::::::::");
        super.onResume();
        Log.e(TAG, "onResume: out:::::::::::::::::::::::::");
    }

    @Override
    protected void onRestart() {
        Log.e(TAG, "onRestart: in::::::::::::::::::::::::");
        super.onRestart();
        Log.e(TAG, "onRestart: out:::::::::::::::::::::::");
    }


    /**onPause and onSaveInstanceSate methods:::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * When the activity is running, another activity may be brought to the foreground,
     * or the phone may sleep. If this occurs, the current activity is paused,
     * and the onPause() method is invoked before the current activity loses control.
     * The activity is still in a happy sate, and there is no need to save and restore state –
     * it is just no longer in the foreground.
     * So, if a dialog is displayed on top of the activity, the activity will then be paused,
     * and onPause() will be invoked. When the user brings the activity to the foreground,
     * for example, by dismissing a dialogue, onResume() is invoked. Since there is no saved state,
     * onSaeInstanceState() is not called, and the Bundle passed to onCreate() is null.
     * If, on the other hand, the activity is destroyed due to a configuration change or
     * that Android requires its resources, Android "remembers" that it existed and was
     * terminated by the system. So, if the configuration changes, the same thing occurs,
     * but the activity is automatically restarted which means that the user doesn't need to
     * lunch it again.
     * */
    @Override
    protected void onPause() {
        Log.e(TAG, "onPause: in:::::::::::::::::::::::::");
        super.onPause();
        Log.e(TAG, "onPause: out::::::::::::::::::::::::");
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: in");
        // bundle contains the list of key value pairs
        // saving the current value into the bundle
         outState.putSerializable(FEED_DATA, (Serializable) feedAdapter.getItems());
        // super method will take care of saving process
        Log.e(TAG, "onSaveInstanceState: Service cancelled::::::::::::::");
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: out");
    }

    /** onDestroy() and onStop() method:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * It should be noted that onDestroy() is not always called.
     * onSaveInstanceState() and onRestoreInstance() are not always called either;
     * they are called only when the system detects a need for them. As a consequence,
     * Google Docs advises saving user data in onPause() rather than onSaveInstanceState().
     */
    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: in");
        scheduleTaskExecutor.shutdown();// shout down the scheduler
        super.onDestroy();
        Log.d(TAG, "onDestroy: out");
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop: in");
        try {
            System.out.println("time remaining:::::::::::::::::::::::::::::::::::::::::");
            REMAINING_DELAY = future.getDelay(TimeUnit.SECONDS);
            System.out.println(REMAINING_DELAY);
            System.out.println("time remaining:::::::::::::::::::::::::::::::::::::::::");
        }catch (Exception e){
            e.printStackTrace();
        }
        future.cancel(true);// cancel the refresh schedule
        IS_scheduleTaskExecutor_RUNNING = false;
        super.onStop();
        Log.e(TAG, "onStop: out");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
            Log.d("On Config Change:","LANDSCAPE");
        }else{

            Log.d("On Config Change:","PORTRAIT");
            System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        }
    }

    private void startService(){
        IS_scheduleTaskExecutor_RUNNING = true;
        future = scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "in startService: starting AsyncTask");
                         new DownloadData().execute(urlSource);
                        Log.d(TAG, "in startService: done");
                    }
                });
            }
        }, REMAINING_DELAY > 0 ? REMAINING_DELAY : initialDelay, schedulePeriod, TimeUnit.SECONDS);
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

    public class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";
        ProgressDialog progressDialog;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: in onPostExecute");
            progressDialog.dismiss();

            ParseXMLData parseXMLData = new ParseXMLData();
            parseXMLData.parseXML(s);// s is the xml the android framework has sent at this point

            /******/
            feedAdapter.setItems(parseXMLData.getApplications());
            map_fragment.setItems(feedAdapter.getItems());
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
            // Everything in here will run asynchronously
            Log.d(TAG, "doInBackground: starts with: "+ strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if(rssFeed==null){
                Log.e(TAG, "doInBackground: Error downloading");
            }

            return rssFeed;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
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