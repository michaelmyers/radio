package models.user;

import models.user.User;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class UserList extends Model {

    @Id
    public Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<User>();

    public void add(User user) {
        this.users.add(user);
    }

    public void remove(User user) {
        this.users.remove(user);
    }

    public Boolean contains(User user) {
        return this.users.contains(user);
    }

}
