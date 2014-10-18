package nz.hogwild.model;

import javax.persistence.*;

@Entity
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "userIdGen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "userIdGen", sequenceName = "user_id_seq")
    private  int id;

    @Column(name = "email")
    private  String email;

    @Column(name = "characterName")
    private  String characterName;

    public User(){

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
