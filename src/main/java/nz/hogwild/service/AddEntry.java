package nz.hogwild.service;

public class AddEntry {
    private String body;
    private int authorId;

    public AddEntry(){

    }

    public AddEntry(String body, int authorId) {
        this.body = body;
        this.authorId = authorId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
}
