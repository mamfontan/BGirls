package arousa.com.bgirls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import arousa.com.bgirls.model.Gallery;

public class GalleryPicsActivity extends AppCompatActivity {

    private final String SEPARATOR = " / ";

    private Gallery _gallery;
    private Integer _actualPicIndex = 0;
    private Integer _loadedPics = 0;

    private ArrayList<Bitmap> _bitmaps = new ArrayList<Bitmap>();

    private float x1,x2;
    static final int MIN_DISTANCE = 150;

    private ImageView _mainImageView;
    private TextView _galleryIndex;
    private TextView _loader;
    private ImageView _left;
    private ImageView _right;
    private ImageView _star;
    private ImageView _download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i  = getIntent();
        _gallery  = (Gallery)i.getParcelableExtra("gallery");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_pics);

        GetViewControls();
        LoadGalleryName();

        HookButtonEvents();
        UpdateLoaderText(_loadedPics, _gallery.pics.size());
        ShowMainImage(false);
        ShowLoader(true);

        LoadGalleryImages();
    }

    private void LoadGalleryName()
    {
        TextView galleryName = (TextView) findViewById(R.id.lblGalleryName);
        galleryName.setText(_gallery.name);
    }

    private void GetViewControls()
    {
        _mainImageView = (ImageView) findViewById(R.id.imageView);
        _loader = (TextView) findViewById(R.id.lblGalleryLoading);
        _galleryIndex = (TextView) findViewById(R.id.lblGalleryIndex);
        _left = (ImageView) findViewById(R.id.btnLeft);
        _right = (ImageView) findViewById(R.id.btnRight);
        _star = (ImageView) findViewById(R.id.btnStar);
        _download = (ImageView) findViewById(R.id.btnDownload);
    }

    private void HookButtonEvents()
    {
        _left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_actualPicIndex > 0) {
                    _actualPicIndex = _actualPicIndex - 1;
                } else {
                    _actualPicIndex = _gallery.pics.size()-1;
                }
                SetMainImage();
            }
        });

        _right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_actualPicIndex < _gallery.pics.size()-1) {
                    _actualPicIndex = _actualPicIndex + 1;
                } else {
                    _actualPicIndex = 0;
                }
                SetMainImage();
            }
        });

        _star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });

        _download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
            }
        });
    }

    private void ShowMainImage(boolean show)
    {
        if (show) {
            _mainImageView.setVisibility(View.VISIBLE);
            _galleryIndex.setVisibility(View.VISIBLE);
            _left.setVisibility(View.VISIBLE);
            _right.setVisibility(View.VISIBLE);
            _star.setVisibility(View.VISIBLE);
            _download.setVisibility(View.VISIBLE);
        } else {
            _mainImageView.setVisibility(View.INVISIBLE);
            _galleryIndex.setVisibility(View.INVISIBLE);
            _left.setVisibility(View.INVISIBLE);
            _right.setVisibility(View.INVISIBLE);
            _star.setVisibility(View.INVISIBLE);
            _download.setVisibility(View.INVISIBLE);
        }
    }

    private void SetMainImage()
    {
        _mainImageView.setImageBitmap(_bitmaps.get(_actualPicIndex));
        SetGalleryIndex();
    }

    private void ShowLoader(boolean show)
    {
        if (show)
            _loader.setVisibility(View.VISIBLE);
        else
            _loader.setVisibility(View.INVISIBLE);
    }

    private void UpdateLoaderText(Integer loadedPics, Integer numPics)
    {
        _loader.setText(loadedPics + SEPARATOR +  numPics);
    }

    private void SetGalleryIndex()
    {
        _galleryIndex.setText((_actualPicIndex + 1) + SEPARATOR + _gallery.pics.size());
    }

    private void LoadGalleryImages()
    {
        for (Integer index = 0; index < _gallery.pics.size(); index++)
            new DownloadImageFromInternet().execute(_gallery.pics.get(index));
    }
    
    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {

        public DownloadImageFromInternet() {
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
            _bitmaps.add(result);

            _loadedPics = _loadedPics + 1;

            if (_loadedPics == _gallery.pics.size()) {
                ShowLoader(false);
                ShowMainImage(true);
                SetMainImage();
            } else {
                UpdateLoaderText(_loadedPics, _gallery.pics.size());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}