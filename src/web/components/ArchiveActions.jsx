import Reflux from 'reflux';

//actions restore/create snapshot for elasticsearch
const ArchiveActions = Reflux.createActions({
    createSnapshot: { asyncResult: true },
});

export default ArchiveActions;
