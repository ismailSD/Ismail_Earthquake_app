package org.me.gcu.equakestartercode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Layout;
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
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";

    // create a reference to the listView widget from the activity main
    private ListView xmlListView;
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    private ItemAdapter feedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("MyTag","in onCreate");
        xmlListView = (ListView) findViewById(R.id.xmlListView);
        downloadURL(urlSource);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;// to tell android that we have inflated the menu
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.searchMenu:
                Intent searchIntent = new Intent(getApplicationContext(), SearchData.class);
                searchIntent.putExtra("items", (Serializable) feedAdapter.getItems());
                startActivity(searchIntent);
                break;
            case R.id.exitAppMenu: MainActivity.this.finish();
            break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadURL(String feedUrl){
        Log.d(TAG, "downloadURL: starting AsyncTask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute(feedUrl);
        Log.d(TAG, "downloadURL: done");
    }

    // class that extends async task class
    //1 (String) the type of information will be a string (pass URL to the RSS feed)
    //2 (Void) normally used if you want to display a progress bar
    //  (we used Void as we don't need progress bar as our information is quiet small)
    //3 (String) which contains all the XML data.
    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";
        ProgressDialog pd;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d(TAG, "onPostExecute: Parameter is: "+s);
            ParseXMLData parseXMLData = new ParseXMLData();
            parseXMLData.parseXML(s);// s is the xml the android framework has sent at this point

             //using my custom adapter
            feedAdapter = new ItemAdapter(MainActivity.this,
                    R.layout.list_record,
                    parseXMLData.getApplications()
            );
            //:::::::::pass item to map::::::::::::
            // initial map_fragment class
            map_fragment map_fragment = new map_fragment(feedAdapter.getItems());
            // Open fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.map_frame, map_fragment).commit();
            //:::::::::::pass items to map::::::::::::::::::
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
                    moreInfoIntent.putExtra("item", selectedItem.getDescription());
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
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Fetch earth quake data");
            pd.setMessage("Fetching....Please wait");
            pd.show();
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