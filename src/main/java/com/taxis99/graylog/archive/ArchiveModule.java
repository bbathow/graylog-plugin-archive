package com.taxis99.graylog.archive;

import com.taxis99.graylog.SnapshotService;
import com.taxis99.graylog.SnapshotServiceImpl;
import com.taxis99.graylog.strategies.ArchiveRetentionStrategy;
import org.graylog2.plugin.PluginConfigBean;
import org.graylog2.plugin.PluginModule;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.Set;

/**
 * Extend the PluginModule abstract class here to add you plugin to the system.
 */
public class ArchiveModule extends PluginModule {
    /**
     * Returns all configuration beans required by this plugin.
     *
     * Implementing this method is optional. The default method returns an empty {@link Set}.
     */

    @Override
    public Set<? extends PluginConfigBean> getConfigBeans() {
        return Collections.emptySet();
    }

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ArchiveModule.class);

    @Override
    protected void configure() {

        addRetentionStrategy(ArchiveRetentionStrategy.class);
        bind(SnapshotService.class).to(SnapshotServiceImpl.class);
        addConfigBeans();
        addRestResource(RepositoryResource.class);
        log.info("graylog-archive-plugin started");
    }
}
