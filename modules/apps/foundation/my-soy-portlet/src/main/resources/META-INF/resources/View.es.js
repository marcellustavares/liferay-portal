import Modal from 'metal-popover/src/Popover';
import ViewBase from './View.soy.es';

class View extends ViewBase {
	attached() {
		console.log('custom view');
	}
}

export default View;