package nz.hogwild.service;

public class ApiEntry {

    private  String body;

    private String authorEmail;

    private boolean isSpoiler;

    public ApiEntry(){

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public boolean isSpoiler() {
        return isSpoiler;
    }

    public void setSpoiler(boolean isSpoiler) {
        this.isSpoiler = isSpoiler;
    }
}
