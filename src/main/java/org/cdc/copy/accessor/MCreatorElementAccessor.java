package org.cdc.copy.accessor;

import net.mcreator.element.ModElementType;
import net.mcreator.ui.MCreator;
import net.mcreator.workspace.elements.IElement;
import net.mcreator.workspace.elements.ModElement;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class MCreatorElementAccessor {

  private IElement element;
  private MCreator mCreator;

  public MCreatorElementAccessor(IElement element, MCreator mCreator) {
    this.element = element;
    this.mCreator = mCreator;
  }

  public String toJSON() {
    if (element instanceof ModElement modElement) {
      return mCreator.getModElementManager().generatableElementToJSON(modElement.getGeneratableElement());
    }
    return "";
  }

  public String getName(){
    return element.getName();
  }

  public ModElementType<?> getType(){
    if (element instanceof ModElement modElement){
      return modElement.getType();
    }
    return null;
  }
}
