package models.classification;

import models.content.Episode;
import models.content.Show;
import play.Logger;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Channel extends Model {

    @Id
    @Column(name = "CHANNEL_ID")
    public Long id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public Date creationDate;

    @Column(nullable = true)
    @ManyToMany(cascade = CascadeType.REMOVE)
    private List<Show> shows = new ArrayList<Show>();

    public void addShow(Show show) {
        if (!this.shows.contains(show)) {
            this.shows.add(show);
        }
    }

    public List<Show> getShows() {
        return this.shows;
    }

    @Column(nullable = true)
    @ManyToOne
    public Genre genre;

    public List<Episode> getPlayList() {

        ArrayList<Episode> episodes = new ArrayList<Episode>();

        for (Show show : getShows()) {
            episodes.addAll(Episode.getEpisodes((long) 0, show.id, (long) 0, 0, 20, "publishedDate", "desc", "").getList());
        }

        //Get the episodes associated with the Genre
        episodes.addAll(Episode.getEpisodes((long) 0, (long) 0, genre.id, 0, 20, "publishedDate", "desc", "").getList());

        //sort by news published date
        Collections.sort(episodes, new Comparator<Episode>() {
            public int compare(Episode episode1, Episode episode2) {
                return episode1.publishedDate.compareTo(episode2.publishedDate);
            }
        });

        return episodes;

    }

    public Channel() {
        this("New Channel", null, null);
    }

    public Channel(String name, Genre genre) {
        this(name, genre, null);
    }

    public Channel(String name, List<Show> shows) {
        this(name, null, shows);
    }

    public Channel(String name, Genre genre, List<Show> shows) {
        this.name = name;
        this.genre = genre;

        if (shows != null) {
            this.shows = shows;
        }

        this.creationDate = new Date();
    }

    public static Finder<Long, Channel> find = new Finder<Long, Channel>(Long.class, Channel.class);

    public static List<Channel> getChannels() {
        return find.all();
    }

    public static int getNumberOfDistinctChannels() {
        return find.findRowCount();
    }

    public static Channel getChannel(Long id) {
        return find.byId(id);
    }
}
