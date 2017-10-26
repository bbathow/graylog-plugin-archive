import React from 'react';
import PropTypes from 'prop-types';

const ArchiveRetentionStrategySummary = React.createClass({
  propTypes: {
    config: React.PropTypes.object.isRequired,
  },

  getInitialState() {
    return {
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
          <dt>Repository Name:</dt>
          <dd>{this.state.config.name_of_repository}</dd>
          <dt>Max index retention:</dt>
          <dd>{this.state.config.max_number_of_indices}</dd>
        </dl>
      </div>
    );
  },
});

export default ArchiveRetentionStrategySummary;
