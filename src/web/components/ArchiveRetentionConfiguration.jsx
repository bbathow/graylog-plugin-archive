import PropTypes from 'prop-types';
import React from 'react';
import { Input } from 'components/bootstrap';
import RepositorySelect from 'components/RepositorySelect';


const ArchiveRetentionConfiguration = React.createClass({
  propTypes: {
    config: PropTypes.object.isRequired,
    jsonSchema: PropTypes.object.isRequired,
    updateConfig: PropTypes.func.isRequired,
  },

  getInitialState() {
    return {
      max_number_of_indices: 20,
       //max_number_of_indices: this.props.config.max_number_of_indices,

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
