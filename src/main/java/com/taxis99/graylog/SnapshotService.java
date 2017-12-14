package com.taxis99.graylog;

import java.util.List;

public interface SnapshotService {

    void createRepository(String repositoryName,
                                           String path, boolean compress);
    void deleteRepository(String repositoryName);

    void createSnapshot(String repositoryName, String index);

    void deleteSnapshot(String repositoryName, String snapshotName);

    List<String> repositoryList();

}
