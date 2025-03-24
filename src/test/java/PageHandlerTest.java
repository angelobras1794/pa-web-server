import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;

class PageHandlerTest {
    private PageHandler pageHandler;

    @BeforeEach
    void setUp() {
        pageHandler = new PageHandler();
    }

    @Test
    void testCreateEntry() {
        String page = "testPage.html";
        pageHandler.createEntry(page);
        ConcurrentHashMap<String, ReentrantLock> lockMap = pageHandler.getLockConcurrentHashMap();
        assertTrue(lockMap.containsKey(page));
    }

    @Test
    void testLockAccessPage() {
        String page = "testPage.html";
        pageHandler.lockAccessPage(page);
        ConcurrentHashMap<String, ReentrantLock> lockMap = pageHandler.getLockConcurrentHashMap();
        assertTrue(lockMap.get(page).isLocked());
    }

    @Test
    void testUnlockAccessPage() {
        String page = "testPage.html";
        pageHandler.lockAccessPage(page);
        pageHandler.unlockAccessPage(page);
        ConcurrentHashMap<String, ReentrantLock> lockMap = pageHandler.getLockConcurrentHashMap();
        assertFalse(lockMap.get(page).isLocked());
    }

    @Test
    void testLockAccessPage404() {
        String page = "404.html";
        pageHandler.lockAccessPage(page);
        ConcurrentHashMap<String, ReentrantLock> lockMap = pageHandler.getLockConcurrentHashMap();
        System.out.println(lockMap);
        assertFalse(lockMap.containsKey(page));
    }

    @Test
    void testUnlockAccessPage404() {
        String page = "404.html";
        pageHandler.unlockAccessPage(page);
        ConcurrentHashMap<String, ReentrantLock> lockMap = pageHandler.getLockConcurrentHashMap();
        assertFalse(lockMap.containsKey(page));
    }
}