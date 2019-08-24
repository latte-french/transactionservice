package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FromJsonToObjectConverter {
    private static JsonParser parser = new JsonParser();

    public static JsonObject convertFromJsonToObject (String response) {
        System.out.println(response);
        return (JsonObject) parser.parse(response);
    }
}
