package arousa.com.bgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

import arousa.com.bgirls.model.Gallery;

public class GalleryPicsActivity extends AppCompatActivity {

    private Gallery _gallery;
    private Integer _actualPicIndex = 0;
    private ArrayList<Bitmap> _bitmaps;
    private ArrayList<Boolean> _imgLoaded;

    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i  = getIntent();
        _gallery  = (Gallery)i.getParcelableExtra("gallery");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_pics);

        LoadGalleryName();
        LoadGalleryIndex();
        LoadGalleryEmptyImages();
        LoadGalleryImage(_actualPicIndex);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    private void LoadGalleryName()
    {
        TextView galleryName = (TextView) findViewById(R.id.lblGalleryName);
        galleryName.setText(_gallery.getName());
    }

    private void LoadGalleryIndex()
    {
        TextView galleryIndex = (TextView) findViewById(R.id.lblGalleryIndex);
        galleryIndex.setText((_actualPicIndex + 1) + " / " + _gallery.getNumPics());
    }

    private void LoadGalleryEmptyImages()
    {
        _bitmaps = new ArrayList<Bitmap>();
        _imgLoaded = new ArrayList<Boolean>();

        for (Integer index = 0; index < _gallery.getNumPics(); index++)
        {
            Bitmap newBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.question);
            _bitmaps.add(newBitmap);

            _imgLoaded.add(false);
        }
    }

    private void LoadGalleryImage(Integer index)
    {
        ImageView img = (ImageView) findViewById(R.id.imageView);
        new DownloadImageFromInternet(img).execute(_gallery.getPics().get(index).getFile());
    }
    
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected void onPreExecute(Bitmap result) {
            // Show loader
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
            imageView.setImageBitmap(result);
            LoadGalleryIndex();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    if (x2 > x1) {
                        if (_actualPicIndex > 0) {
                            _actualPicIndex = _actualPicIndex - 1;
                        }
                    } else {
                        if (_actualPicIndex < _gallery.getNumPics()-1) {
                            _actualPicIndex = _actualPicIndex + 1;
                        }
                    }
                }
                break;
        }

        LoadGalleryImage(_actualPicIndex);

        return super.onTouchEvent(event);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            ImageView img = (ImageView) findViewById(R.id.imageView);
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            img.setScaleX(mScaleFactor);
            img.setScaleY(mScaleFactor);
            return true;
        }
    }
}