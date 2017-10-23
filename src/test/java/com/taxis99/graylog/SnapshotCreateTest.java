package com.taxis99.graylog;

import com.google.gson.Gson;
import io.searchbox.snapshot.CreateSnapshot;
import org.elasticsearch.common.settings.Settings;
import org.junit.Test;


import static junit.framework.Assert.assertEquals;

public class SnapshotCreateTest {

    private String repository = "repo01";
    private String snapshot = "snapshot99";

    @Test
    public void testCreateSnapshot()  {
        CreateSnapshot createSnapshot = new CreateSnapshot.Builder(repository, snapshot).waitForCompletion(true).build();
        assertEquals("PUT", createSnapshot.getRestMethodName());
        assertEquals("/_snapshot/repo01/snapshot99?wait_for_completion=true", createSnapshot.getURI());
    }

    @Test
    public void testCreateSnapshotWithSettings() {

        final Settings.Builder registerRepositorySettings = Settings.builder();
        registerRepositorySettings.put("indices", "index_1");
        registerRepositorySettings.put("ignore_unavailable", "true");
        registerRepositorySettings.put("include_global_state", "false");

        CreateSnapshot createSnapshot = new CreateSnapshot.Builder(repository, snapshot)
                .settings(registerRepositorySettings.build().getAsMap())
                .waitForCompletion(true)
                .build();

        assertEquals("PUT", createSnapshot.getRestMethodName());
        assertEquals("/_snapshot/repo01/snapshot99?wait_for_completion=true", createSnapshot.getURI());
        String settings = new Gson().toJson(createSnapshot.getData(new Gson()));
        assertEquals("\"{\\\"ignore_unavailable\\\":\\\"true\\\",\\\"include_global_state\\\":\\\"false\\\",\\\"indices\\\":\\\"index_1\\\"}\"", settings);
    }
}
