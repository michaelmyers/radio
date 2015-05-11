package models.content;

import models.user.User;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.*;

@Entity
public class ShowList extends Model {

    @Id
    public Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Show> shows = new ArrayList<Show>();

    public void add(Show show) {
        this.shows.add(show);
    }

    public void remove(Show show) {
        this.shows.remove(show);
    }

    public Boolean contains(Show show) {
        return this.shows.contains(show);
    }

    //Other possible methods
    //public sortByHighestRated, mostListenedTo
    //public getTotalLength
    //public getAverageRat    ng

}
