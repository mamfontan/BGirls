package arousa.com.bgirls;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import arousa.com.bgirls.model.Gallery;

public class GalleryListActivity extends AppCompatActivity {

    public List<Gallery> galleryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_list);

        HookButtonEvents();
        //GetGalleryList();
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