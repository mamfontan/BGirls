package arousa.com.bgirls;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_list);

        HookButtonEvents();
    }

    private void HookButtonEvents()
    {
        ImageView imgDownload = (ImageView) findViewById(R.id.imgDownload);
        imgDownload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(GalleryListActivity.this, "Not available yet", Toast.LENGTH_LONG).show();
            }
        });

        ImageView imgInformation = (ImageView) findViewById(R.id.imgInformation);
        imgInformation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(GalleryListActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });

        ImageView imgShare = (ImageView) findViewById(R.id.imgShare);
        imgShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(GalleryListActivity.this, "Not available yet", Toast.LENGTH_LONG).show();
            }
        });
    }
}