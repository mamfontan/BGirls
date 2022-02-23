package arousa.com.bgirls.adapters;

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
import arousa.com.bgirls.model.Video;

public class VideoListAdapter extends BaseAdapter {

    private ArrayList<Video> listData;
    private LayoutInflater layoutInflater;

    public VideoListAdapter(Context aContext, ArrayList<Video> listData) {
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
            view = layoutInflater.inflate(R.layout.activity_video_list_row, null);
            holder = new ViewHolder();
            holder.videoImage = view.findViewById(R.id.imgVideo);
            holder.galleryName = view.findViewById(R.id.galleryName);
            holder.galleryViews = view.findViewById(R.id.galleryViews);
            holder.galleryRating = view.findViewById(R.id.galleryRating);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        new DownloadImageFromInternet(holder.videoImage).execute(listData.get(position).url);
        holder.galleryName.setText(listData.get(position).name);

        Integer numViews = listData.get(position).views;
        holder.galleryViews.setText("Views: " + numViews);

        float rating = listData.get(position).rating;
        holder.galleryRating.setText("Rating: " + rating);

        return view;
    }

    static class ViewHolder {
        ImageView videoImage;
        TextView galleryName;
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


