package arousa.com.bgirls;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import arousa.com.bgirls.adapters.GalleryListAdapter;
import arousa.com.bgirls.model.Gallery;

public class GalleryListActivity extends AppCompatActivity {

    public ArrayList<Gallery> galleryList = new ArrayList<Gallery>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_list);

        HookButtonEvents();

        Gallery g1 = new Gallery();
        g1.setMainPic("https://gals.kindgirls.com/d009/vine_23_38569/vine_23_38569_2.jpg");
        g1.setName("Vine");
        g1.setNumPics(12);
        galleryList.add(g1);

        Gallery g2 = new Gallery();
        g2.setMainPic("https://gals.kindgirls.com/d009/delfina_26_00498/delfina_26_00498_3.jpg");
        g2.setName("Delfine");
        g1.setNumPics(15);
        galleryList.add(g2);

        Gallery g3 = new Gallery();
        g3.setMainPic("https://gals.kindgirls.com/d009/sailor_21_38858/sailor_21_38858_3.jpg");
        g3.setName("Sailor");
        g1.setNumPics(13);
        galleryList.add(g3);

        ListView list = (ListView) findViewById(R.id.galleryList);
        list.setAdapter(new GalleryListAdapter(this, galleryList));
        Log.i("onCreate", "Hemos establecido el adaptador");
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = list.getItemAtPosition(position);
                Gallery newsData = (Gallery) o;
                Toast.makeText(GalleryListActivity.this, "Selected :" + " " + newsData, Toast.LENGTH_LONG).show();
            }
        });

        ShowNoDataLabel(false);

        //GetGalleryList();
    }

    private void ShowNoDataLabel(boolean value)
    {
        TextView label = (TextView) findViewById(R.id.lblNoData);
        if (value)
            label.setVisibility(View.VISIBLE);
        else
            label.setVisibility(View.INVISIBLE);
    }

    private void HookButtonEvents()
    {
        ImageView imgMenu = (ImageView) findViewById(R.id.imgMenu);
        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
    }

    private void GetGalleryList()
    {
        String url = Constants.ApiUrl + "gallery.php";

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

    private class GetGalleries extends AsyncTask<URL, Void, Integer> {
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
                    String cad = convertStreamToString(in);

                    myConnection.disconnect();

                    if (cad.contains("Server is online")) {
                        result = 0;
                    }
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
            if (result == 0) {

            } else {

            }
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
}