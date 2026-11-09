package org.cdc.copy.builder;


import java.awt.event.ActionListener;
import javax.swing.JMenu;
import net.mcreator.ui.action.ActionRegistry;
import net.mcreator.ui.action.BasicAction;
import net.mcreator.ui.init.L10N;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class BasicActionBuilder {

  private final ActionRegistry actionRegistry;
  private String name;
  private ActionListener actionListener;
  private boolean dynamic;

  public BasicActionBuilder(ActionRegistry actionRegistry) {
    this.actionRegistry = actionRegistry;
  }

  public BasicActionBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public BasicActionBuilder markDynamic() {
    this.dynamic = true;
    return this;
  }

  public BasicActionBuilder setActionListener(ActionListener actionListener) {
    this.actionListener = actionListener;
    return this;
  }

  public void buildToJMenu(JMenu jMenu) {
    BasicAction basicAction = new BasicAction(actionRegistry,
        dynamic ? name : L10N.t("plugin.ccopy.menu." + jMenu.getName() + "." + name),
        actionListener);
    jMenu.add(basicAction);
  }
}
