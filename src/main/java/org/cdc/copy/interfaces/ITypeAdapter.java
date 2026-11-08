package org.cdc.copy.interfaces;

import com.google.gson.JsonObject;
import org.cdc.copy.accessor.MCreatorAccessor;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public interface ITypeAdapter {

  String getRelatedType();

  /**
   * This will be called by the Extractor
   */
  JsonObject adapteFrom(JsonObject jsonObject, MCreatorAccessor mCreatorAccessor,
      String typeString);

  /**
   * This will be called by the convertor
   */
  JsonObject restoreFrom(JsonObject jsonObject, MCreatorAccessor mCreatorAccessor,
      String typeString);
}
