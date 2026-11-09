package org.cdc.copy.ui;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.mcreator.ui.init.L10N;
import org.cdc.copy.accessor.MCreatorAccessor;
import org.intellij.lang.annotations.MagicConstant;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class Dialogs {

  private final MCreatorAccessor mCreator;

  public Dialogs(MCreatorAccessor mCreator) {
    this.mCreator = mCreator;
  }

  public List<String> showMultipleOptionDialog(List<String> stringList) {
    JPanel panel = new JPanel(new GridLayout(0, 1));
    List<JCheckBox> selected = new ArrayList<>();
    for (String s : stringList) {
      JCheckBox checkBox = new JCheckBox(L10N.t("options." + s));
      checkBox.setSelected(true);
      checkBox.setName(s);
      panel.add(checkBox);
      selected.add(checkBox);
    }
    var select = JOptionPane.showConfirmDialog(mCreator.getMCreator(), panel,
        L10N.t("dialog.options.title"),
        JOptionPane.OK_CANCEL_OPTION);
    if (select == JOptionPane.CANCEL_OPTION) {
      return List.of();
    }
    return selected.stream().map(a -> a.isSelected() ? a.getName() : null).filter(Objects::nonNull)
        .toList();
  }

  public void showWarningDialog(String warning) {
    showMessageDialog(warning, "Warning", JOptionPane.WARNING_MESSAGE);
  }

  public void showMessageDialog(String message, String title,
      @MagicConstant(intValues = {JOptionPane.INFORMATION_MESSAGE, JOptionPane.WARNING_MESSAGE,
          JOptionPane.ERROR_MESSAGE, JOptionPane.QUESTION_MESSAGE,
          JOptionPane.PLAIN_MESSAGE}) int messageType) {
    JOptionPane.showMessageDialog(mCreator.getMCreator(), message, title, messageType);
  }

  public String showRenameDialog(String originName) {
    return JOptionPane.showInputDialog(mCreator.getMCreator(), "Input new name", originName);
  }
}
