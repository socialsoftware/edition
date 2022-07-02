const getLocalStorageState = (stateName) => {
  const storage = localStorage.getItem(stateName);
  return storage ? JSON.parse(storage) : storage;
};

const filterObjectByKeys = (obj, keys) =>
  Array.from(keys).reduce((prev, key) => {
    if (Object.keys(obj).includes(key)) prev[key] = obj[key];
    return prev;
  }, {});

const getPartialStorage = (storageName, keys) => {
  let storage = localStorage.getItem(storageName);
  if (!storage) return;
  return filterObjectByKeys(JSON.parse(storage), keys);
};

const setLocalStorageState = (name, state) =>
  localStorage.setItem(name, JSON.stringify(state));

const removeLocalStorage = (name) => localStorage.removeItem(name);

const setPartialStorage = (name, state) => {
  let storage = getLocalStorageState(name);
  storage = storage ? storage : {};
  Object.entries(state).forEach(([key, val]) => (storage[key] = val));
  removeLocalStorage(name);
  setLocalStorageState(name, storage);
};

const objectsAreEqual = (objOne, objeTwo) => {
  const areObjects = (o1, o2) => isObject(o1) && isObject(o2);
  const isObject = (o) => o && typeof o === 'object';
  const areEqual = (val1, val2) => val1 === val2;

  if (
    !areObjects(objOne, objeTwo) ||
    Object.values(objOne).length !== Object?.values(objeTwo).length
  )
    return false;

  return Object.entries(objOne).every(([key, val], index) => {
    const [key2, val2] = Object.entries(objeTwo)[index];
    if (areObjects(val, val2))
      return areEqual(key, key2) && objectsAreEqual(val, val2);
    return key === key2 && val === val2;
  });
};

export {
  objectsAreEqual,
  getLocalStorageState,
  setLocalStorageState,
  getPartialStorage,
  setPartialStorage,
  filterObjectByKeys,
};
