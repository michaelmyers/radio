package models.content;

/**
 * Description for the show that controls how it is played
 */
public enum ShowType {
    /**
     * News content is typically updated frequently and the most recent is always played.
     * Old news content is never played as the information is time sensitive.
     */
    NEWS,

    /**
     * A series is a story told over the span of several episodes and starts with the oldest
     * and is played in chronological order.
     */
    SERIES,

    /**
     * A weekly show has content that is typically not ephemeral and can be played at anytime.
     * It is still desired to play the most recent however the backlogs can be accessed.
     *
     * TODO: These are not always weekly, think of another name
     */
    WEEKLY
}
