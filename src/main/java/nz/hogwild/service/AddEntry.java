package nz.hogwild.service;

public class AddEntry {
    private String body;

    public AddEntry(){

    }

    public AddEntry(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
