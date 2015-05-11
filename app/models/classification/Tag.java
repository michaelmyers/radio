package models.classification;

import com.avaje.ebean.Page;
import models.user.User;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Entity
public class Tag extends Model {

    @Id
    @Column(name = "tag_id")
    public Long id;

    @Column(nullable = false, length = 256, unique = true)
    @Constraints.MinLength(2)
    @Constraints.MaxLength(256)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        //make trim, lowercase, remove quotes
        this.name = name.trim().toLowerCase().replaceAll("^\"|\"$", "");
    }

    @Column(nullable = false)
    public Date creationDate;

    @Column(nullable = true)
    public User createdBy;

    public Tag() {
        this("Tag Name");
    }

    public Tag(String name) {
        this(name, null);
    }

    public Tag(String name, User user) {
        this.setName(name);
        this.createdBy = user;
        this.creationDate = new Date();
    }

    public static Finder<Long, Tag> find = new Finder<Long, Tag>(Long.class, Tag.class);

    public static List<Tag> getTags() {
        return find.findList();
    }

    public static Page<Tag> getTags(int page, int pageSize, String sortBy, String order, String search) {
        return find.where()
                .ilike("name", search)
                .orderBy(sortBy + " " + order)
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }

    public static int count() {
        return find.findRowCount();
    }

    public static Tag getTag(Long id) { return find.byId(id); }

    public static Tag getTagByName(String name) {
        return find.where().eq("name", name.toLowerCase()).findUnique();
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
