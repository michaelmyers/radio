package models.classification;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TagList {

    @Id
    public Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Tag> tags = new ArrayList<Tag>();

    public void add(Tag tag) {
        this.tags.add(tag);
    }
}
