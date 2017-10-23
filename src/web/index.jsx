// eslint-disable-next-line no-unused-vars
import webpackEntry from 'webpack-entry';
import { PluginManifest, PluginStore } from 'graylog-web-plugin/plugin';
import packageJson from '../../package.json';
import ArchiveRetentionConfiguration from "components/ArchiveRetentionConfiguration";
import ArchiveRetentionStrategySummary from "components/ArchiveRetentionStrategySummary";

const manifest = new PluginManifest(packageJson, {

  indexRetentionConfig: [
    {
      type: 'com.taxis99.graylog.strategies.ArchiveRetentionStrategy',
      displayName: 'Snapshot Index',
      configComponent: ArchiveRetentionConfiguration,
      summaryComponent: ArchiveRetentionStrategySummary,
    }
  ],
});

PluginStore.register(manifest);

if (module.hot) {
  module.hot.accept();
  module.hot.dispose(() => PluginStore.unregister(manifest));
}
