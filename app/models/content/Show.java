package models.content;

import models.classification.Genre;
import models.user.User;
import models.user.UserList;
import play.db.ebean.Model;
import utils.StringUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Show extends Model {

    @Id
    @Column(name = "show_id")
    public Long id;

    @Column(nullable = false)
    public ShowType showType;

    @Column(nullable = true)
    @ManyToOne
    public Network network;

    @Column(nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtil.trimToLength(name, 255);
    }

    @Column(nullable = true)
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = StringUtil.trimToLength(author, 255);
    }

    @Column(columnDefinition = "TEXT", nullable = true)
    public String description;

    @Column(nullable = true)
    public String rightsLabel;

    @Column(nullable = true)
    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<Genre> genres = new ArrayList<Genre>();

    public void addGenre(Genre genre) {
        if (!this.genres.contains(genre)) {
            this.genres.add(genre);
        }
    }

    public List<Genre> getGenres() {
        return genres;
    }

    @Column(nullable = true, unique=true, length = 2000)
    public String rssFeedUrl;

    @Column(nullable = true, length = 2000)
    public String websiteUrl;

    @Column(nullable = true)
    public String hostsDisplayName;

    @Column(nullable = true)
    public UserList hosts;

    @Column(nullable = true, length = 2000)
    public String iTunesLinkUrl;

    @Column(nullable = true)
    public Long iTunesId;

    @Column(nullable = true, length = 2000)
    public String imageUrl;

    /**
     * The language of the show, as to ISO-639.
     */
    @Column(nullable = true)
    private String languageCode;

    public String getLanguageCode() {
        return this.languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode.toLowerCase();
    }

    @Column(nullable = false)
    public Boolean explicit;

    @Column(nullable = false)
    public Date creationDate;

    @Column(nullable = true)
    public User createdBy;

    @ManyToMany(mappedBy = "shows")
    private List<ShowList> showLists;

    public Show() {
        this("New Show");
    }

    public Show(String name) {
        this(name, null);
    }

    public Show(String name, String feedUrl) {
        this(name, feedUrl, null);
    }

    public Show(String name, String feedUrl, Network network) {
        this(name, feedUrl, network, null);
    }

    public Show(String name, String feedUrl, Network network, User user) {
        setName(name);
        this.rssFeedUrl = feedUrl;
        this.network = network;
        this.creationDate = new Date();
        this.createdBy = user;
        this.showType = ShowType.WEEKLY;
        this.explicit = false;
    }

    public static Finder<Long, Show> find = new Finder<Long, Show>(Long.class, Show.class);

    public static List<Show> getShows() {
        return find.all();
    }

    public static int getNumberOfDistinctShows() {
        return find.findRowCount();
    }

    public static Show getShow(Long id) {
        return find.byId(id);
    }

    public static Show getShowByNameAndRSSFeedURL(String name, String rssFeedUrl) {
        //We have to trim the name of the show just like we do on setName
        return find.where().eq("rssFeedUrl", rssFeedUrl).eq("name", StringUtil.trimToLength(name, 255)).findUnique();
    }

    public static List<Show> getShowsByNetwork(Network network) {
        return Show.getShowsByNetwork(network.id);
    }

    public static List<Show> getShowsByNetwork(Long id) {
        return find.where().eq("network.id", id).findList();
    }

    public static List<Show> getShowsByGenre(Genre genre) {
        return find.where().eq("genres.id", genre.id).findList();
    }

    public static void deleteShow(Long id) {
        Show.getShow(id).delete();
    }

    @Override
    public String toString() {
        return "Show{" +
                "id=" + id +
                ", showType=" + showType +
                ", network=" + network +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                ", rightsLabel='" + rightsLabel + '\'' +
                ", genres=" + genres +
                ", rssFeedUrl='" + rssFeedUrl + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", hostsDisplayName='" + hostsDisplayName + '\'' +
                ", hosts=" + hosts +
                ", iTunesLinkUrl='" + iTunesLinkUrl + '\'' +
                ", iTunesId=" + iTunesId +
                ", imageUrl='" + imageUrl + '\'' +
                ", languageCode='" + languageCode + '\'' +
                ", explicit=" + explicit +
                ", creationDate=" + creationDate +
                ", createdBy=" + createdBy +
                ", showLists=" + showLists +
                '}';
    }
}
