package arousa.com.bgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import arousa.com.bgirls.model.Gallery;

public class GalleryPicsActivity extends AppCompatActivity {

    private Gallery _gallery;
    private Integer _actualPic = 1;
    private ArrayList<Bitmap> _bitmaps;

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

        //LoadGalleryPics();
    }
    
    private void LoadGalleryPics()
    {
        _bitmaps = new ArrayList<Bitmap>();

        for (int index = 0; index < _gallery.getNumPics(); index++)
        {
            Bitmap newBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.question);
            _bitmaps.add(newBitmap);

            new DownloadImageFromInternet2(newBitmap).execute(_gallery.getPics().get(index).getFile());
        }
    }

    private class DownloadImageFromInternet2 extends AsyncTask<String, Void, Bitmap> {
        Bitmap bitmap;

        public DownloadImageFromInternet2(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bImage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bImage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
            }
            return bImage;
        }

        protected void onPostExecute(Bitmap result) {
            this.bitmap = result;
        }
    }    
}