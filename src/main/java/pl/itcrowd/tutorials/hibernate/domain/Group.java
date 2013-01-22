package pl.itcrowd.tutorials.hibernate.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "GROUPS")
public class Group {

    @Id
    @SequenceGenerator(name = "GROUPS_ID_SEQUENCE", sequenceName = "GROUPS_ID_SEQUENCE", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GROUPS_ID_SEQUENCE")
    private Long id;

    @OneToMany(mappedBy = "group")
    private List<User> users = new ArrayList<User>();

    private String groupName;

    public List<User> getUsers() {
        return users;
    }

    public Group(Long id) {

        this.id = id;

    }

    public Group() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
