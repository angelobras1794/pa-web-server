import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JSONObjectTest {

    @Test
    void testPut_AddsKeyValuePair() {
        JSONObject json = new JSONObject();
        json.put("key1", "value1");
        json.put("key2", 123);

        String expectedJson = "{\n  \"key1\": \"value1\", \"key2\": 123\n}";
        assertEquals(expectedJson, json.toString());
    }

    @Test
    void testNewEntry_CreatesNewJSONObjectEntry() {
        JSONObject json = new JSONObject();
        json.put("key1", "value1");
        json.newEntry();
        json.put("key2", 123);

        String expectedJson = "{\n  \"key1\": \"value1\"\n  \"key2\": 123\n}";
        assertEquals(expectedJson, json.toString());
    }

    @Test
    void testToString_EmptyJSONObject() {
        JSONObject json = new JSONObject();
        assertEquals("{\n}", json.toString());
    }

    @Test
    void testToString_MultipleEntries() {
        JSONObject json = new JSONObject();
        json.put("key1", "value1");
        json.newEntry();
        json.put("key2", 123);
        json.newEntry();
        json.put("key3", true);

        String expectedJson = "{\n  \"key1\": \"value1\"\n  \"key2\": 123\n  \"key3\": true\n}";
        assertEquals(expectedJson, json.toString());
    }
}