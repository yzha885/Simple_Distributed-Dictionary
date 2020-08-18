package ServerService;

import org.json.simple.JSONObject;

import java.io.IOException;

public interface Iservice {
    public JSONObject query(String Word);
    public JSONObject add(String Word, String Meaning ) throws IOException;
    public JSONObject delete(String Word) throws IOException;

}
