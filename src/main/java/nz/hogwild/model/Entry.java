package nz.hogwild.model;

import javax.persistence.*;

@Entity
public class Entry {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "entryIdGen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "entryIdGen", sequenceName = "entry_id_seq")
    private  int id;

    @Column(name = "body")
    private  String body;

    @Column(name = "author")
    private  String author;

    public Entry(){

    }
    public Entry(int id, String body, String author) {
        this.id = id;
        this.body = body;
        this.author = author;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }
}
