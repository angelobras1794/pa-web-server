import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONObject {

    private final List<Map<String, Object>> objectLogs = new ArrayList<>();

    public void put(String key, Object value) {
        if (objectLogs.isEmpty()) {
            objectLogs.add(new HashMap<>());
        }
        objectLogs.get(objectLogs.size() - 1).put(key, value);
    }


    public void newEntry() {
        objectLogs.add(new HashMap<>());
    }

    @Override
    public String toString() {
        StringBuilder json = new StringBuilder("{\n");

        for (Map<String, Object> log : objectLogs) {
            if (!log.isEmpty()) {
                json.append("  ");
                for (Map.Entry<String, Object> entry : log.entrySet()) {
                    json.append("\"").append(entry.getKey()).append("\": ");
                    if (entry.getValue() instanceof String) {
                        json.append("\"").append(entry.getValue()).append("\"");
                    } else {
                        json.append(entry.getValue());
                    }
                    json.append(", ");
                }
                // Remove a última vírgula e espaço extra
                json.delete(json.length() - 2, json.length());
                json.append("\n");
            }
        }
        json.append("}");
        return json.toString();
    }
}
