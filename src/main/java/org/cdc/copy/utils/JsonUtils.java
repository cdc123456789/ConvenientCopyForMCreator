package org.cdc.copy.utils;

import com.google.gson.JsonObject;
import org.cdc.copy.accessor.MCreatorAccessor;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class JsonUtils {

  public static JsonObject createDefaultCopiedJson(MCreatorAccessor mcreator) {
    var result = new JsonObject();
    result.addProperty("_fv", mcreator.getFormatVersion());
    return result;
  }

  public static String validateCopiedJson(JsonObject jsonObject,MCreatorAccessor mcreator){
    if (jsonObject == null) {
      return "unkown_format_in_clipboard";
    }
    if (jsonObject.has("_fv")) {
      var fv = jsonObject.get("_fv").getAsInt();
      if (mcreator.getFormatVersion() < fv) {
        return "unmatched_format_version";
      }
    }
    return null;
  }

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
