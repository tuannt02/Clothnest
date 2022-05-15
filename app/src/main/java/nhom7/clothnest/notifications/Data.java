package nhom7.clothnest.notifications;

public class Data {
    private String user, title, body, sent;

    public Data() {
    }

    public Data(String user, String title, String body, String sent) {
        this.user = user;
        this.title = title;
        this.body = body;
        this.sent = sent;
    }

    public String getSent() {
        return sent;
    }

    public String getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
