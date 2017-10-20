package com.taxis99.graylog.archive;

import com.taxis99.graylog.es.snapshots.SnapshotService;
import com.taxis99.graylog.es.snapshots.SnapshotServiceImpl;
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

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ArchiveModule.class);

    @Override
    protected void configure() {

        /*
         * Register your plugin types here.
         *
         * Examples:
         *
         * addMessageInput(Class<? extends MessageInput>);
         * addMessageFilter(Class<? extends MessageFilter>);
         * addMessageOutput(Class<? extends MessageOutput>);
         * addPeriodical(Class<? extends Periodical>);
         * addAlarmCallback(Class<? extends AlarmCallback>);
         * addInitializer(Class<? extends Service>);
         * addRestResource(Class<? extends PluginRestResource>);
         *
         *
         * Add all configuration beans returned by getConfigBeans():
         *
         * addConfigBeans();
         */

        addRetentionStrategy(ArchiveRetentionStrategy.class);
        bind(SnapshotService.class).to(SnapshotServiceImpl.class);
        addConfigBeans();
        addRestResource(RepositoryResource.class);
        LOG.info("graylog-plugin-archive plugin started");
    }
}
