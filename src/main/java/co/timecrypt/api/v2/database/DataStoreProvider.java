
package co.timecrypt.api.v2.database;

import co.timecrypt.api.v2.servlets.TimecryptApiServlet;

import java.util.HashMap;
import java.util.Map;

/**
 * This class should handle creation of {@link TimecryptDatastore}(s). If you need a more complex implementation, override that here.
 */
public class DataStoreProvider {

    private static Map<String, TimecryptDatastore> storeCache = new HashMap<>();

    /**
     * This gets called whenever a servlet is initialized. Do some complex pool logic or caching here if you need to.
     * 
     * @param servlet Which servlet was just initialized
     */
    public static void onServletInitialized(TimecryptApiServlet servlet) {
        TimecryptDatastore dataStore = new SimplePostgresqlDatabase();
        try {
            dataStore.init(servlet);
        } catch (com.sun.media.sound.InvalidDataException e) {
            e.printStackTrace();
        }
        storeCache.put(servlet.getClass().getSimpleName(), dataStore);
    }

    /**
     * This method should create and return the data store for the given servlet. If you have any complex caching logic, use it now.
     * 
     * @param servlet Which servlet needs the data store
     * @return The data store instance. If you return {@code null}, everything will crash
     */
    public static TimecryptDatastore getDataStore(TimecryptApiServlet servlet) {
        return storeCache.get(servlet.getClass().getSimpleName());
    }

    /**
     * This gets called whenever a servlet becomes destroyed. Do some pool cleanup logic or un-caching here if you need to.
     * 
     * @param servlet Which servlet was just destroyed
     */
    public static void onServletDestroyed(TimecryptApiServlet servlet) {
        TimecryptDatastore dataStore = storeCache.get(servlet.getClass().getSimpleName());
        dataStore.destroy();
        storeCache.remove(servlet.getClass().getSimpleName());
    }

}
