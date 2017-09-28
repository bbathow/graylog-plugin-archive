import React from 'react';
import { Input, Button } from 'react-bootstrap';
import BootstrapModalForm from 'components/bootstrap/BootstrapModalForm';
import { IfPermitted, Select, ISODurationInput } from 'components/common';
import ObjectUtils from 'util/ObjectUtils';
import BackupActions from "components/ArchiveActions";
import BackupStore from "components/ArchiveServices";
import RepositorySelect from 'components/RepositorySelect';

const ArchiveRetentionConfiguration = React.createClass({
  propTypes: {
    config: React.PropTypes.object,
    updateConfig: React.PropTypes.func.isRequired,
  },

  getDefaultProps() {
    return {
      config: {
        enabled: false,
        cfg_backup_path: 'data/bck',
        cfg_restore_path: 'data/restore',
        mongodump_path: '/usr/bin',
        scheduled_period: 'P15D',
      },
    };
  },

getInitialState() {
    return {
      config: ObjectUtils.clone(this.props.config),
    };
  },

  _updateConfigField(field, value) {
    const update = ObjectUtils.clone(this.state.config);
    update[field] = value;
    this.setState({config: update});
  },

  _onSelect(field) {
    return (selection) => {
      this._updateConfigField(field, selection);
    };
  },

  _onUpdate(field) {
    return (e) => {
      this._updateConfigField(field, e.target.value);
    };
  },

  _createSnapshot() {
    BackupActions.createSnapshot();
  },

  _saveConfig() {
    this.props.updateConfig(this.state.config).then(() => {
      this._closeModal();
    });
  },

  getInitialState() {
    return {
      max_number_of_indices: this.props.config.max_number_of_indices,
    };
  },

  _onInputUpdate(field) {
    return (e) => {
      const update = {};
      update[field] = e.target.value;

      this.setState(update);
      this.props.updateConfig(update);
    };
  },

  render() {
    return (
        <div>
          <div>
            <fieldset>
              <Input type="number"
                     id="max-number-of-indices"
                     label="Max number of elasticsearch indices"
                     onChange={this._onInputUpdate('max_number_of_indices')}
                     value={this.state.max_number_of_indices}
                     help={<span>Maximum number of <strong>elasticsearch</strong> indices to <strong>snapshots</strong></span>}
                     required />
            </fieldset>
          </div>


          <div>
            <label htmlFor="recipient-name" className="control-label">Select the elasticsearch repository name</label>
            <fieldset>
            <RepositorySelect ref="session_timeout_unit" className="form-control repository-select-fields"
                               disabled={this.state.sessionTimeoutNever}
                               value={this.state.unit} onChange={this._onChangeUnit}
                               />
            </fieldset>
          </div>
        </div>
    );
  },
});

export default ArchiveRetentionConfiguration;
