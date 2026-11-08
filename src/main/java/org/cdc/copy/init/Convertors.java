package org.cdc.copy.init;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.copy.accessor.MCreatorAccessor;
import org.cdc.copy.interfaces.IConvertor;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class Convertors implements IConvertor {

  private static final ServiceLoader<IConvertor> serviceLoader = ServiceLoader.load(IConvertor.class,
      Convertors.class.getClassLoader());

  private static final HashMap<String, IConvertor> idToDecoder = new HashMap<>();

  private static Convertors INSTANCE;
  private static final Logger LOGGER = LogManager.getLogger(Convertors.class);

  public static Convertors getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Convertors();
    }
    return INSTANCE;
  }

  private Convertors() {

  }

  public void init() {
    serviceLoader.forEach(a -> idToDecoder.put(a.getID(), a));
  }

  public IConvertor getDecoder(String id) {
    return idToDecoder.get(id);
  }

  public List<String> getIDs() {
    return idToDecoder.keySet().stream().sorted().toList();
  }

  @Override
  public String getID() {
    return "general";
  }

  @Override
  public void convert(JsonObject jsonObject, MCreatorAccessor mCreatorAccessor) {
    if (jsonObject == null) {
      return;
    }
    for (Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
      if (idToDecoder.containsKey(stringJsonElementEntry.getKey())) {
        var decoder = getDecoder(stringJsonElementEntry.getKey());
        if (stringJsonElementEntry.getValue().isJsonObject()) {
          decoder.convert(stringJsonElementEntry.getValue().getAsJsonObject(), mCreatorAccessor);
        }
      } else {
        LOGGER.warn("Unknown decoder: {}", stringJsonElementEntry.getKey());
      }
    }
  }
}
