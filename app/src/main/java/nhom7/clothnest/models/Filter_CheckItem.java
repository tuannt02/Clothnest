package nhom7.clothnest.models;

public class Filter_CheckItem {
    private boolean isChecked;
    private String title;

    public Filter_CheckItem(boolean isChecked, String title) {
        this.isChecked = isChecked;
        this.title = title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
