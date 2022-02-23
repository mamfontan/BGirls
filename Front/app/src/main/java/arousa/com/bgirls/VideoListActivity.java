package arousa.com.bgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import arousa.com.bgirls.adapters.GalleryListAdapter;
import arousa.com.bgirls.adapters.VideoListAdapter;
import arousa.com.bgirls.model.Gallery;
import arousa.com.bgirls.model.Video;

public class VideoListActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    String idUser = "";
    public ArrayList<Video> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        addListenerMenu();

        // GetVideoList();
    }

    private void GetVideoList()
    {
        String url = Constants.ApiUrl + "video.php";

        try {
            URL endPoint = new URL(url);
            VideoListActivity.GetVideos cConnection = new VideoListActivity.GetVideos();
            cConnection.execute(endPoint);
        }
        catch (Exception error)
        {
            Log.e("ERROR", error.getMessage());
        }
    }

    private class GetVideos extends AsyncTask<URL, Void, Integer>
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
                    TypeToken<ArrayList<Video>> token = new TypeToken<ArrayList<Video>>() {};
                    videoList = gson.fromJson(data, token.getType());
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

    private void UpdateListElements()
    {
        ListView list = (ListView) findViewById(R.id.videoList);
        list.setAdapter(new VideoListAdapter(this, videoList));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_video_list, menu);
        return true;
    }

    public void addListenerMenu() {
        ImageView imgMenu = (ImageView) findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                PopupMenu popup = new PopupMenu(VideoListActivity.this, imgMenu);
                popup.setOnMenuItemClickListener(VideoListActivity.this);
                popup.inflate(R.menu.menu_video_list);
                popup.show();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuShowPics:
                Intent i = new Intent(VideoListActivity.this, GalleryListActivity.class);
                startActivity(i);
                return true;
            default:
                return false;
        }
    }
}