package org.cdc.copy.init;

import com.google.gson.JsonObject;
import java.io.FileNotFoundException;
import java.io.Reader;
import javax.swing.JMenu;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.copy.accessor.MCreatorAccessor;
import org.cdc.copy.accessor.ResourcePointerAccessor;
import org.cdc.copy.interfaces.IExtractor;
import org.cdc.copy.utils.L10Helper;
import org.cdc.copy.utils.ResourceUtils;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class Menus {

  private static final Logger LOG = LogManager.getLogger(Menus.class);

  public static void registerMenus(MCreatorAccessor mcreator) {
    var mainMenu = mcreator.registerMenu("convenient_copy");

    mcreator.newBasicActionBuilder().setName("copy").setActionListener(_ -> {
      var extractor = Extractors.getInstance();
      var dialogs = mcreator.getDialogs();
      var list = dialogs.showMultipleOptionDialog(extractor.getIDs());
      JsonObject result = new JsonObject();
      result.addProperty("_fv", mcreator.getFormatVersion());
      for (String s : list) {
        IExtractor extractor1 = extractor.getExtractor(s);
        var result1 = new JsonObject();
        extractor1.extractFrom(result1, mcreator);
        result.add(s, result1);
      }
      mcreator.setClipBoardText(result);
      dialogs.showWarningDialog("Copied");
    }).buildToJMenu(mainMenu);
    mcreator.newBasicActionBuilder().setName("paste").setActionListener(_ -> {
      JsonObject jsonObject = mcreator.getClipBoardText();
      var dialogs = mcreator.getDialogs();
      if (jsonObject == null) {
        dialogs.showWarningDialog(mcreator.t("error.unkown_format_in_clipboard"));
        return;
      }
      if (jsonObject.has("_fv")) {
        var fv = jsonObject.get("_fv").getAsInt();
        if (mcreator.getFormatVersion() != fv) {
          dialogs.showWarningDialog(
              mcreator.t("error.unmatched_format_version", mcreator.getFormatVersion()));
          return;
        }
      }
      Convertors.getInstance().convert(jsonObject, mcreator);
      dialogs.showWarningDialog("Pasted");
      SwingUtilities.invokeLater(mcreator::reloadWholeWorkspaceTab);
    }).buildToJMenu(mainMenu);

    var templates = new JMenu(mcreator.t(L10Helper.getMenuKey("templates")));
    templates.setName("templates");
    mainMenu.add(templates);

    for (ResourcePointerAccessor accessor : ResourceUtils.getAllCCTemplates()) {
      mcreator.newBasicActionBuilder().setName(accessor.getIdentify())
          .setActionListener(_ -> {
            Reader input = null;
            try {
              input = accessor.getReader();
            } catch (FileNotFoundException e) {
              throw new RuntimeException(e);
            }
            if (input == null) {
              LOG.warn("Can't read template {}", accessor.getPath());
              return;
            }
            var json = mcreator.getGson().fromJson(input, JsonObject.class);
            Convertors.getInstance().convert(json, mcreator);
            SwingUtilities.invokeLater(mcreator::reloadWholeWorkspaceTab);
          }).buildToJMenu(templates);
    }
  }

}
