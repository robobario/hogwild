package nz.hogwild.model;

import com.google.common.collect.ImmutableSet;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity(name="user")
public class User implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "userIdGen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "userIdGen", sequenceName = "user_id_seq")
    private  int id;

    @Column(name = "email")
    private  String email;

    @Column(name = "characterName")
    private  String characterName;

    @ManyToMany(mappedBy="authors")
    public Set<Story> stories;

    public User(){

    }

    public Set<Story> getStories() {
        return ImmutableSet.copyOf(stories);
    }

    public void setStories(Set<Story> stories) {
        this.stories = stories;
    }

    public User(String email) {
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
