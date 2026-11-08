package org.cdc.copy.accessor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;
import javax.swing.JMenu;
import net.mcreator.element.GeneratableElement;
import net.mcreator.element.ModElementType;
import net.mcreator.element.ModElementTypeLoader;
import net.mcreator.ui.MCreator;
import net.mcreator.ui.init.L10N;
import net.mcreator.ui.workspace.WorkspacePanel;
import net.mcreator.workspace.elements.ModElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.copy.builder.BasicActionBuilder;
import org.cdc.copy.ui.Dialogs;
import org.cdc.copy.utils.L10Helper;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class MCreatorAccessor {

  private final static Logger LOGGER = LogManager.getLogger(MCreatorAccessor.class);

  private final MCreator mCreator;
  private final Gson gson;
  private final Dialogs dialogs;

  public MCreatorAccessor(MCreator mCreator) {
    this.mCreator = mCreator;
    this.gson = new Gson();
    this.dialogs = new Dialogs(this);
  }

  public JMenu registerMenu(String menuIdentify) {
    JMenu menu = new JMenu(L10N.t(L10Helper.getMenuKey(menuIdentify)));
    menu.setName(menuIdentify);
    mCreator.getMainMenuBar().add(menu);
    return menu;
  }

  public BasicActionBuilder newBasicActionBuilder() {
    return new BasicActionBuilder(mCreator.getActionRegistry());
  }

  public String t(String localKey, Object... args) {
    return L10N.t(localKey, args);
  }

  public Component getMCreator() {
    return mCreator;
  }

  public void setClipBoardText(JsonObject result) {
    StringSelection stringSelection = new StringSelection(result.toString());
    mCreator.getToolkit().getSystemClipboard().setContents(stringSelection, stringSelection);
  }

  public JsonObject getClipBoardText() {
    try {
      var content = mCreator.getToolkit().getSystemClipboard().getContents(null)
          .getTransferData(DataFlavor.stringFlavor)
          .toString();
      return gson.fromJson(content, JsonObject.class);
    } catch (UnsupportedFlavorException | IOException e) {
      throw new RuntimeException(e);
    } catch (JsonSyntaxException syntaxException) {
      return null;
    }
  }

  public Gson getGson() {
    return gson;
  }

  public Dialogs getDialogs() {
    return dialogs;
  }

  public List<MCreatorElementAccessor> getSelectedModElements() {
    if (mCreator.getWorkspacePanel() instanceof WorkspacePanel workspacePanel) {
      return workspacePanel.list.getSelectedValuesList().stream()
          .map(a -> new MCreatorElementAccessor(a, mCreator)).toList();
    }
    return List.of();
  }

  public boolean supportType(ModElementType<?> elementType) {
    return mCreator.getGeneratorStats().getSupportedModElementTypes().contains(elementType);
  }

  public void convertJsonAndAddElement(String name, JsonObject jsonObject) {
    if (jsonObject.has("_type")) {
      var type = ModElementTypeLoader.getModElementType(jsonObject.get("_type").getAsString());
      if (type != null && supportType(type)) {
        var modElement = new ModElement(mCreator.getWorkspace(), name, type);
        var element = mCreator.getModElementManager()
            .fromJSONtoGeneratableElementOrNull(jsonObject.toString(), modElement);
        if (element != null) {
          element.setModElement(modElement);
          mCreator.getModElementManager().invalidateCache();
          mCreator.getModElementManager().storeModElement(element);
          mCreator.getWorkspace().addModElement(modElement);
        }
      } else {
        if (type != null) {
          LOGGER.warn("Unsupported element type: {}", type.getRegistryName());
        }
      }
    }
  }

  public void reloadWholeWorkspaceTab(){
    mCreator.getWorkspacePanel().reloadWorkspaceTab();
  }

  public int getFormatVersion() {
    return GeneratableElement.formatVersion;
  }

  public JsonObject getEmptyElementJsonObject(String type) {
    var type1 = ModElementTypeLoader.getModElementType(type);
    var generable = type1.getModElementGUI(mCreator,
        new ModElement(mCreator.getWorkspace(), "empty", type1), false).getElementFromGUI();
    return gson.fromJson(mCreator.getModElementManager().generatableElementToJSON(generable),
        JsonObject.class).getAsJsonObject("definition");
  }
}
