import React from 'react';
import PropTypes from 'prop-types';
import { Input } from 'components/bootstrap';
import ObjectUtils from 'util/ObjectUtils';
import RepositorySelect from 'components/RepositorySelect';
import URLUtils from 'util/URLUtils';
import UserNotification from 'util/UserNotification';
import fetch, { fetchPeriodically } from 'logic/rest/FetchProvider';

const urlPrefix = '/plugins/com.taxis99.graylog.archive';
var names = ['Jake', 'Jon', 'Thruster'];

const ArchiveRetentionConfiguration = React.createClass({
  propTypes: {
    config: React.PropTypes.object,
    jsonSchema: PropTypes.object,
    updateConfig: PropTypes.func.isRequired,
    collectors: PropTypes.array
  },

  getDefaultProps() {
    return {
      config: {
        max_number_of_indices: 10
      },
      collectors: []
    };
  },

  init() {
    this.trigger({ collectors: this.collectors });
  },

  getInitialState() {
    return {
      config: ObjectUtils.clone(this.props.config),
      names: []
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

  _url(path) {
      return URLUtils.qualifyUrl(`${urlPrefix}${path}`);
  },

/*
  loadNames() {
    fetch('GET', this._url('/system/repository'))
      .then(response => {
        if(response.ok) {
          console.debug(response);
          return response.data;
        }
      }).catch(error => {
      return Promise.reject(Error(error.message))
    })


    /*fetch('GET', this._url('/system/repository'))
      .then(
        response => {
          return response.body;
        },
        error => {
          UserNotification.error(`Fetching Collectors failed with status: ${error}`,
            'Could not retrieve Collectors');
        });

  },
*/
  componentDidMount: function() {

    const promise = fetch('GET', this._url('/system/repository'));
    promise
      .then(
        response => {
          this.setState({names: response})
          names = response;
          console.debug(response)
          return names;
        },
        error => {
          UserNotification.error(`Fetching Collectors failed with status: ${error}`,
            'Could not retrieve Collectors');
        }).bind(this);
        console.log(names)
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
                     value={this.state.config.max_number_of_indices}
                     help={<span>Maximum number of <strong>elasticsearch</strong> indices to <strong>snapshots</strong></span>}
                     required />
            </fieldset>
          </div>


          <div>
            <label className="control-label">Select the elasticsearch repository name</label>
          </div>

          <div>
          <select className="form-control" id="repo_name">
              {this.state.names.map(function(name, index){
                return <option key={ index }>{name}</option>;
              })}
          </select>
          </div>
        </div>
    );
  },
});

export default ArchiveRetentionConfiguration;
