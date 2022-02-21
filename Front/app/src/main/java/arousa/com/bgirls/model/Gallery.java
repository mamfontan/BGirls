package arousa.com.bgirls.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Gallery implements Parcelable
{
    protected Gallery(Parcel in) {
        _name = in.readString();
        _mainPic = in.readString();
        _views = in.readInt();
        _rating = in.readFloat();
        _pics = in.createTypedArrayList(Pic.CREATOR);
    }

    public static final Creator<Gallery> CREATOR = new Creator<Gallery>() {
        @Override
        public Gallery createFromParcel(Parcel in) {
            return new Gallery(in);
        }

        @Override
        public Gallery[] newArray(int size) {
            return new Gallery[size];
        }
    };

    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    private String _mainPic;

    public String getMainPic() {
        return _mainPic;
    }

    public void setMainPic(String mainPic) {
        _mainPic = mainPic;
    }

    private int _views;

    public int getNumViews() {
        return _views;
    }

    public void setNumViews(int views) {
        _views = views;
    }

    private float _rating;

    public float getRating() {
        return _rating;
    }

    public void setRating(float rating) {
        _rating = rating;
    }

    private ArrayList<Pic> _pics;

    public ArrayList<Pic> getPics() {
        return _pics;
    }

    public int getNumPics() {
        return _pics.size();
    }

    public Gallery()
    {
        _pics = new ArrayList<Pic>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_name);
        parcel.writeString(_mainPic);
        parcel.writeInt(_views);
        parcel.writeFloat(_rating);
        parcel.writeTypedList(_pics);
    }
}
