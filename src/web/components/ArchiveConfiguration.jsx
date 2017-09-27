import React from 'react';
import { Input, Button } from 'react-bootstrap';
import BootstrapModalForm from 'components/bootstrap/BootstrapModalForm';
import { IfPermitted, Select, ISODurationInput } from 'components/common';


const ArchiveConfiguration = React.createClass({
  propTypes: {
    config: React.PropTypes.object,
    updateConfig: React.PropTypes.func.isRequired,
  },

  render() {
    return (
        <div>
          <h3>Archive configuration</h3>
          <p>
            Graylog backup plugin for dump Graylog configuration data.
            With <strong>Update Configuration</strong> is possible to adapt backup configuration to your needs and system. <strong>Restore data</strong> button restore data under directory Backuprestore/graylog
          </p>
          <dl className="deflist">
            <dt>Enabled:</dt>
            <dd>{this.state.config.enabled === true ? 'yes' : 'no'}</dd>
            <dt>Backup frequency:</dt>
            <dd>{this.state.config.scheduled_period}</dd>
            <dt>BackupPath:</dt>
            <dd>{this.state.config.cfg_backup_path}</dd>
            <dt>Backuprestore:</dt>
            <dd>{this.state.config.cfg_restore_path}</dd>
            <dt>Mongodump path:</dt>
            <dd>{this.state.config.mongodump_path}</dd>
          </dl>

          <IfPermitted permissions="clusterconfigentry:edit">
            <Button bsStyle="info" bsSize="xs" onClick={this._openModal}>Update Configuration</Button>
            <Button bsStyle="info" bsSize="xs" onClick={this._launchRestore}>Restore Data</Button>
          </IfPermitted>

          <BootstrapModalForm ref="backupConfigModal"
                              title="Update Graylog backup Configuration"
                              onSubmitForm={this._saveConfig}
                              onModalClose={this._resetConfig}
                              submitButtonText="Save">
            <fieldset>
              <Input type="checkbox"
                       ref="configEnabled"
                     label="Enable Backup process"
                     name="enabled"
                     checked={this.state.config.enabled}
                     onChange={this._onCheckboxClick('enabled', 'configEnabled')}/>

              <Input type="text"
                     label="Bakup directory"
                     name="backup_dir"
                     value={this.state.config.cfg_backup_path}
                     onChange={this._onUpdate('cfg_backup_path')}/>

              <Input type="text"
                     label="Restore directory"
                     name="backup_dir"
                     value={this.state.config.cfg_restore_path}
                     onChange={this._onUpdate('cfg_restore_path')}/>

              <Input type="text"
                     label="Path where mongodump is installed"
                     name="mongodump_dir"
                     value={this.state.config.mongodump_path}
                     onChange={this._onUpdate('mongodump_path')}/>

              <ISODurationInput duration={this.state.config.scheduled_period}
                                update={this._onUpdatePeriod('scheduled_period')}
                                label="Expiration threshold (as ISO8601 Duration)"
                                help="Amount of time after which inactive collectors are purged from the database."
                                validator={this._expirationThresholdValidator}
                                errorText="invalid (min: 1 minute)"
                                required />
            </fieldset>
          </BootstrapModalForm>
        </div>
    );
  },
});

export default ArchiveConfiguration;
