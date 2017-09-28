// eslint-disable-next-line no-unused-vars
import webpackEntry from 'webpack-entry';

import { PluginManifest, PluginStore } from 'graylog-web-plugin/plugin';

import packageJson from '../../package.json';
import BackupConfiguration from "components/ArchiveRetentionConfiguration";

PluginStore.register(new PluginManifest(packageJson, {
  indexRetentionConfig: [
    {
      //path da classe Java
      type: 'org.graylog2.indexer.retention.strategies.SnapshotRetentionStrategy',
      displayName: 'Snapshot Strategy',
      configComponent: BackupConfiguration,
      summaryComponent: BackupConfiguration
    },
  ],
}));
