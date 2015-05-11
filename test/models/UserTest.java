package models;

import models.user.User;
import models.user.UserType;
import org.junit.Test;

import javax.persistence.PersistenceException;
import java.lang.reflect.Field;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

/**
 * Tests the User Class
 */
public class UserTest {

    @Test
    public void testCreate() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                User user = new User("foo@foo.com", "password");
                user.save();
                assertThat(user.id).isNotNull();
                assertThat(user.getEmailAddress()).isEqualTo("foo@foo.com");
                assertThat(user.firstName).isEqualTo(null);
                assertThat(user.lastName).isEqualTo(null);
                assertThat(user.creationDate).isNotNull();
                assertThat(user.userType).isEqualTo(UserType.LISTENER);

                assertThat(User.getUsers().isEmpty()).isFalse();
                assertThat(User.getUsers().size()).isEqualTo(1);
                assertThat(User.getUser(user.id)).isEqualTo(user);

                try {
                    // check the private shaPassword
                    Field field = User.class.getDeclaredField("shaPassword");
                    field.setAccessible(true);
                    assertThat(field.get(user)).isEqualTo(User.getSha512("password"));
                    assertThat(((byte[])field.get(user)).length).isEqualTo(64); // 512 bits = 64 bytes

                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Test
    public void testCreateWithName() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                User user = new User("foo@foo.com", "password", "John", "Doe");
                user.save();
                assertThat(user.id).isNotNull();
                assertThat(user.getEmailAddress()).isEqualTo("foo@foo.com");
                assertThat(user.firstName).isEqualTo("John");
                assertThat(user.lastName).isEqualTo("Doe");
                assertThat(user.creationDate).isNotNull();
                assertThat(user.userType).isEqualTo(UserType.LISTENER);

                assertThat(User.getUsers().isEmpty()).isFalse();
                assertThat(User.getUsers().size()).isEqualTo(1);
                assertThat(User.getUser(user.id)).isEqualTo(user);

                try {
                    // check the private shaPassword
                    Field field = User.class.getDeclaredField("shaPassword");
                    field.setAccessible(true);
                    assertThat(field.get(user)).isEqualTo(User.getSha512("password"));
                    assertThat(((byte[])field.get(user)).length).isEqualTo(64); // 512 bits = 64 bytes

                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Test
    public void testUpdate() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                User user = new User("foo@foo.com", "password", "John", "Doe");
                user.save();

                Long originalId = user.id;

                User newUserInfo = new User("new@foo.com", "updated", "Jane", "Smith");

                User updatedUser = User.updateUser(user.id, newUserInfo);

                assertThat(updatedUser.id).isEqualTo(originalId); //Id should not change
                assertThat(updatedUser.getEmailAddress()).isEqualToIgnoringCase(newUserInfo.getEmailAddress());
                assertThat(updatedUser.getPassword()).isEqualTo(newUserInfo.getPassword());
                assertThat(updatedUser.firstName).isEqualTo(newUserInfo.firstName);
                assertThat(updatedUser.lastName).isEqualTo(newUserInfo.lastName);
                assertThat(updatedUser.zipCode).isEqualTo(newUserInfo.zipCode);
                assertThat(updatedUser.userType).isEqualTo(newUserInfo.userType);
            }
        });
    }

    @Test
    public void testCompleteUpdate() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                User user = new User("foo@foo.com", "password");
                user.save();

                Long originalId = user.id;

                User newUserInfo = new User();
                newUserInfo.firstName = "First";
                newUserInfo.lastName = "Last";

                User updatedUser = User.updateUser(user.id, newUserInfo);

                assertThat(updatedUser.id).isEqualTo(originalId);
                assertThat(updatedUser.firstName).isEqualTo(newUserInfo.firstName);
                assertThat(updatedUser.lastName).isEqualTo(newUserInfo.lastName);
                //Make sure the user's email didn't get modified
                assertThat(updatedUser.getEmailAddress()).isEqualTo("foo@foo.com");

            }
        });
    }

    @Test(expected = PersistenceException.class)
    public void testCreateWithDuplicateEmail() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                User user1 = new User("foo@foo.com", "password", "John", "Doe");
                user1.save();
                User user2 = new User("foo@foo.com", "password", "John", "Doe");
                user2.save();
            }
        });
    }

    @Test(expected = PersistenceException.class)
    public void testCreateWithDuplicateEmailDifferentCase() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                User user1 = new User("foo@foo.com", "password", "John", "Doe");
                user1.save();
                User user2 = new User("FOO@FOO.COM", "password", "John", "Doe");
                user2.save();
            }
        });
    }

    @Test
    public void findAll() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                User user1 = new User("foo@foo.com", "password", "John", "Doe");
                user1.save();

                User user2 = new User("bar@foo.com", "password", "John", "Doe");
                user2.save();

                List<User> users = User.find.all();

                assertThat(users.size()).isEqualTo(2);
            }
        });
    }

    @Test
    public void findByEmailAddressAndPassword() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                User newUser = new User("foo@foo.com", "password", "John", "Doe");
                newUser.save();

                User foundUser = User.findByEmailAddressAndPassword("foo@foo.com", "password");

                assertThat(foundUser).isNotNull();
                assertThat(foundUser.firstName).isEqualTo("John");
                assertThat(foundUser.lastName).isEqualTo("Doe");
            }
        });
    }

    @Test
    public void findByEmailAddressDifferentCaseAndPassword() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                User newUser = new User("foo@foo.com", "password", "John", "Doe");
                newUser.save();

                User foundUser = User.findByEmailAddressAndPassword("FOO@FOO.COM", "password");

                assertThat(foundUser).isNotNull();
                assertThat(foundUser.firstName).isEqualTo("John");
                assertThat(foundUser.lastName).isEqualTo("Doe");
            }
        });
    }

    @Test
    public void findByInvalidEmailAddressAndPassword() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                User newUser = new User("foo@foo.com", "password", "John", "Doe");
                newUser.save();

                User foundUser = User.findByEmailAddressAndPassword("foo@foo.com", "wrong!");

                assertThat(foundUser).isNull();
            }
        });
    }

    @Test
    public void createToken() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                User newUser = new User("foo@foo.com", "password", "John", "Doe");
                newUser.save();
                
                assertThat(newUser.id).isNotNull();
                
                String token = newUser.createToken();
                
                assertThat(token).isNotNull();

                User foundUser = User.findByAuthToken(token);

                assertThat(newUser.id).isEqualTo(foundUser.id);
            }
        });
    }

}
