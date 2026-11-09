package org.cdc.copy.ui;

import org.cdc.copy.accessor.MCreatorAccessor;

import javax.swing.*;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/9
 */
public class ProgressDialog {
    private final net.mcreator.ui.dialogs.ProgressDialog parent;
    private net.mcreator.ui.dialogs.ProgressDialog.ProgressUnit last;

    public ProgressDialog(MCreatorAccessor mcreator, String title) {
        this.parent = new net.mcreator.ui.dialogs.ProgressDialog(mcreator.getMCreator(), title);
    }

    public void addProgressUnit(String name) {
        var unit = new net.mcreator.ui.dialogs.ProgressDialog.ProgressUnit(name);
        parent.addProgressUnit(unit);
        last = unit;
    }

    public void setProgress(int percent) {
        last.setPercent(percent);
    }

    public void markFinish() {
        if (last == null) return;
        last.markStateOk();
    }

    public void markError() {
        if (last == null) return;
        last.markStateError();
    }

    public void markWarning() {
        if (last == null) return;
        last.markStateWarning();
    }

    public void show() {
        parent.setVisible(true);
    }

    public void hide() {
        parent.hideDialog();
    }
}
