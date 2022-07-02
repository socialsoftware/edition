import { filterObjectByKeys } from '../src/utils.js';

describe('filering and creating new object by keys', () => {
  it('filter object that contains the keys', () => {
    const object = {
      lang: 'pt',
      user: { name: 'Raimundo', admin: true },
      token: true,
    };
    const keys = ['lang', 'token'];
    const emptyKeys = [];
    const fullkeys = ['lang', 'token', 'user'];
    let result = filterObjectByKeys(object, keys);
    expect(Object.keys(result).length).toStrictEqual(2);
    expect(Object.entries(result)).toStrictEqual([
      ['lang', 'pt'],
      ['token', true],
    ]);
    let emptyResult = filterObjectByKeys(object, emptyKeys);
    expect(Object.keys(emptyResult).length).toStrictEqual(0);
    expect(Object.entries(emptyResult)).toStrictEqual([]);

    let fullResult = filterObjectByKeys(object, fullkeys);
    expect(Object.keys(fullResult).length).toStrictEqual(3);
  });

  it('filter object that is empty', () => {
    const object = {};
    const keys = ['lang', 'token'];

    let result = filterObjectByKeys(object, keys);
    expect(result).toStrictEqual({});
  });

  it('filter object that not contains the keys', () => {
    const object = {
      user: { name: 'Raimundo', admin: true },
    };
    const keys = ['lang', 'token'];

    let result = filterObjectByKeys(object, keys);
    expect(result).toStrictEqual({});
  });
});
