/** @format */

import { isAdmin, store } from './store';

let Navbar = await customElements.whenDefined('nav-bar');
store.subscribe(changeAdminVisibility, 'user', { fire: true });
function changeAdminVisibility() {
	new Navbar().setMenuVis('admin', !isAdmin());
}
