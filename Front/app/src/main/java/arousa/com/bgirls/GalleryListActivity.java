package arousa.com.bgirls;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import arousa.com.bgirls.adapters.GalleryListAdapter;
import arousa.com.bgirls.database.DbHelper;
import arousa.com.bgirls.model.Gallery;

public class GalleryListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    String idUser = "";
    String order = "id";
    public ArrayList<Gallery> galleryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_list);

        DbHelper dbHelper = new DbHelper(GalleryListActivity.this);
        this.idUser = dbHelper.getIdentification();

        addListenerMenu();
        ShowNoDataLabel(false);
        GetGalleryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery_list, menu);
        return true;
    }

    public void addListenerMenu() {
        ImageView imgMenu = (ImageView) findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                PopupMenu popup = new PopupMenu(GalleryListActivity.this, imgMenu);
                popup.setOnMenuItemClickListener(GalleryListActivity.this);
                popup.inflate(R.menu.menu_gallery_list);
                popup.show();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuViewMoreRecent:
                order = "id";
                GetGalleryList();
                return true;
            case R.id.menuViewMostVoted:
                order = "views";
                GetGalleryList();
                return true;
            case R.id.menuAbout:
                Intent i = new Intent(GalleryListActivity.this, AboutActivity.class);
                startActivity(i);
                return true;
            default:
                return false;
        }
    }

    private void ShowNoDataLabel(boolean value)
    {
        TextView label = (TextView) findViewById(R.id.lblNoData);
        if (value)
            label.setVisibility(View.VISIBLE);
        else
            label.setVisibility(View.INVISIBLE);
    }

    private void GetGalleryList()
    {
        String url = Constants.ApiUrl + "gallery.php?order=" + order;

        try {

            URL endPoint = new URL(url);
            GetGalleries cConnection = new GetGalleries();
            cConnection.execute(endPoint);
        }
        catch (Exception error)
        {
            Log.e("ERROR", error.getMessage());
        }
    }

    private void UpdateListElements()
    {
        ListView list = (ListView) findViewById(R.id.galleryList);
        list.setAdapter(new GalleryListAdapter(this, galleryList));
        Log.i("onCreate", "Hemos establecido el adaptador");
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = list.getItemAtPosition(position);
                Gallery selectedGallery = (Gallery) o;

                try {
                    String url = Constants.ApiUrl + "gallery.php";
                    URL endPoint = new URL(url);
                    AddGalleryView addGalleryView = new AddGalleryView(selectedGallery.id);
                    addGalleryView.execute(endPoint);
                }
                catch (Exception error)
                {
                    Log.e("ERROR", error.getMessage());
                }

                Intent i = new Intent(GalleryListActivity.this, GalleryPicsActivity.class);
                i.putExtra("gallery", selectedGallery);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        String url = Constants.ApiUrl + "logout.php";

        try {
            URL endPoint = new URL(url);
            GalleryListActivity.MarkLogout cConnection = new GalleryListActivity.MarkLogout();
            cConnection.execute(endPoint);
        }
        catch (Exception error) {
        }
    }

    private class AddGalleryView extends AsyncTask<URL, Void, Integer>
    {
        private Integer _idGallery;

        public AddGalleryView(Integer idGallery)
        {
            _idGallery = idGallery;
        }

        @Override
        protected Integer doInBackground(URL... urls) {
            Integer result = 0;
            try
            {
                Map<String,Object> params = new LinkedHashMap<>();
                params.put("id", _idGallery);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String,Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection myConnection = (HttpURLConnection) urls[0].openConnection();
                myConnection.setRequestMethod("POST");
                myConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                myConnection.setDoOutput(true);
                myConnection.getOutputStream().write(postDataBytes);

                Reader in = new BufferedReader(new InputStreamReader(myConnection.getInputStream(), "UTF-8"));

                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;)
                    sb.append((char)c);
                String response = sb.toString();

                if (myConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    myConnection.disconnect();
                }
            }
            catch (Exception error) {
                result = -1;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
        }
    }

    private class GetGalleries extends AsyncTask<URL, Void, Integer>
    {
        @Override
        protected Integer doInBackground(URL... urls) {
            Integer result = 0;
            try
            {
                HttpURLConnection myConnection = (HttpURLConnection) urls[0].openConnection();
                myConnection.setRequestMethod("GET");

                if (myConnection.getResponseCode() == 200) {
                    // Success
                    InputStream responseBody = myConnection.getInputStream();
                    InputStream in = new BufferedInputStream(myConnection.getInputStream());
                    String data = convertStreamToString(in);
                    myConnection.disconnect();

                    Gson gson = new Gson();
                    TypeToken<ArrayList<Gallery>> token = new TypeToken<ArrayList<Gallery>>() {};
                    galleryList = gson.fromJson(data, token.getType());
                }
            }
            catch (Exception error) {
                result = -1;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Call activity method with results
            UpdateListElements();
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                sb.append(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return sb.toString();
        }
    }

    private class MarkLogout extends AsyncTask<URL, Void, Integer>
    {
        @Override
        protected Integer doInBackground(URL... urls) {
            Integer result = 0;
            try
            {
                Map<String,Object> params = new LinkedHashMap<>();
                params.put("device", idUser);

                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String,Object> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                HttpURLConnection myConnection = (HttpURLConnection) urls[0].openConnection();
                myConnection.setRequestMethod("POST");
                myConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                myConnection.setDoOutput(true);
                myConnection.getOutputStream().write(postDataBytes);

                Reader in = new BufferedReader(new InputStreamReader(myConnection.getInputStream(), "UTF-8"));

                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;)
                    sb.append((char)c);
                String response = sb.toString();

                if (myConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    myConnection.disconnect();
                }
            }
            catch (Exception error) {
                result = -1;
            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
        }
    }
}