package org.cdc.copy.init;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.function.Function;

import net.mcreator.ui.workspace.WorkspacePanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cdc.copy.accessor.MCreatorAccessor;
import org.cdc.copy.interfaces.IConvertor;
import org.cdc.copy.utils.Workers;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/8
 */
public class Convertors implements IConvertor {

    private static final ServiceLoader<IConvertor> serviceLoader = ServiceLoader.load(IConvertor.class,
            Convertors.class.getClassLoader());

    private static final HashMap<String, IConvertor> idToDecoder = new HashMap<>();

    private static Convertors INSTANCE;
    private static final Logger LOGGER = LogManager.getLogger(Convertors.class);

    public static Convertors getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Convertors();
        }
        return INSTANCE;
    }

    private Function<String, Boolean> manager;

    private Convertors() {

    }

    public void init() {
        serviceLoader.forEach(a -> idToDecoder.put(a.getID(), a));
    }

    public IConvertor getDecoder(String id) {
        return idToDecoder.get(id);
    }

    public List<String> getIDs() {
        return idToDecoder.keySet().stream().sorted().toList();
    }

    public void setManager(Function<String, Boolean> manager) {
        this.manager = manager;
    }

    @Override
    public String getID() {
        return "general";
    }

    @Override
    public void convert(JsonObject jsonObject, MCreatorAccessor mCreatorAccessor) {
        if (jsonObject == null) {
            return;
        }
        var progressDialog = mCreatorAccessor.createProgressDialog("Pasting");
        Workers.getInstance().executeCopyOrPaste(()->{
            for (Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
                var key = stringJsonElementEntry.getKey();
                progressDialog.addProgressUnit(key);
                if (idToDecoder.containsKey(key)) {
                    var decoder = getDecoder(key);
                    if (manager != null && !manager.apply(key)) {
                        continue;
                    }
                    if (stringJsonElementEntry.getValue().isJsonObject()) {
                        decoder.convert(stringJsonElementEntry.getValue().getAsJsonObject(), mCreatorAccessor);
                    }
                } else {
                    // special key: _fv
                    if (key.charAt(0) != '_') {
                        LOGGER.warn("Unknown decoder: {}", key);
                    }
                }
                progressDialog.markFinish();
            }
            progressDialog.hide();
        });
        progressDialog.show();
    }
}
