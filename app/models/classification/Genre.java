package models.classification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.user.User;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

/**
 *  Genres are used to describe the content of the audio
 *
 *  @auther Michael Myers
 *  @since 2015-01-15
 */
@Entity
public class Genre extends Model {

    @Id
    @Column(name = "genre_id")
    public Long id;

    @Column(unique=true, length = 256, nullable = false)
    @Constraints.MinLength(2)
    @Constraints.MaxLength(256)
    public String name;

    @Column(columnDefinition = "TEXT", nullable = true)
    public String description;

    /**
     *  A genre can also be a subset of a larger genre that is more broad.
     */
    @ManyToOne
    @Column(unique=true, nullable = true)
    public Genre parentGenre;

    /**
     * Helper function to determine if the genre is a subgenre of another genre.  If it is, check parentGenre for it's parent.
     *
     * @return If the genre is a subgrenre
     */
    @JsonIgnore
    public Boolean isSubgenre() {
        return (this.parentGenre != null);
    }

    /**
     * The Genre ID as defined by Apple.
     *
     * See http://www.apple.com/itunes/affiliates/resources/documentation/genre-mapping.html for reference
     */
    @Column(nullable = true)
    public Long appleGenreId;

    @Column(nullable = false)
    public Date creationDate;

    @Column(nullable = true)
    public User createdBy;

    public Genre() {
        this("New Genre");
    }

    public Genre(String name) {
        this(name, null);
    }

    public Genre(String name, User user) {
        this.name = name;
        this.creationDate = new Date();
        this.createdBy = user;
    }

    public static Finder<Long, Genre> find = new Finder<Long, Genre>(Long.class, Genre.class);

    public static List<Genre> getGenres() {
        return find.all();
    }

    public static int count() {
        return find.findRowCount();
    }

    public static Genre getGenre(Long id) {
        return find.byId(id);
    }

    public static void deleteGenre(Long id) {
        //TODO: This will be more complicated, we will have to figure out what to do with all the shows that have this genre
        Genre.getGenre(id).delete();
    }

    public static List<Genre> getSubgenres(Genre genre) {
        return find.where().eq("parentGenre.id", genre.id).findList();
    }

    public static Genre getGenreByAppleId(Long id) {
        return find.where().eq("appleGenreId", id).findUnique();
    }

    public static List<Genre> getGenresWithAppleId() {
        return find.where().isNotNull("appleGenreId").findList();
    }

    public static Genre getGenreByName(String name) {
        return find.where().eq("name", name).findUnique();
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parentGenre=" + parentGenre +
                ", appleGenreId=" + appleGenreId +
                ", creationDate=" + creationDate +
                ", createdBy=" + createdBy +
                '}';
    }
}
