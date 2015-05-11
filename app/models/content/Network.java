package models.content;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.user.User;
import models.user.UserList;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Network extends Model {

    @Id
    @Column(name = "NETWORK_ID")
    public Long id;

    @Column(nullable = false, unique=true)
    public String name;

    @Column(nullable = true)
    public String websiteUrl;

    @Column(nullable = true)
    public String profileImageUrl;

    @Column(nullable = true)
    public String headerImageUrl;

    @Column(nullable = true)
    public String description;

    @Column(nullable = false)
    public Date creationDate;

    @Column(nullable = true)
    public User createdBy;

    @Column(nullable = false)
    public User contactUser;

    @Column(nullable = false)
    @JsonIgnore
    private UserList admins = new UserList();

    /**
     * Add an admin to the publisher
     *
     * @param admin
     */
    public void addAdmin(User admin) {
        admins.add(admin);
    }

    /**
     * Remove an admin from the publisher
     *
     * @param admin
     */
    public void removeAdmin(User admin) {
        admins.remove(admin);
    }

    /**
     * Check if a user is an admin for the publisher
     *
     * @param user
     * @return
     */
    public Boolean isAdmin(User user) {
        return admins.contains(user);
    }

    public Network() {
        this("Publisher Name");
    }

    public Network(String name) {
        this.name = name;
        this.creationDate = new Date();
    }

    public static Finder<Long, Network> find = new Finder<Long, Network>(Long.class, Network.class);

    public static List<Network> getNetworks() {
        return find.all();
    }

    public static int getNumberOfDistinctNetworks() {
        return find.findRowCount();
    }

    public static Network getNetwork(Long id) {
        return find.byId(id);
    }

    public static Network getNetworkByName(String name) { return find.where().eq("name", name).findUnique();}

}
