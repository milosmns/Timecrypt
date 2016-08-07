package co.timecrypt.api.v2.database;

import co.timecrypt.api.v2.exceptions.InvalidIdentifierException;
import co.timecrypt.api.v2.servlets.TimecryptApiServlet;

/**
 * The main Timecrypt data store interface, implement this if you need to change how the data communication works.
 */
public interface TimecryptDataStore {

    /**
     * Initialize the data store here. This will generally get called by each servlet instance only once.
     *
     * @param creator The servlet that created this data store, if you need it
     * @throws IllegalStateException If something goes wrong
     */
    void init(TimecryptApiServlet creator) throws IllegalStateException;

    /**
     * Checks whether the Timecrypt message associated with this ID contains a password lock.
     *
     * @param id A unique Timecrypt message ID. Must not be {@code null}
     * @return {@code True} if the message exists, and it contains a password lock; {@code false} if it doesn't contain the lock
     * @throws InvalidIdentifierException if the message with that ID doesn't exist
     */
    boolean checkLock(String id) throws InvalidIdentifierException;

    /**
     * Creates a new Timecrypt message.
     *
     * @param viewCount    How many times is this message allowed to be shown. Must be a positive number starting with 1
     * @param destructDate When to self-destruct the message. Format depends on the data store. Can be {@code null}, defaults to
     *                     <b>tomorrow</b> date
     * @param email        Where to send the invitation to this message. Can be {@code null}, no default
     * @param text         Contents of the message. Must not be {@code null}
     * @param title        Title of the message. Can be {@code null}
     * @param password     Which passphrase to use to encrypt the message. Can be {@code null}, defaults to data store's passphrase
     * @return Message ID of the just-created Timecrypt message, never {@code null}
     */
    String create(int viewCount, String destructDate, String email, String text, String title, String password);

    /**
     * Deallocate all resources here and drop any external connections. This will generally get called by each servlet instance only once.
     */
    void destroy();

}
