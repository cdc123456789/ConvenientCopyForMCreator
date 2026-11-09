package org.cdc.copy.impl.extractor;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import org.cdc.copy.accessor.MCreatorAccessor;
import org.cdc.copy.accessor.MCreatorElementAccessor;
import org.cdc.copy.init.TypeAdapters;
import org.cdc.copy.interfaces.IExtractor;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class SelectedModElementsExtractor implements IExtractor {

  @Override
  public String getID() {
    return "selected_mod_elements";
  }

  @Override
  public void extractFrom(JsonObject jsonObject, MCreatorAccessor mCreatorAccessor) {
    var gson = mCreatorAccessor.getGson();
    List<MCreatorElementAccessor> elements = new ArrayList<>();
    mCreatorAccessor.getSelectedModElements().forEach(a -> {
      if (a.isFolder()) {
        elements.addAll(a.getElementsInFolder());
      } else {
        elements.add(a);
      }
    });
    for (MCreatorElementAccessor selectedModElement : elements) {
      var jsonObject1 = gson.fromJson(selectedModElement.toJSON(), JsonObject.class);
      jsonObject1 = TypeAdapters.getInstance().adapteFrom(jsonObject1, mCreatorAccessor, null);
//      jsonObject1.addProperty("_folder",selectedModElement.getPath());
      jsonObject.add(selectedModElement.getName(), jsonObject1);
    }
  }
}
