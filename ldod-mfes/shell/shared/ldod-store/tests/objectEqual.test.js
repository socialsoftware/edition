import { objectsAreEqual } from '../src/utils.js';

describe('Store class instantiation unpersisted with object as state', () => {
  it('two empty objects are equal', () => {
    const obj1 = {};
    const obj2 = {};
    expect(objectsAreEqual(obj1, obj2)).toBeTruthy();
  });

  it('one empty object and undefined', () => {
    const obj1 = {};
    const obj2 = undefined;
    expect(objectsAreEqual(obj1, obj2)).toBeFalsy();
  });

  it('objects with same values but with different keys', () => {
    const obj1 = {
      key1: 'value1',
      key2: 'value2',
    };
    const obj2 = {
      key2: 'value1',
      key1: 'value2',
    };
    expect(objectsAreEqual(obj1, obj2)).toBeFalsy();
  });

  it('objects with same keys but with different values', () => {
    const obj1 = {
      key1: 'value1',
      key2: 'value2',
    };
    const obj2 = {
      key1: 'value2',
      key2: 'value2',
    };
    expect(objectsAreEqual(obj1, obj2)).toBeFalsy();
  });

  it('objects with same values  and keys', () => {
    const obj1 = {
      key1: 'value1',
      key2: 'value2',
    };
    const obj2 = {
      key1: 'value1',
      key2: 'value2',
    };
    expect(objectsAreEqual(obj1, obj2)).toBeTruthy();
  });

  it('copy object and then change value', () => {
    const obj1 = {
      key1: 'value1',
      key2: 'value2',
    };
    const obj2 = obj1;
    obj2.key1 = 'value2';
    expect(objectsAreEqual(obj1, obj2)).toBeTruthy();
  });

  it('copy object and then change value', () => {
    const val = {
      key1: 'val1',
    };
    const obj1 = {
      key1: val,
      key2: 'value2',
    };

    const obj2 = {
      key1: val,
      key2: 'value4',
    };

    expect(objectsAreEqual(obj1, obj2)).toBeFalsy();
  });
});
