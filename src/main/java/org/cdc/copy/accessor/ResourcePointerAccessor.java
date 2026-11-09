package org.cdc.copy.accessor;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;
import net.mcreator.io.ResourcePointer;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/9
 */
public class ResourcePointerAccessor {

  private final ResourcePointer resourcePointer;

  public ResourcePointerAccessor(ResourcePointer resourcePointer) {
    this.resourcePointer = resourcePointer;
  }

  public Reader getReader() throws FileNotFoundException {
    return new InputStreamReader(resourcePointer.getStream());
  }

  public String getPath() {
    return resourcePointer.identifier.toString();
  }

  public String getIdentify(){
    return resourcePointer.toString().toLowerCase(Locale.ROOT).replace(" ","_");
  }

  public boolean isInClassPath(){
    return resourcePointer.isInClasspath();
  }
}
