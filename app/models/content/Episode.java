package models.content;

import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import models.classification.Genre;
import models.classification.Tag;
import models.classification.TagList;
import models.user.User;
import play.Logger;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.StringUtil;

@Entity
public class Episode extends Model {

    @Id
    @Column(name = "episode_id")
    public Long id;

    @Column(nullable = false)
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = StringUtil.trimToLength(name, 255);
    }

    @Column(nullable = true)
    private String author;

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = StringUtil.trimToLength(author, 255);
    }

    @Column(nullable = false)
    @ManyToOne
    public Show show;

    @Column(columnDefinition = "TEXT", nullable = true)
    public String description;

    @Column(nullable = true, length = 2000)
    public String link;

    @Column(nullable = false, length = 2000)
    public String audioUrl;

    @Column(nullable = true)
    public Long duration;

    @Column(nullable = true)
    @ManyToMany(cascade = CascadeType.REMOVE)
    public List<Tag> tags = new ArrayList<Tag>();

    public void addTag(Tag tag) {
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    @Column(nullable = true)
    public Date publishedDate;

    @Column(nullable = false)
    public Boolean explicit;

    @Column(nullable = false)
    public Date creationDate;

    @Column(nullable = true)
    public User createdBy;

    public Episode() {
        this("");
    }

    public Episode(String name) {
        this(name, "");
    }

    public Episode(String name, String audioUrl) {
        setName(name);
        this.explicit = false;
        this.audioUrl = audioUrl;
        this.creationDate = new Date();
    }

    public static Finder<Long, Episode> find = new Finder<Long, Episode>(Long.class, Episode.class);

    public static List<Episode> getEpisodes() {
        return find.findList();
    }

    public static Page<Episode> getEpisodes(int networkId, int showId, int genreId, int page, int pageSize, String sortBy, String order, String search) {
        return Episode.getEpisodes((long) networkId, (long) showId, (long) genreId, page, pageSize, sortBy, order, search);
    }

    public static Page<Episode> getEpisodes(Long networkId, Long showId, Long genreId, int page, int pageSize, String sortBy, String order, String search) {

        ExpressionList<Episode> episodeExpressionList = find.where();

        //Network id
        if (networkId != 0) {
            episodeExpressionList = episodeExpressionList.eq("show.network.id", networkId);
        }

        //Show id
        if (showId != 0) {
            episodeExpressionList = episodeExpressionList.eq("show.id", showId);
        }

        //Genre id
        if (genreId != 0) {
            episodeExpressionList = episodeExpressionList.eq("show.genres.id", genreId);
        }

        //Default to "name"
        if (sortBy.isEmpty()) {
            sortBy = "name";
        }

        //Default to "asc"
        if (order.isEmpty()) {
            order = "asc";
        }

        return episodeExpressionList
                .ilike("name", "%" + search + "%")
                .orderBy(sortBy + " " + order)
                .fetch("tags")
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }

    public static int count() {
        return find.findRowCount();
    }

    public static Episode getEpisode(Long id) { return find.byId(id);}

    public static List<Episode> getEpisodesByNetwork(Network network) {
        return Episode.getEpisodesByNetwork(network.id);
    }

    public static List<Episode> getEpisodesByNetwork(Long id) {
        return find.where().eq("show.network.id", id).findList();
    }

    public static List<Episode> getEpisodesByShow(Show show) {
        return Episode.getEpisodesByShow(show.id);
    }

    public static List<Episode> getEpisodesByShow(Long id) {
        return find.where().eq("show.id", id).findList();
    }

    public static List<Episode> getEpisodesByGenre(Genre genre) {
        return find.where().eq("show.genres.id", genre.id).findList();
    }

    public static List<Episode> getEpisodesByTag(Tag tag) {
        return find.where().eq("tags.id", tag.id).findList();
    }

    public static Episode getEpisodeByNameAndShow(String name, Show show) {
        return find.where().eq("show.id", show.id).eq("name", name).findUnique();
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                ", duration=" + duration +
                ", tags=" + tags +
                ", publishedDate=" + publishedDate +
                ", explicit=" + explicit +
                ", creationDate=" + creationDate +
                ", createdBy=" + createdBy +
                '}';
    }
}
