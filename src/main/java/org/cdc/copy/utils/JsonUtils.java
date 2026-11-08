package org.cdc.copy.utils;

import com.google.gson.JsonObject;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class JsonUtils {

  public static JsonObject deduplicate(JsonObject target, JsonObject template) {
    var result = target.deepCopy();
    for (String s : template.keySet()) {
      if (target.has(s)) {
        if (target.get(s).equals(template.get(s))) {
          result.remove(s);
        }
      }
    }
    return result;
  }

  public static JsonObject complete(JsonObject target, JsonObject template) {
    var result = target.deepCopy();
    for (String s : template.keySet()) {
      if (!target.has(s)) {
        result.add(s, template.get(s));
      }
    }
    return result;
  }
}
