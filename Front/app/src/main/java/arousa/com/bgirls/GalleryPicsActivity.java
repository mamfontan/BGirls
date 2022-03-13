package arousa.com.bgirls;

import static arousa.com.bgirls.Constants.WRITE_EXTERNAL_STORAGE_PERMISSION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import arousa.com.bgirls.model.Gallery;

public class GalleryPicsActivity extends AppCompatActivity {

    private Gallery _gallery;
    private Integer _actualPicIndex = 0;
    private Integer _loadedPics = 0;

    private ArrayList<Bitmap> _bitmaps = new ArrayList<>();

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
                // TODO Rating activity
            }
        });

        _download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveImage();
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
        _loader.setText(loadedPics + Constants.SEPARATOR +  numPics);
    }

    private void SetGalleryIndex()
    {
        _galleryIndex.setText((_actualPicIndex + 1) + Constants.SEPARATOR + _gallery.pics.size());
    }

    private void LoadGalleryImages()
    {
        for (int index = 0; index < _gallery.pics.size(); index++)
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

    private void SaveImage()
    {
        if (ContextCompat.checkSelfPermission(GalleryPicsActivity.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(GalleryPicsActivity.this, new String[]{ Manifest.permission.MANAGE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION);
        }

        String message = "";
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "BeautyGirl";
        File appDir = new File(storePath);
        boolean albumCreated = true;
        if (!appDir.exists()) {
            albumCreated = appDir.mkdir();
            if (!albumCreated)
                message = "Error creating the gallery album";
        }

        if (albumCreated)
        {
            String fileName = _gallery.name +  " (" + (_actualPicIndex + 1) +  ").jpg";
            File file = new File(appDir, fileName);
            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file, true);
                //Compress and save pictures by io stream
                boolean isSuccess = _bitmaps.get(_actualPicIndex).compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.flush();
                fos.close();

                //Insert files into the system Gallery
                //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

                //Update the database by sending broadcast notifications after saving pictures
                Uri uri = Uri.fromFile(file);
                getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                if (isSuccess) {
                    message = "Picture saved successfully";
                } else {
                    message = "Error saving picture";
                }
            } catch (IOException e) {
                message = e.getMessage();
            }
        }

        Toast.makeText(GalleryPicsActivity.this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        /*
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    // boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted)
                        Snackbar.make(view, "Permission Granted, Now you can access camera.", Snackbar.LENGTH_LONG).show();
                    else {
                        Snackbar.make(view, "Permission Denied, You cannot access camera.", Snackbar.LENGTH_LONG).show();
                    }
                }

                break;
        }

         */
    }

}