import Reflux from 'reflux';

const ArchiveActions = Reflux.createActions({
    launchRestore: { asyncResult: true },
});

export default ArchiveActions;
