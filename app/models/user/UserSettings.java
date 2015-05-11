package models.user;


import play.db.ebean.Model;

import javax.persistence.Id;

public class UserSettings extends Model {

    @Id
    public Long id;

    public Boolean conserveData;

    public Boolean autoPlay;

    public Boolean notifications;

    public Boolean playNewsWhenAvailable;

}
