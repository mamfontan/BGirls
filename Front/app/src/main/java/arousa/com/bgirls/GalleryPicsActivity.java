package arousa.com.bgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import arousa.com.bgirls.model.Gallery;

public class GalleryPicsActivity extends AppCompatActivity {

    private Gallery _gallery;
    private Integer _actualPic = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i  = getIntent();
        _gallery  = (Gallery)i.getParcelableExtra("gallery");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_pics);

        LoadGalleryData();
    }

    private void LoadGalleryData()
    {
        TextView galleryName = (TextView) findViewById(R.id.lblGalleryName);
        galleryName.setText(_gallery.getName());
    }
}