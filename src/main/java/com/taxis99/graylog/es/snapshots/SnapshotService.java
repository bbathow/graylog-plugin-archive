package com.taxis99.graylog.es.snapshots;

import org.elasticsearch.action.admin.cluster.repositories.put.PutRepositoryResponse;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.delete.DeleteSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotResponse;
import org.elasticsearch.action.admin.cluster.repositories.delete.DeleteRepositoryResponse;
import org.elasticsearch.client.Client;

/**
 * Created by drsantos on 9/29/17.
 */
public interface SnapshotService {

    boolean isRepositoryExist(Client client, String repositoryName);

    PutRepositoryResponse createRepository(Client client, String repositoryName,
                                           String path, boolean compress);

    DeleteRepositoryResponse deleteRepository(Client client, String repositoryName);

    boolean isSnapshotExist(Client client, String repositoryName, String snapshotName);

    CreateSnapshotResponse createSnapshot(Client client, String repositoryName,
                                          String snapshotName, String indexName);

    DeleteSnapshotResponse deleteSnapshot(Client client, String repositoryName, String snapshotName);

    RestoreSnapshotResponse restoreSnapshot(Client client, String repositoryName, String snapshotName);

}
