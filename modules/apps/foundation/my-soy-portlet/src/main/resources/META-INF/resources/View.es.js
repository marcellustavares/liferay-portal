import Modal from 'metal-dropdown/src/Dropdown';
import ViewBase from './View.soy';

class View extends ViewBase {
	attached() {
		console.log('custom view');
	}
}

export default View;