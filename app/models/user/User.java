package models.user;

import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import models.classification.Channel;
import models.content.Episode;
import models.SecurityRole;
import models.Status;
import models.content.EpisodeList;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class User extends Model implements Subject {

    //Define some string constants for JSON keys
    public static final String USER_KEY_JSON = "user";
    public static final String PASSWORD_KEY_JSON = "password";

    @Id
    public Long id;

    @Column(length = 256, unique = true, nullable = false)
    @Constraints.MaxLength(256)
    @Constraints.Required
    @Constraints.Email
    private String emailAddress;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress.toLowerCase();
    }

    @Column(length = 64, nullable = false)
    private byte[] shaPassword;

    @Transient
    @Constraints.Required
    @Constraints.MinLength(6)
    @Constraints.MaxLength(256)
    @JsonIgnore
    private String password;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
        shaPassword = getSha512(password);
    }

    @Column(length = 256, nullable = true)
    @Constraints.MinLength(2)
    @Constraints.MaxLength(255)
    public String firstName;

    @Column(length = 256, nullable = true)
    @Constraints.MinLength(2)
    @Constraints.MaxLength(255)
    public String lastName;

    @Column(nullable = true)
    public Long zipCode;

    @Column(nullable = false)
    public Date creationDate;

    @Column(nullable = false)
    public UserType userType;

    @Column(nullable = false)
    public Status status;

    @Column(nullable = true)
    public List<Episode> queue; //TODO: rename this, make it an object

    @Column(nullable = true)
    public EpisodeList history;

    //TODO: make these next two into an object, Placeholder, maybe Update
    @Column(nullable = true)
    public Episode currentEpisode;

    @Column(nullable = true)
    public Channel currentChannel;

    /**
     * Private authToken used for either a cookie or token to use the API.
     *   This only exists when the user has signed in or requested a session.
     */
    private String authToken;

    /**
     * Creates a token for the user and saves it
     *
     * @return The authorization token
     */
    public String createToken() {
        authToken = UUID.randomUUID().toString();
        save();
        return authToken;
    }

    /**
     * Deletes the auth token for the use and saves it
     */
    public void deleteAuthToken() {
        authToken = null;
        save();
    }

    @Column(nullable = true)
    @JsonIgnore
    public List<SecurityRole> roles;

    @Override
    public List<? extends Role> getRoles()
    {
        return roles;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return null;
    }

    @JsonIgnore
    private String identifier;

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @ManyToMany(mappedBy = "users")
    private List<UserList> publisherAdminLists;

    /**
     * The Default Constructor
     */
    public User() {
        this("", "", null, null);
    }

    /**
     * The Most Likely Constructor
     *
     * @param emailAddress
     * @param password
     */
    public User(String emailAddress, String password) {
        this(emailAddress, password, null, null);
    }

    /**
     * The Unlikely Constructor
     *
     * @param emailAddress
     * @param password
     * @param firstName
     * @param lastName
     */
    public User(String emailAddress, String password, String firstName, String lastName) {
        setEmailAddress(emailAddress);
        setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.creationDate = new Date();
        this.userType = UserType.LISTENER;
        this.status = Status.ACTIVE;
        this.identifier = null;
    }

    /**
     * Encrypts the provided text to SHA-512, used for the user's password.
     * @param value String to be encrypted
     * @return Encrypted string
     */
    public static byte[] getSha512(String value) {
        try {
            return MessageDigest.getInstance("SHA-512").digest(value.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Finder<Long, User> find = new Finder<Long, User>(Long.class, User.class);

    public static List<User> getUsers() {
        return find.all();
    }

    public static User getUser(Long id) {
        return find.byId(id);
    }

    public static User updateUser(Long id, User user) {

        User existingUser = User.getUser(id);

        if (existingUser == null) {
            return null;
        }

        if (!user.emailAddress.isEmpty()) {
            existingUser.setEmailAddress(user.emailAddress);
        }

        if (!user.password.isEmpty()) {
            existingUser.setPassword(user.password);
        }

        if (!user.firstName.isEmpty()) {
            existingUser.firstName = user.firstName;
        }

        if (!user.lastName.isEmpty()) {
            existingUser.lastName = user.lastName;
        }

        if (user.zipCode != null) {
            existingUser.zipCode = user.zipCode;
        }

        if (user.userType != null) {
            existingUser.userType = user.userType;
        }

        existingUser.save();

        return existingUser;
    }

    public static User disableUser(Long id) {

        User disabledUser = User.getUser(id);

        if (disabledUser == null) {
            return null;
        }

        disabledUser.status = Status.DISABLED;
        disabledUser.save();

        return disabledUser;
    }

    public static void deleteUser(Long id) {
        User.getUser(id).delete();
    }

    public static User findByAuthToken(String authToken) {
        if (authToken == null) {
            return null;
        }

        try  {
            return find.where().eq("authToken", authToken).findUnique();
        } catch (Exception e) {
            return null;
        }
    }

    public static User findByEmailAddressAndPassword(String emailAddress, String password) {
        // todo: verify this query is correct.  Does it need an "and" statement?
        return find.where().eq("emailAddress", emailAddress.toLowerCase()).eq("shaPassword", getSha512(password)).findUnique();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", emailAddress='" + emailAddress + '\'' +
                //", shaPassword=" + Arrays.toString(shaPassword) +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", zipCode=" + zipCode +
                ", creationDate=" + creationDate +
                ", userType=" + userType +
                ", status=" + status +
                ", queue=" + queue +
                ", currentEpisode=" + currentEpisode +
                ", currentChannel=" + currentChannel +
                ", authToken='" + authToken + '\'' +
                ", roles=" + roles +
                ", identifier='" + identifier + '\'' +
                '}';
    }
}
