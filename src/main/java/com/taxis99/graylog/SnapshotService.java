package com.taxis99.graylog;

import org.elasticsearch.action.admin.cluster.repositories.put.PutRepositoryResponse;
import org.elasticsearch.action.admin.cluster.snapshots.create.CreateSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.delete.DeleteSnapshotResponse;
import org.elasticsearch.action.admin.cluster.snapshots.restore.RestoreSnapshotResponse;
import org.elasticsearch.action.admin.cluster.repositories.delete.DeleteRepositoryResponse;
import org.elasticsearch.client.Client;

import java.util.List;

public interface SnapshotService {

    void createRepository(String repositoryName,
                                           String path, boolean compress);
    void deleteRepository(String repositoryName);

    void createSnapshot(String repositoryName, String index);

    void deleteSnapshot(String repositoryName, String snapshotName);

}
