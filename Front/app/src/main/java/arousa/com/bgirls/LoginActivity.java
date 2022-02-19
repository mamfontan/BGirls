package arousa.com.bgirls;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import arousa.com.bgirls.database.DbHelper;

public class LoginActivity extends AppCompatActivity {

    TextView lblStatus;
    int serverConnectionStatus = 0;
    String idUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lblStatus = (TextView) findViewById(R.id.lblCheckStatus);

        CheckConnectionToServer();
    }

    private void NavigateToGalleryList()
    {
        Intent i = new Intent(LoginActivity.this, GalleryListActivity.class);
        startActivity(i);
    }

    private void SetErrorMessage()
    {
        lblStatus.setText(R.string.connectionError);
    }

    private void CheckConnectionToServer() {
        String url = Constants.ApiUrl + "check.php";

        try {
            URL endPoint = new URL(url);
            CheckConnection cConnection = new CheckConnection();
            cConnection.execute(endPoint);
        }
        catch (Exception error)
        {
            lblStatus.setText(error.getMessage());
        }
    }

    private void SendLoginToServer()
    {
        String url = Constants.ApiUrl + "login.php";

        lblStatus.setText(R.string.lblCheckStatus02);

        try {
            DbHelper dbHelper = new DbHelper(LoginActivity.this);
            idUser = dbHelper.getIdentification();
            if (idUser == "") {
                UUID uuid = UUID.randomUUID();
                idUser = uuid.toString();
                dbHelper.insertIdentification(idUser);
            }

            URL endPoint = new URL(url);
            MarkLogin cConnection = new MarkLogin();
            cConnection.execute(endPoint);
        }
        catch (Exception error)
        {
            lblStatus.setText(error.getMessage());
        }
    }

    private class CheckConnection extends AsyncTask<URL, Void, Integer> {
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
                SendLoginToServer();
            } else {
                SetErrorMessage();
            }
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

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

    private class MarkLogin extends AsyncTask<URL, Void, Integer>
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
            if (result == 0) {
                NavigateToGalleryList();
            } else {
                SetErrorMessage();
            }
        }
    }
}