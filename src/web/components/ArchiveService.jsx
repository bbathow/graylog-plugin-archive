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
});

export default ArchiveServices;
