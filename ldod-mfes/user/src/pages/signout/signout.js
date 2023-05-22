/** @format */

import { logoutPublisher } from '../../events-modules';
import { getState, setState } from '../../store';
import { navigateTo } from '@core';
import { userReferences } from '../../user-references';

const mount = () => {
	if (!(getState().user || getState().token)) return;
	setState({ token: '', user: '' });
	logoutPublisher();
	navigateTo(userReferences.signin());
};

const unMount = () => {};

const path = '/signout';

export { mount, unMount, path };
