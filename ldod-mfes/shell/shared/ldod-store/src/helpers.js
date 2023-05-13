/** @format */

export const getStorageItem = name => JSON.parse(sessionStorage.getItem(name));
export const setStorageItem = (name, data) => sessionStorage.setItem(name, JSON.stringify(data));
export const removeStorageItem = name => sessionStorage.removeItem(name);

export function setPartialStorage(name, data) {
	const storage = getStorageItem(name) || {};
	Object.entries(data).forEach(([key, val]) => (storage[key] = val));
	removeStorageItem(name);
	setStorageItem(name, storage);
}

export function areObjectsEqual(objOne, objeTwo) {
	if (!areObjects(objOne, objeTwo)) return;
	if (Object.values(objOne).length !== Object?.values(objeTwo).length) return;

	return Object.entries(objOne).every(([key, val], index) => {
		const [key2, val2] = Object.entries(objeTwo)[index];
		if (areObjects(val, val2)) return key === key2 && areObjectsEqual(val, val2);
		return key === key2 && val === val2;
	});
}

export function getPartialStorage(storageName, keys) {
	let storage = globalThis.sessionStorage.getItem(storageName);
	if (!storage) return;
	return filterObjectByKeys(JSON.parse(storage), keys);
}

export function filterObjectByKeys(obj, keys) {
	return Array.from(keys).reduce((prev, key) => {
		if (Object.keys(obj).includes(key)) prev[key] = obj[key];
		return prev;
	}, {});
}

function areObjects(o1, o2) {
	return isObject(o1) && isObject(o2);
}
function isObject(o) {
	return o && typeof o === 'object';
}
