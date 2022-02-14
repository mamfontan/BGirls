package arousa.com.bgirls.model;

public class Gallery
{
    public int Year;

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public int Month;

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

    public String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String MainPic;

    public String getMainPic() {
        return MainPic;
    }

    public void setMainPic(String mainPic) {
        MainPic = mainPic;
    }

    public int Views;

    public int getViews() {
        return Views;
    }

    public void setViews(int views) {
        Views = views;
    }

    public float Score;

    public float getScore() {
        return Score;
    }

    public void setScore(float score) {
        Score = score;
    }

    private int _numPics;

    public int getNumPics() {
        return _numPics;
    }

    public void setNumPics(int _numPics) {
        this._numPics = _numPics;
    }
}
