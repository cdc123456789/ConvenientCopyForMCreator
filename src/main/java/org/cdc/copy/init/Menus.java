package org.cdc.copy.init;

import com.google.gson.JsonObject;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.Reader;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.copy.accessor.MCreatorAccessor;
import org.cdc.copy.accessor.ResourcePointerAccessor;
import org.cdc.copy.interfaces.IExtractor;
import org.cdc.copy.ui.ProgressDialog;
import org.cdc.copy.utils.JsonUtils;
import org.cdc.copy.utils.L10Helper;
import org.cdc.copy.utils.ResourceUtils;
import org.cdc.copy.utils.Workers;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class Menus {

  private static final Logger LOG = LogManager.getLogger(Menus.class);

  public static void registerMenus(MCreatorAccessor mcreator) {
    var mainMenu = mcreator.registerMenu("convenient_copy");

    mcreator.newBasicActionBuilder().setName("copy").setActionListener(_ -> {
      var progressDialog = new ProgressDialog(mcreator,"Copying");
      var extractor = Extractors.getInstance();
      var dialogs = mcreator.getDialogs();
      var list = dialogs.showMultipleOptionDialog(extractor.getIDs());
      extractor.setManager(a->{
        progressDialog.markFinish();
        var result = list.contains(a);
        if (result) {
          progressDialog.addProgressUnit(a);
        }
        return result;
      });
      Workers.getInstance().executeCopyOrPaste(()->{
        JsonObject result = JsonUtils.createDefaultCopiedJson(mcreator);
        Extractors.getInstance().extractFrom(result,mcreator);
        progressDialog.markFinish();
        progressDialog.hide();
        mcreator.setClipBoardText(result);
        dialogs.showWarningDialog("Copied");
      });
      progressDialog.show();
    }).buildToJMenu(mainMenu);
    mcreator.newBasicActionBuilder().setName("paste").setActionListener(_ -> {
      var progressDialog = new ProgressDialog(mcreator,"Pasting");
      JsonObject jsonObject = mcreator.getClipBoardText();
      var dialogs = mcreator.getDialogs();
      var error = JsonUtils.validateCopiedJson(jsonObject, mcreator);
      var convertor = Convertors.getInstance();
      if (error != null) {
        dialogs.showWarningDialog(mcreator.t("error." + error));
        return;
      }
      convertor.setManager(a->{
        progressDialog.markFinish();
        progressDialog.addProgressUnit(a);
        return true;
      });
      Workers.getInstance().executeCopyOrPaste(()->{
        convertor.convert(jsonObject, mcreator);
        progressDialog.markFinish();
        progressDialog.hide();
      });
      progressDialog.show();
      dialogs.showWarningDialog("Pasted");
      SwingUtilities.invokeLater(mcreator::reloadWholeWorkspaceTab);
    }).buildToJMenu(mainMenu);

    var templates = L10Helper.menu("templates");
    mainMenu.add(templates);

    mainMenu.addMenuListener(new MenuListener() {
      @Override
      public void menuSelected(MenuEvent ignored) {
        templates.removeAll();
        SwingUtilities.invokeLater(()->{
          for (ResourcePointerAccessor accessor : ResourceUtils.getAllCCTemplates()) {
            ActionListener actionListener = _ -> {
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
            };
            if (accessor.isInClassPath()) {
              mcreator.newBasicActionBuilder().setName(accessor.getIdentify())
                  .setActionListener(actionListener).buildToJMenu(templates);
            } else {
              mcreator.newBasicActionBuilder().markDynamic().setName(accessor.getIdentify())
                  .setActionListener(actionListener).buildToJMenu(templates);
            }
          }
        });
      }

      @Override
      public void menuDeselected(MenuEvent e) {

      }

      @Override
      public void menuCanceled(MenuEvent e) {

      }
    });
  }

}
