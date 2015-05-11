package models.content;

import play.db.ebean.Model;

import javax.persistence.Id;

/**
 * The Queue is a list of episodes for a listener.
 */
public class Queue extends Model {

    @Id
    public Long id;


    //Add based on time sensitivity

    //Add an entire show

    //Add to the bottom

    //Add to the top

    //Clear queue

    //Combine Queues (to the top or to the end?)


}
