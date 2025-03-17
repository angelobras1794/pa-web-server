import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JSONObjectTest {

    @Test
    void testJSONObjectToString() {
        JSONObject json = new JSONObject();
        json.put("timestamp", "2025-03-17 14:30:25");
        json.put("method", "GET");
        json.put("route", "/home");
        json.put("origin", "192.168.1.1");
        json.put("HTTP response status", 200);

        String expectedJson = "{\"route\":\"/home\",\"method\":\"GET\",\"origin\":\"192.168.1.1\",\"HTTP response status\":200,\"timestamp\":\"2025-03-17 14:30:25\"}";

        assertEquals(expectedJson, json.toString());
    }

    @Test
    void testJSONObjectEmpty() {
        JSONObject json = new JSONObject();
        assertEquals("{}", json.toString());
    }
}