package org.cdc.copy.init;

import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Function;

import org.cdc.copy.accessor.MCreatorAccessor;
import org.cdc.copy.interfaces.IExtractor;
import org.cdc.copy.utils.Workers;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class Extractors implements IExtractor{
  private static final ServiceLoader<IExtractor> serviceLoader = ServiceLoader.load(IExtractor.class,Extractors.class.getClassLoader());

  private static final HashMap<String,IExtractor> idToExtractor = new HashMap<>();

  private static Extractors INSTANCE;

  public static Extractors getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new Extractors();
    }
    return INSTANCE;
  }

  private Function<String, Boolean> manager;

  private Extractors(){}

  public void init(){
    serviceLoader.forEach(a-> idToExtractor.put(a.getID(),a));
  }

  public IExtractor getExtractor(String id){
    return idToExtractor.get(id);
  }

  public List<String> getIDs(){
    return idToExtractor.keySet().stream().sorted().toList();
  }

  public void setManager(Function<String, Boolean> manager) {
    this.manager = manager;
  }

  @Override
  public String getID() {
    return "general";
  }

  @Override
  public void extractFrom(JsonObject result, MCreatorAccessor mCreatorAccessor) {
    var progressDialog = mCreatorAccessor.createProgressDialog("Copying");
    Workers.getInstance().executeCopyOrPaste(()->{
      var list = getIDs();
      for (String s : list) {
        progressDialog.addProgressUnit(s);
        if (manager != null && !manager.apply(s)){
          continue;
        }
        IExtractor extractor1 = this.getExtractor(s);
        var result1 = new JsonObject();
        extractor1.extractFrom(result1, mCreatorAccessor);
        result.add(s, result1);
        progressDialog.markFinish();
      }
      progressDialog.hide();
    });
    progressDialog.show();
  }
}
