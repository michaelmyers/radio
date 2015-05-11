package models.user;

/**
 * User type describes the level of access a user has
 * within the application
 */
public enum UserType {
    /**
     * The most common level (as well as the default level for a new user).
     */
    LISTENER,

    /**
     * Admins have the privilege of viewing and disabling, but not deleting
     */
    ADMIN,

    /**
     * Only a super user can create new admins and have the power to delete.
     */
    SUPER_USER;
}

