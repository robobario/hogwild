package nz.hogwild.model;

import com.google.common.collect.ImmutableList;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "story")
public class Story implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "storyIdGen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "storyIdGen", sequenceName = "story_id_seq")
    private  int id;

    @ManyToMany
    @JoinTable(name="story_author",
            joinColumns=
            @JoinColumn(name="story_id"),
            inverseJoinColumns=
            @JoinColumn(name="author_id")
    )
    private List<User> authors;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    private List<Entry> entries;

    public Story(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAuthors(List<User> author) {
        this.authors = author;
    }

    public List<User> getAuthors() {
        return ImmutableList.copyOf(authors);
    }

    public List<Entry> getEntries() {
        return ImmutableList.copyOf(entries);
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }
}
