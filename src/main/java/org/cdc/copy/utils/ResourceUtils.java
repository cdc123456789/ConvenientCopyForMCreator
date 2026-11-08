package org.cdc.copy.utils;

import java.io.InputStream;
import java.util.List;
import net.mcreator.io.TemplatesLoader;
import net.mcreator.plugin.PluginLoader;
import org.cdc.copy.accessor.ResourcePointerAccessor;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class ResourceUtils {
  public static List<ResourcePointerAccessor> getAllCCTemplates(){
    return TemplatesLoader.loadTemplates("cctemplate","json").stream().map(ResourcePointerAccessor::new).toList();
  }

  public static InputStream getResource(String path){
    return PluginLoader.INSTANCE.getResourceAsStream(path);
  }
}
