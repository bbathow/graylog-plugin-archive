package com.taxis99.graylog;

import com.taxis99.graylog.exception.SnapshotException;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.snapshot.CreateSnapshot;
import io.searchbox.snapshot.CreateSnapshotRepository;
import io.searchbox.snapshot.DeleteSnapshot;
import io.searchbox.snapshot.DeleteSnapshotRepository;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by drsantos on 9/29/17.
 */

public class SnapshotServiceImpl implements SnapshotService {

    private static final Logger log = LoggerFactory.getLogger(SnapshotServiceImpl.class);

    private final JestClient jestClient;

    @Inject
    public SnapshotServiceImpl(JestClient jestClient) {
        this.jestClient = jestClient;
    }

    @Override
    public void createRepository(String repositoryName, String path, boolean compress) {

        try {
            final Settings.Builder registerRepositorySettings = Settings.builder();
            registerRepositorySettings.put("compress", "true");
            registerRepositorySettings.put("location", "/dev/shm");
            registerRepositorySettings.put("chunk_size", "10m");
            registerRepositorySettings.put("max_restore_bytes_per_sec", "40mb");
            registerRepositorySettings.put("max_snapshot_bytes_per_sec", "40mb");
            registerRepositorySettings.put("readonly", "false");
            registerRepositorySettings.put("type", "fs");

            CreateSnapshotRepository createSnapshotRepository = new CreateSnapshotRepository.Builder(repositoryName).settings(registerRepositorySettings.build().getAsMap()).build();

            JestResult jestResult = jestClient.execute(createSnapshotRepository);

            if(jestResult.isSucceeded()) {
                log.info("Repository was created.");
            } else {
                log.error("Repository was created.", jestResult.getErrorMessage());
                throw new SnapshotException(jestResult.getErrorMessage());
            }

        } catch(Exception ex){
            log.error("Exception in createRepository method: " + ex.getMessage());
        }
    }

    @Override
    public void deleteRepository(String repositoryName) {

        try {

            jestClient.execute(new DeleteSnapshotRepository.Builder(repositoryName).build());
            log.info(repositoryName + " repository has been deleted.");

        } catch (Exception ex){
            log.error("Exception in deleteRepository method: " + ex.getMessage());
        }
    }

    @Override
    public void createSnapshot(String repositoryName, String index) {

        final Settings.Builder registerRepositorySettings = Settings.builder();
        registerRepositorySettings.put("indices", index);
        registerRepositorySettings.put("ignore_unavailable", "true");
        registerRepositorySettings.put("include_global_state", "false");
        String snapshotName;

        try {

            snapshotName = new SimpleDateFormat("yyyy-MM-DD:HH:mm:ss").format(new Date());

            JestResult jestResult = jestClient.execute(new CreateSnapshot.Builder(repositoryName, snapshotName)
                    .settings(registerRepositorySettings.build())
                    .build());

            if(jestResult.isSucceeded()) {
                log.info("Repository was created.");
            } else {
                log.error("Repository was created.", jestResult.getErrorMessage());
                throw new SnapshotException(jestResult.getErrorMessage());
            }

        } catch (Exception ex){
            log.error("Exception in createSnapshot method: " + ex.getMessage());
        }
    }

    @Override
    public void deleteSnapshot(String repositoryName, String snapshotName) {

        try {
            jestClient.execute(new DeleteSnapshot.Builder(repositoryName, snapshotName).build());
            log.info(snapshotName + " snapshot has been deleted.");

        } catch (Exception ex){
            log.error("Exception in deleteSnapshot method: " + ex.getMessage());
        }
    }
}
