package org.cdc.copy.init;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.List;
import java.util.ServiceLoader;
import org.cdc.copy.accessor.MCreatorAccessor;
import org.cdc.copy.interfaces.ITypeAdapter;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 * {"_fv":89,"selected_mod_elements":{"UpdateLog":{"_type":"updatelogs","definition":{"logs":["First release"]}}}}
 */
public class TypeAdapters implements ITypeAdapter {

  private static final ServiceLoader<ITypeAdapter> serviceLoader = ServiceLoader.load(
      ITypeAdapter.class, Extractors.class.getClassLoader());

  private static final HashMap<String, ITypeAdapter> idToExtractor = new HashMap<>();

  private static TypeAdapters INSTANCE;

  public static TypeAdapters getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new TypeAdapters();
    }
    return INSTANCE;
  }

  private TypeAdapters() {
  }

  public void init() {
    serviceLoader.forEach(a -> idToExtractor.put(a.getRelatedType(), a));
  }

  public ITypeAdapter getAdapter(String id) {
    return idToExtractor.getOrDefault(id, idToExtractor.getOrDefault("default", null));
  }

  public List<String> getIDs() {
    return idToExtractor.keySet().stream().sorted().toList();
  }

  @Override
  public String getRelatedType() {
    return "general";
  }

  @Override
  public JsonObject adapteFrom(JsonObject jsonObject, MCreatorAccessor mCreatorAccessor,
      String type1) {
    if (jsonObject == null) {
      return null;
    }
    var type = jsonObject.get("_type").getAsString();
    var typeAdapter = getAdapter(type);
    var adapted = typeAdapter.adapteFrom(jsonObject.getAsJsonObject("definition"), mCreatorAccessor,
        type);
    jsonObject.add("definition", adapted);
    jsonObject.remove("_fv");
    return jsonObject;
  }

  @Override
  public JsonObject restoreFrom(JsonObject jsonObject, MCreatorAccessor mCreatorAccessor,
      String type1) {
    if (jsonObject == null) {
      return null;
    }
    var type = jsonObject.get("_type").getAsString();
    jsonObject.addProperty("_fv", mCreatorAccessor.getFormatVersion());
    var typeAdapter = getAdapter(type);
    var value1 = typeAdapter.restoreFrom(jsonObject.getAsJsonObject("definition"), mCreatorAccessor,
        type);
    jsonObject.add("definition", value1);
    return jsonObject;
  }
}
