import React from 'react';
import PropTypes from 'prop-types';

const ArchiveRetentionStrategySummary = React.createClass({
  propTypes: {
    config: React.PropTypes.object.isRequired,
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

  render() {
    return (
      <div>
        <dl>
          <dt>Archive retention strategy:</dt>
          <dd>Message Count</dd>
          <dt>Max docs per index:</dt>
          <dd>{this.state.config.max_number_of_indices}</dd>
        </dl>
      </div>
    );
  },
});

export default ArchiveRetentionStrategySummary;
