/** @format */

import {
	filterObjectByKeys,
	getPartialStorage,
	setPartialStorage,
	areObjectsEqual,
} from './helpers';

export class Store {
	#listenersBySelector;
	#persist;
	#listeners;
	#state;
	constructor(state, { storageName, keys } = {}) {
		this.#state = typeof state === 'object' ? state : state ? { state } : {};
		this.#listenersBySelector = new Map();
		this.#listeners = new Set();
		this.#persist = undefined;
		if (storageName && keys) {
			this.#persist = { storageName, keys };
			const storage = getPartialStorage(storageName, keys);
			storage
				? this.setState(storage, true)
				: setPartialStorage(storageName, filterObjectByKeys(this.#state, keys));
		}
	}

	subscribe(listener, selector, options = {}) {
		if (!selector) return this.subscribeAll(listener, options);
		if (this.#listenersBySelector.has(selector))
			this.#listenersBySelector.get(selector).add(listener);
		else this.#listenersBySelector.set(selector, new Set([listener]));
		if (options.fire) listener(this.#state[selector], this.#state[selector]);
		return () => this.#listenersBySelector.get(selector).delete(listener); //unsub
	}

	subscribeAll(listener, options) {
		this.#listeners.add(listener);
		if (options.fire) listener(this.#state, this.#state);
		return () => this.#listeners.delete(listener);
	}

	destroy() {
		this.#listenersBySelector.clear();
		this.#listeners.clear();
	}

	getState(selector) {
		return selector ? this.#state[selector] : this.#state;
	}

	setState(state, replace) {
		if (!state) return;
		state = checkStateType(state);
		const nextState = this.setNextStateAccordingType(state);

		if (!areObjectsEqual(nextState, this.#state)) {
			const previousState = this.#state;
			this.#state = replace ? nextState : { ...this.#state, ...nextState };

			if (this.#persist?.storageName && this.#persist?.keys) {
				setPartialStorage(
					this.#persist.storageName,
					filterObjectByKeys(this.#state, this.#persist.keys)
				);
			}
			Object.keys(nextState).forEach(key =>
				this.#listenersBySelector
					.get(key)
					?.forEach(listener => listener(this.#state[key], previousState[key]))
			);
			this.#listeners.forEach(listener => listener(this.#state, previousState));
		}
	}

	invokeListener(prevState, listener, selector) {
		listener(selector ? prevState[selector] : prevState, this.#state);
	}

	setNextStateAccordingType(state) {
		return typeof state === 'function'
			? (() => {
					let newState = state(this.#state);
					return newState || this.#state;
			  })()
			: state;
	}
}
function checkStateType(state) {
	state = typeof state === 'object' || typeof state === 'function' ? state : { state };
	return state;
}
