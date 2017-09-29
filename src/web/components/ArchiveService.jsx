import Reflux from 'reflux';


import ArchiveActions from 'components/ArchiveActions'


import UserNotification from 'util/UserNotification';
import URLUtils from 'util/URLUtils';
import fetch from 'logic/rest/FetchProvider';

const urlPrefix = '/plugins/org.graylog.plugins.backup';

const ArchiveServices = Reflux.createStore({
    listenables: [ArchiveActions],

    getInitialState() {
        return {
            config: undefined,
        };
    },

    _errorHandler(message, title, cb) {
        return (error) => {
            let errorMessage;
            try {
                errorMessage = error.additional.body.message;
            } catch (e) {
                errorMessage = error.message;
            }
            UserNotification.error(`${message}: ${errorMessage}`, title);
            if (cb) {
                cb(error);
            }
        };
    },

    _url(path) {
        return URLUtils.qualifyUrl(`${urlPrefix}${path}`);
    },

    //this function will create a els snapshots, we can do it with declaring Java class 
    createSnapshot() {
        console.log("snapshot requested");
        const promise = fetch('POST', this._url('/createsnapshot'));
        promise.then((response) => {
            this.trigger({ config: response });
            UserNotification.success('Configuration restore was completed successfully');
        }, this._errorHandler('Archive snapshot failed', 'Unable to create snapshot config'));

        ArchiveActions.launchRestore.promise(promise);
    },


});

export default ArchiveServices;
