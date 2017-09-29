package com.taxis99.graylog.es.snapshots;

import org.elasticsearch.action.admin.cluster.repositories.delete.DeleteRepositoryResponse;
import org.elasticsearch.action.admin.cluster.repositories.put.PutRepositoryResponse;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.delete.DeleteSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotRequest;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.RepositoryMetaData;
import org.elasticsearch.common.settings.Settings;

import org.elasticsearch.snapshots.SnapshotInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by drsantos on 9/29/17.
 */
public class SnapshotServiceImpl implements SnapshotService {

    private static final Logger log = LoggerFactory.getLogger(SnapshotServiceImpl.class);

    @Override
    public boolean isRepositoryExist(Client client, String repositoryName) {
        boolean result = false;

        try {

            List<RepositoryMetaData> repositories = client.admin().cluster().prepareGetRepositories().get().repositories();

            if(repositories.size() > 0){
                for(RepositoryMetaData repo :repositories)
                    result = repositoryName.equals(repo.name())?true:false;
            }

        } catch (Exception ex){
            log.error("Exception in getRepository method: " + ex.toString());

        } finally {
            return result;
        }
    }

    @Override
    public PutRepositoryResponse createRepository(Client client, String repositoryName, String path, boolean compress) {

        PutRepositoryResponse putRepositoryResponse = null;

        try {

            if(!isRepositoryExist(client, repositoryName)) {

                Settings settings = Settings.builder()
                        .put("location", path + repositoryName)
                        .put("compress", compress).build();

                putRepositoryResponse = client.admin().cluster().preparePutRepository(repositoryName)
                        .setType("fs").setSettings(settings).get();

                log.info("Repository was created.");

            } else
                log.info(repositoryName + " repository already exists");

        } catch(Exception ex){
            log.error("Exception in createRepository method: " + ex.toString());

        } finally {
            return putRepositoryResponse;
        }

    }

    @Override
    public DeleteRepositoryResponse deleteRepository(Client client, String repositoryName) {
        DeleteRepositoryResponse deleteRepositoryResponse = null;

        try {

            if (isRepositoryExist(client, repositoryName)) {

                deleteRepositoryResponse = client.admin().cluster().prepareDeleteRepository(repositoryName).execute().actionGet();
                log.info(repositoryName + " repository has been deleted.");
            }

        } catch (Exception ex){
            log.error("Exception in deleteRepository method: " + ex.toString());

        } finally {
            return deleteRepositoryResponse;
        }
    }

    @Override
    public boolean isSnapshotExist(Client client, String repositoryName, String snapshotName) {
        boolean result = false;

        try {

            List<SnapshotInfo> snapshotInfo = client.admin().cluster().prepareGetSnapshots(repositoryName).get().getSnapshots();

            if(snapshotInfo.size() > 0){
                for(SnapshotInfo snapshot :snapshotInfo)
                    result = snapshotName.equals(snapshot.name())?true:false;
            }

        } catch (Exception ex) {
            log.error("Exception in getSnapshot method: " + ex.toString());

        } finally {
            return result;
        }
    }

    @Override
    public CreateSnapshotResponse createSnapshot(Client client, String repositoryName, String snapshotName, String indexName) {
        CreateSnapshotResponse createSnapshotResponse = null;
        try {

            if(isSnapshotExist(client, repositoryName, snapshotName))
                log.info(snapshotName + " snapshot already exists");

            else {

                createSnapshotResponse = client.admin().cluster()
                        .prepareCreateSnapshot(repositoryName, snapshotName)
                        .setWaitForCompletion(true)
                        .setIndices(indexName).get();

                log.info("Snapshot was created.");
            }

        } catch (Exception ex){
            log.error("Exception in createSnapshot method: " + ex.toString());

        } finally {
            return createSnapshotResponse;
        }
    }

    @Override
    public DeleteSnapshotResponse deleteSnapshot(Client client, String repositoryName, String snapshotName) {
        DeleteSnapshotResponse deleteSnapshotResponse = null;

        try {

            if (isSnapshotExist(client, repositoryName, snapshotName)) {
                deleteSnapshotResponse = client.admin().cluster().prepareDeleteSnapshot(repositoryName, snapshotName)
                        .execute().actionGet();
                log.info(snapshotName + " snapshot has been deleted.");
            }

        } catch (Exception ex){
            log.error("Exception in deleteSnapshot method: " + ex.toString());

        } finally {
            return deleteSnapshotResponse;
        }
    }

    @Override
    public RestoreSnapshotResponse restoreSnapshot(Client client, String repositoryName, String snapshotName) {
        RestoreSnapshotResponse restoreSnapshotResponse = null;
        try {

            if(isRepositoryExist(client, repositoryName) && isSnapshotExist(client, repositoryName, snapshotName)){

                RestoreSnapshotRequest restoreSnapshotRequest = new RestoreSnapshotRequest(repositoryName, snapshotName);
                restoreSnapshotResponse = client.admin().cluster().restoreSnapshot(restoreSnapshotRequest).get();

                log.info("Snapshot was restored.");
            }

        } catch (Exception ex){
            log.error("Exception in restoreSnapshot method: " + ex.toString());

        } finally {
            return restoreSnapshotResponse;
        }
    }
}
