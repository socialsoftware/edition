/** @format */

import { headerData } from './header-data';
import { ldodEventBus } from '@shared/ldod-events.js';

customElements.whenDefined('nav-bar').then(() => ldodEventBus.publish('ldod:header', headerData));
