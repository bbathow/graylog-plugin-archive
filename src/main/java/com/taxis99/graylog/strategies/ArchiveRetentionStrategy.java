package com.taxis99.graylog.strategies;


import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.taxis99.graylog.SnapshotServiceImpl;
import org.graylog2.audit.AuditActor;
import org.graylog2.audit.AuditEventSender;
import org.graylog2.indexer.IndexSet;
import org.graylog2.indexer.indexset.IndexSetConfig;
import org.graylog2.indexer.indices.Indices;
import org.graylog2.indexer.retention.strategies.AbstractIndexCountBasedRetentionStrategy;
import org.graylog2.plugin.indexer.retention.RetentionStrategyConfig;
import org.graylog2.plugin.system.NodeId;
import org.graylog2.shared.system.activities.ActivityWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.graylog2.audit.AuditEventTypes.ES_INDEX_RETENTION_DELETE;

public class ArchiveRetentionStrategy extends AbstractIndexCountBasedRetentionStrategy {
    private static final Logger log = LoggerFactory.getLogger(ArchiveRetentionStrategy.class);

    private final Indices indices;
    private final NodeId nodeId;
    private final AuditEventSender auditEventSender;
    private SnapshotServiceImpl snapshotService;

    @Inject
    public ArchiveRetentionStrategy(Indices indices, ActivityWriter activityWriter, Indices indices1, NodeId nodeId, AuditEventSender auditEventSender, SnapshotServiceImpl snapshotService) {
        super(indices, activityWriter);
        this.indices = indices1;
        this.nodeId = nodeId;
        this.auditEventSender = auditEventSender;
        this.snapshotService = snapshotService;
    }

    @Override
    protected Optional<Integer> getMaxNumberOfIndices(IndexSet indexSet) {
        final IndexSetConfig indexSetConfig = indexSet.getConfig();
        final RetentionStrategyConfig strategyConfig = indexSetConfig.retentionStrategy();

        if (!(strategyConfig instanceof ArchiveRetentionStrategyConfig)) {
            throw new IllegalStateException("Invalid retention strategy config <" + strategyConfig.getClass().getCanonicalName() + "> for index set <" + indexSetConfig.id() + ">");
        }

        final ArchiveRetentionStrategyConfig config = (ArchiveRetentionStrategyConfig) strategyConfig;
        log.info("MaxNumberOfIndices: {} ", config.maxNumberOfIndices());

        return Optional.of(config.maxNumberOfIndices());
    }


    protected String getRepositoryName(IndexSet indexSet) {
        final IndexSetConfig indexSetConfig = indexSet.getConfig();
        final RetentionStrategyConfig strategyConfig = indexSetConfig.retentionStrategy();

        if (!(strategyConfig instanceof ArchiveRetentionStrategyConfig)) {
            throw new IllegalStateException("Invalid retention strategy config <" + strategyConfig.getClass().getCanonicalName() + "> for index set <" + indexSetConfig.id() + ">");
        }

        final ArchiveRetentionStrategyConfig config = (ArchiveRetentionStrategyConfig) strategyConfig;
        log.info("nameRepo {} " + config.nameOfRepository());

        return config.nameOfRepository();
    }

    @Override
    public void retain(String indexName, IndexSet indexSet) {
        final Stopwatch sw = Stopwatch.createStarted();

        log.info("retain ArchiveRetentionStrategy has been started");

        snapshotService.createSnapshot(getRepositoryName(indexSet), indexName);
        indices.delete(indexName);

        auditEventSender.success(AuditActor.system(nodeId), ES_INDEX_RETENTION_DELETE, ImmutableMap.of(
                "index_name", indexName,
                "retention_strategy", this.getClass().getCanonicalName()
        ));

        log.info("Finished index retention strategy for elasticsearch [delete] for index <{}> in {}ms.", indexName,
                sw.stop().elapsed(TimeUnit.MILLISECONDS));
    }


    @Override
    public Class<? extends RetentionStrategyConfig> configurationClass() {
        return ArchiveRetentionStrategyConfig.class;
    }

    @Override
    public RetentionStrategyConfig defaultConfiguration() {
        return ArchiveRetentionStrategyConfig.createDefault();
    }
}
