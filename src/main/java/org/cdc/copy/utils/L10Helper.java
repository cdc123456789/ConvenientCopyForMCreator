package org.cdc.copy.utils;

import javax.swing.JMenu;
import net.mcreator.ui.init.L10N;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class L10Helper {

  public static String t(String key, Object... parameters) {
    return L10N.t(key,parameters);
  }

  public static String getMenuKey(String menuIdentify) {
    return "plugin.ccopy.menu." + menuIdentify;
  }

  public static JMenu menu(String menuIdentify){
    var templates = new JMenu(t(L10Helper.getMenuKey(menuIdentify)));
    templates.setName(menuIdentify);
    return templates;
  }
}
