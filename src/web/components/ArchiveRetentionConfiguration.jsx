import React from 'react';
import PropTypes from 'prop-types';
import { Input } from 'components/bootstrap';
import URLUtils from 'util/URLUtils';
import UserNotification from 'util/UserNotification';
import fetch from 'logic/rest/FetchProvider';

const urlPrefix = '/plugins/com.taxis99.graylog.archive';

const ArchiveRetentionConfiguration = React.createClass({
  propTypes: {
    config: React.PropTypes.object.isRequired,
    updateConfig: PropTypes.func.isRequired,
    repos: PropTypes.array
  },

  getInitialState() {
    return {
      repos: ['Loading Repos'],
      config: {
        max_number_of_indices: this.props.config.max_number_of_indices,
        name_of_repository: this.props.config.name_of_repository
      },
    };
  },

  componentWillReceiveProps(nextProps){
    this.setState({config: nextProps.config})
  },

  _onInputUpdate(field) {
    return (e) => {
      let config = this.state.config;
      let value = e.target.value;
      config[field] = value;
      this.setState({config})
      this.props.updateConfig(config);
    };
  },

  _url(path) {
    return URLUtils.qualifyUrl(`${urlPrefix}${path}`);
  },

  componentDidMount: function() {
    const promise = fetch('GET', this._url('/system/repository'));
    promise
      .then(
        response => {
          this.setState({repos: response})
          repos = response;
        },
        error => {
          UserNotification.error(`Fetching Collectors failed with status: ${error}`,
            'Could not retrieve Collectors');
        });
  },

  render() {
    return (
      <div>
        <fieldset>
          <Input type="number"
                 id="max_number_of_indices"
                 label="Max number of elasticsearch indices"
                 value={ this.state.config.max_number_of_indices }
                 maxlength="100"
                 onChange={ this._onInputUpdate('max_number_of_indices') }
                 help={ <span>Maximum number of <strong>elasticsearch</strong> indices to <strong>snapshots</strong></span> }
                 required />


          <div>
            <label className="control-label">Select the elasticsearch repository name</label>
          </div>

          <select id="name_of_repository"
                  className="form-control"
                  value={ this.state.config.name_of_repository }
                  onChange={ this._onInputUpdate('name_of_repository') } >
            { this.state.repos.map((name, repo) => <option value={ name } key={ repo }> { name } </option>) }
          </select>
        </fieldset>
      </div>
    );
  },
});

export default ArchiveRetentionConfiguration;
