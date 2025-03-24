import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The `PageHandler` class manages access to server documents, ensuring that only one thread can access a specific document at a time.
 * It uses a `ConcurrentHashMap` to store locks for each document and `ReentrantLock` with fairness set to true to ensure First-Come, First-Served (FCFS) order.
 */
public class PageHandler {
    private final ConcurrentHashMap<String, ReentrantLock> lockConcurrentHashMap;

    /**
     * Constructs a `PageHandler` object and initializes the lock map.
     */
    public PageHandler() {
        lockConcurrentHashMap = new ConcurrentHashMap<>();
    }

    /**
     * Creates an entry for the specified page in the lock map if it does not already exist.
     *
     * @param page The page for which to create an entry.
     */
    public void createEntry(String page) {
        lockConcurrentHashMap.putIfAbsent(page, new ReentrantLock(true));
    }

    /**
     * Locks access to the specified page, ensuring that only one thread can access it at a time.
     *
     * @param page The page to lock.
     */
    public void lockAccessPage(String page) {
        if(page.endsWith("404.html")){return;}
        createEntry(page);
        lockConcurrentHashMap.get(page).lock();
        System.out.println("Page locked: " + page);
    }

    /**
     * Unlocks access to the specified page,
     * allowing other threads to access it.
     *
     * @param page The page to unlock.
     */
    public void unlockAccessPage(String page) {
        if(page.endsWith("404.html")){return;}
        lockConcurrentHashMap.get(page).unlock();
        System.out.println("Page unlocked: " + page);
    }
    /**
     * Returns the lock map for testing purposes.
     *
     */
    public ConcurrentHashMap<String, ReentrantLock> getLockConcurrentHashMap() {
        return lockConcurrentHashMap;
    }
}