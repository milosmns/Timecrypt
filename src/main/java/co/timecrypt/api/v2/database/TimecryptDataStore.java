
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
     * @param id A unique Timecrypt message ID
     * @return {@code True} if the message exists, and it contains a password lock; {@code false} if it doesn't contain the lock
     * @throws InvalidIdentifierException if the message with that ID doesn't exist
     */
    boolean checkLock(String id) throws InvalidIdentifierException;

    /**
     * Deallocate all resources here and drop any external connections. This will generally get called by each servlet instance only once.
     */
    void destroy();

}
