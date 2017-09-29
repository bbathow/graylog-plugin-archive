import React from 'react';

const RepositorySelect = React.createClass({
  getValue() {
    return this.refs.session_timeout_unit.value;
  },
  render() {
    return (
      <select className="form-control" ref="session_timeout_unit" {...this.props}>
        <option value="">repo_backup</option>
      </select>
    );
  },
});

export default RepositorySelect;
