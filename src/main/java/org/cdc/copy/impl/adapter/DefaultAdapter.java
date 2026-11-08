package org.cdc.copy.impl.adapter;

import com.google.gson.JsonObject;
import org.cdc.copy.accessor.MCreatorAccessor;
import org.cdc.copy.interfaces.ITypeAdapter;
import org.cdc.copy.utils.JsonUtils;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class DefaultAdapter implements ITypeAdapter {

  @Override
  public String getRelatedType() {
    return "default";
  }

  @Override
  public JsonObject adapteFrom(JsonObject jsonObject, MCreatorAccessor mCreatorAccessor,String type) {
    var empty = mCreatorAccessor.getEmptyElementJsonObject(type);
    return JsonUtils.deduplicate(jsonObject,empty);
  }

  @Override
  public JsonObject restoreFrom(JsonObject jsonObject, MCreatorAccessor mCreatorAccessor,String type) {
    var empty = mCreatorAccessor.getEmptyElementJsonObject(type);
    return JsonUtils.complete(jsonObject,empty);
  }
}
