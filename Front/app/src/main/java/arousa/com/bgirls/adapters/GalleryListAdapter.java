package arousa.com.bgirls.adapters;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import arousa.com.bgirls.R;
import arousa.com.bgirls.helpers.ApiHelper;
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
        ViewHolder holder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.activity_gallery_list_row, null);
            holder = new ViewHolder();
            holder.galleryImage = view.findViewById(R.id.imgGallery);
            holder.galleryName = view.findViewById(R.id.galleryName);
            holder.galleryPics = view.findViewById(R.id.galleryPics);
            holder.galleryViews = view.findViewById(R.id.galleryViews);
            holder.galleryRating = view.findViewById(R.id.galleryRating);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        new DownloadImageFromInternet(holder.galleryImage).execute(listData.get(position).getMainPic());
        holder.galleryName.setText(listData.get(position).getName());

        Integer numPics = listData.get(position).getNumPics();
        holder.galleryPics.setText( "Num. pics: "  + numPics.toString());

        Integer numViews = listData.get(position).getNumViews();
        holder.galleryViews.setText("Views: " + numViews.toString());

        float rating = listData.get(position).getRating();
        holder.galleryRating.setText("Rating: " + rating);

        return view;
    }

    static class ViewHolder {
        ImageView galleryImage;
        TextView galleryName;
        TextView galleryPics;
        TextView galleryViews;
        TextView galleryRating;
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
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


