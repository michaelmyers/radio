package models.content;

import play.db.ebean.Model;

import javax.persistence.Id;
import javax.xml.datatype.Duration;

/**
 * Listener Statistics for a publisher, show, or episode
 */
public class ListenerStatistics extends Model {

    @Id
    public Long id;


    /**
     * Play Count is incremented if more than ten seconds are listened to.
     */
    public Long playCount;

    /**
     * Completed Play count is incremented if more than 7/10s of the content is listened to
     *
     */
    public Long completedPlayCount;

    public Duration averageListenDuration;

}
