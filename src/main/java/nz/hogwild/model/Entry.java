package nz.hogwild.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "entry")
public class Entry implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "entryIdGen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "entryIdGen", sequenceName = "entry_id_seq")
    private  int id;

    @Column(name = "body")
    private  String body;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name="author_id", nullable=false, updatable=false)
    private Author author;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name="story_id", nullable=false, updatable=false)
    private Story story;

    public Entry(){

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

    public void setAuthor(Author author) {
        this.author = author;
    }


    public String getBody() {
        return body;
    }

    public Author getAuthor() {
        return author;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }
}
