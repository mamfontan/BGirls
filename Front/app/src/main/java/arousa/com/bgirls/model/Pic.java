package arousa.com.bgirls.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pic implements Parcelable {

    private String _file;

    protected Pic(Parcel in) {
        _file = in.readString();
    }

    public static final Creator<Pic> CREATOR = new Creator<Pic>() {
        @Override
        public Pic createFromParcel(Parcel in) {
            return new Pic(in);
        }

        @Override
        public Pic[] newArray(int size) {
            return new Pic[size];
        }
    };

    public String getFile() {
        return _file;
    }

    public void setFile(String file) {
        _file = file;
    }

    public Pic(String file)
    {
        _file = file;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(_file);
    }
}
