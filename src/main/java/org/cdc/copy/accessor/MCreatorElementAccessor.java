package org.cdc.copy.accessor;

import java.util.ArrayList;
import java.util.List;
import net.mcreator.element.ModElementType;
import net.mcreator.ui.MCreator;
import net.mcreator.workspace.elements.FolderElement;
import net.mcreator.workspace.elements.IElement;
import net.mcreator.workspace.elements.ModElement;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class MCreatorElementAccessor {

  private final IElement element;
  private final MCreator mCreator;

  public MCreatorElementAccessor(IElement element, MCreator mCreator) {
    this.element = element;
    this.mCreator = mCreator;
  }

  public String toJSON() {
    if (element instanceof ModElement modElement) {
      return mCreator.getModElementManager()
          .generatableElementToJSON(modElement.getGeneratableElement());
    }
    return "{}";
  }

  public String getName() {
    return element.getName();
  }

  public boolean isFolder() {
    return element instanceof FolderElement;
  }

  public List<MCreatorElementAccessor> getElementsInFolder() {
    if (element instanceof FolderElement folder) {
      List<MCreatorElementAccessor> list = new ArrayList<>();
      mCreator.getWorkspace().getModElements().forEach(a -> {
        if (folder.getPath().equals(a.getFolderPath())) {
          list.add(new MCreatorElementAccessor(a, mCreator));
        }
      });
      return list;
    }
    return List.of();
  }

  public ModElementType<?> getType() {
    if (element instanceof ModElement modElement) {
      return modElement.getType();
    }
    return null;
  }

  public String getPath(){
    if (element instanceof ModElement modElement) {
      return modElement.getFolderPath();
    }
    return null;
  }
}
