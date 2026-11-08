package org.cdc.copy.interfaces;

import com.google.gson.JsonObject;
import org.cdc.copy.accessor.MCreatorAccessor;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public interface IConvertor {
  String getID();

  void convert(JsonObject object, MCreatorAccessor mCreatorAccessor);
}
