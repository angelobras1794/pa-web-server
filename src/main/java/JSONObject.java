import java.util.HashMap;
import java.util.Map;

public class JSONObject  {

    private final Map<String, Object> ObjectLog = new HashMap<>();

    public void put(String key, Object value) {
        ObjectLog.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder json = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : ObjectLog.entrySet()) {
            json.append("\"").append(entry.getKey()).append("\":");

            if (entry.getValue() instanceof String) {
                json.append("\"").append(entry.getValue()).append("\"");
            } else {
                json.append(entry.getValue());
            }
            json.append(",");
        }
        // Remove a última vírgula se houver elementos
        if (!ObjectLog.isEmpty()) {
            json.deleteCharAt(json.length() - 1);
        }
        json.append("}");
        return json.toString();
    }
}
