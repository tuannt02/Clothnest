package nhom7.clothnest.models;

public class Comment {
    private String name;
    private int avt;
    private String dateTime;
    private int starNumber;
    private String type;
    private String comment;

    public Comment(String name, int avt, String dateTime, int starNumber, String type, String comment) {
        this.name = name;
        this.avt = avt;
        this.dateTime = dateTime;
        this.starNumber = starNumber;
        this.type = type;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvt() {
        return avt;
    }

    public void setAvt(int avt) {
        this.avt = avt;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getStarNumber() {
        return starNumber;
    }

    public void setStarNumber(int starNumber) {
        this.starNumber = starNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
