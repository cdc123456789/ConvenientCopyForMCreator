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
import org.cdc.copy.utils.JsonUtils;
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
            extractor.setManager(list::contains);
            JsonObject result = JsonUtils.createDefaultCopiedJson(mcreator);
            Extractors.getInstance().extractFrom(result, mcreator);
            mcreator.setClipBoardText(result);
            dialogs.showWarningDialog("Copied");
        }).buildToJMenu(mainMenu);
        mcreator.newBasicActionBuilder().setName("paste").setActionListener(_ -> {
            JsonObject jsonObject = mcreator.getClipBoardText();
            var dialogs = mcreator.getDialogs();
            var error = JsonUtils.validateCopiedJson(jsonObject, mcreator);
            var convertor = Convertors.getInstance();
            if (error != null) {
                dialogs.showWarningDialog(mcreator.t("error." + error));
                return;
            }
            convertor.convert(jsonObject, mcreator);
            dialogs.showWarningDialog("Pasted");
            SwingUtilities.invokeLater(mcreator::reloadWholeWorkspaceTab);
        }).buildToJMenu(mainMenu);

        var templates = L10Helper.menu("templates");
        mainMenu.add(templates);

        mainMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent i) {
                templates.removeAll();
                SwingUtilities.invokeLater(() -> {
                    for (ResourcePointerAccessor accessor : ResourceUtils.getAllCCTemplates()) {
                        ActionListener actionListener = _ -> {
                            Reader input = null;
                            try {
                                input = accessor.getReader();
                            } catch (FileNotFoundException ignored) {
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
