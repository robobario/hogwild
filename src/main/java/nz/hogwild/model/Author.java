package nz.hogwild.model;

import com.google.common.collect.ImmutableSet;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity(name="author")
public class Author implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "authorIdGen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "authorIdGen", sequenceName = "author_id_seq")
    private  int id;

    @Column(name = "email")
    private  String email;

    @Column(name = "character_name")
    private  String characterName;

    @ManyToMany(mappedBy="authors")
    public Set<Story> stories;

    public Author(){

    }

    public Set<Story> getStories() {
        return ImmutableSet.copyOf(stories);
    }

    public void setStories(Set<Story> stories) {
        this.stories = stories;
    }

    public Author(String email) {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }
}
