package arousa.com.bgirls.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import arousa.com.bgirls.R;
import arousa.com.bgirls.model.Gallery;

public class GalleryListAdapter extends BaseAdapter {

    private ArrayList<Gallery> listData;
    private LayoutInflater layoutInflater;

    public GalleryListAdapter(Context aContext, ArrayList<Gallery> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.i("getView", "Llegamos a getView");
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.activity_gallery_list_row, null);
            holder = new ViewHolder();
            holder.galleryImage = (ImageView) view.findViewById(R.id.imgGallery);
            holder.galleryName = (TextView) view.findViewById(R.id.galleryName);
            holder.galleryPicsValue = (TextView) view.findViewById(R.id.galleryPicsValue);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //new DownloadImageFromInternet(holder.galleryImage).execute("https://gals.kindgirls.com/d009/lana_lane_24_09964/lana_lane_24_09964_5.jpg");
        new DownloadImageFromInternet(holder.galleryImage).execute(listData.get(position).getMainPic());
        holder.galleryName.setText(listData.get(position).getName());

        Integer numPics = listData.get(position).getNumPics();
        holder.galleryPicsValue.setText(numPics.toString());
        return view;
    }

    static class ViewHolder {
        ImageView galleryImage;
        TextView galleryName;
        TextView galleryPicsValue;
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}


