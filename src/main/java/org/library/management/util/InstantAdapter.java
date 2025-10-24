package org.library.management.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;

/**
 * Author: Artyom Aroyan
 * Date: 24.10.25
 * Time: 22:35:31
 */
public class InstantAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Instant.parse(json.getAsString());
    }
}
//public class InstantAdapter extends TypeAdapter<Instant> {
//    @Override
//    public void write(JsonWriter out, Instant value) throws IOException {
//        if (value == null) {
//            out.nullValue();
//        } else {
//            out.value(value.toEpochMilli());
//        }
//    }
//
//    @Override
//    public Instant read(JsonReader in) throws IOException {
//        if (in.peek() == null) {
//            return null;
//        }
//        return Instant.ofEpochMilli(in.nextLong());
//    }
//}