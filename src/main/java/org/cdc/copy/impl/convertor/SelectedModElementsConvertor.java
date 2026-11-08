package org.cdc.copy.impl.convertor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import java.util.Map.Entry;
import org.cdc.copy.accessor.MCreatorAccessor;
import org.cdc.copy.init.TypeAdapters;
import org.cdc.copy.interfaces.IConvertor;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class SelectedModElementsConvertor implements IConvertor {

  @Override
  public String getID() {
    return "selected_mod_elements";
  }

  @Override
  public void convert(JsonObject object, MCreatorAccessor mCreatorAccessor) {
    for (Entry<String, JsonElement> stringJsonElementEntry : object.entrySet()) {
      var value = stringJsonElementEntry.getValue();
      if (value.isJsonObject()) {
        var value1 = value.getAsJsonObject();
        value1 = TypeAdapters.getInstance().restoreFrom(value1, mCreatorAccessor, null);
        mCreatorAccessor.convertJsonAndAddElement(stringJsonElementEntry.getKey(), value1);
      }
    }
  }
}
