package org.cdc.copy;

import net.mcreator.plugin.JavaPlugin;
import net.mcreator.plugin.Plugin;
import net.mcreator.plugin.events.PreGeneratorsLoadingEvent;
import net.mcreator.plugin.events.workspace.MCreatorLoadedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import org.cdc.copy.accessor.MCreatorAccessor;
import org.cdc.copy.init.Convertors;
import org.cdc.copy.init.Extractors;
import org.cdc.copy.init.Menus;
import org.cdc.copy.init.TypeAdapters;

public class PluginMain extends JavaPlugin {

    private static final Logger LOG = LogManager.getLogger("Copy plugin");

    public PluginMain(Plugin plugin) {
        super(plugin);

        addListener(PreGeneratorsLoadingEvent.class,_->{
            Extractors.getInstance().init();
            Convertors.getInstance().init();
            TypeAdapters.getInstance().init();
        });

        addListener(MCreatorLoadedEvent.class, event -> SwingUtilities.invokeLater(() -> Menus.registerMenus(new MCreatorAccessor(event.getMCreator()))));

        LOG.info("Plugin");
    }

}
