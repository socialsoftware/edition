import { workerData, parentPort } from 'node:worker_threads';

import(workerData).then((data) => {
  const api = data.default;
  const apiKeys = Object.keys(api);
  const contains = (key) => apiKeys.indexOf(key) !== -1;

  const checkApiKey = (key, type = 'function') => {
    if (!contains(key) || typeof api[key] !== type)
      parentPort.postMessage(`The MFE API must supply a ${key} as a ${type}`);
  };
  const checkReferences = () => {
    if (!contains("references")) return
    if (Object.values(api.references).some(reference => typeof reference !== "function"))
      parentPort.postMessage(`The references should be encapsulated on a function`)
  }


  checkApiKey('path', 'string');
  checkReferences()
  checkApiKey('mount');
  checkApiKey('unMount');
});
