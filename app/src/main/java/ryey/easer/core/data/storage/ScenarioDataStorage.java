package ryey.easer.core.data.storage;

import android.content.Context;

import java.io.IOException;

import ryey.easer.core.data.EventStructure;
import ryey.easer.core.data.ScenarioStructure;
import ryey.easer.core.data.storage.backend.ScenarioDataStorageBackendInterface;
import ryey.easer.core.data.storage.backend.json.scenario.JsonScenarioDataStorageBackend;

public class ScenarioDataStorage extends AbstractDataStorage<ScenarioStructure, ScenarioDataStorageBackendInterface> {


    private static ScenarioDataStorage instance = null;

    Context context;

    public static ScenarioDataStorage getInstance(Context context) {
        if (instance == null) {
            instance = new ScenarioDataStorage();
            instance.storage_backend_list = new ScenarioDataStorageBackendInterface[] {
                    JsonScenarioDataStorageBackend.getInstance(context),
            };
            instance.context = context;
        }
        return instance;
    }

    @Override
    public ScenarioStructure get(String name) {
        return super.get(name);
    }

    @Override
    boolean isSafeToDelete(String name) {
        return StorageHelper.isSafeToDeleteScenario(context, name);
    }

    @Override
    public boolean delete(String name) {
        return super.delete(name);
    }

    @Override
    public boolean edit(String oldName, ScenarioStructure scenario) throws IOException {
        if (super.edit(oldName, scenario)) {
            if (!oldName.equals(scenario.getName())) {
                handleRename(oldName, scenario);
            }
            return true;
        } else
            return false;
    }

    private void handleRename(String oldName, ScenarioStructure newScenario) throws IOException {
        EventDataStorage eventDataStorage = EventDataStorage.getInstance(context);
        for (EventStructure eventStructure : eventDataStorage.allEvents()) {
            if (oldName.equals(eventStructure.getScenario().getName())) {
                eventStructure.setScenario(newScenario);
                eventDataStorage.update(eventStructure);
            }
        }
    }
}
