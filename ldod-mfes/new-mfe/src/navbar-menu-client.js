/** @format */

import { headerData } from './references';

customElements.whenDefined('nav-bar').then(() => eventBus.publish('ldod:header', headerData));
