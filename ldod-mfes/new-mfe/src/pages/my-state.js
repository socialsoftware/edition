/** @format */

import { LitState } from '@vendor/lit-element-state_1.7.0/lit-state.js';

class MyState extends LitState {
	static get stateVars() {
		return {
			counter: 0,
		};
	}
}

export const myState = new MyState();
