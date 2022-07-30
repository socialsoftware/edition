import {
  addEndSlash,
  addStartSlash,
  addSlashes,
  removeStartSlash,
  removeEndSlash,
  removeSlashes,
} from '../src/utils.js';

describe('when invoking addStartSlash', () => {
  it('passing an undefined path it should return undefined.', () => {
    expect(addStartSlash(undefined)).toBeUndefined();
  });
  it('passing a null path it should return undefined', () => {
    expect(addStartSlash(null)).toBeUndefined();
  });
  it('passing a empty path it should return undefined', () => {
    expect(addStartSlash('')).toBeUndefined();
  });
  it('passing a false path it should return undefined', () => {
    expect(addStartSlash(false)).toBeUndefined();
  });

  it('passing an empty space path it should return undefined', () => {
    expect(addStartSlash(' ')).toBeUndefined();
  });

  it('passing a slashed path in begining should return the same path', () => {
    expect(addStartSlash('/path')).toBe('/path');
  });

  it('passing a non slashed path in begining should return the same path but slashed', () => {
    expect(addStartSlash('path')).toBe('/path');
  });

  it('passing a slashed path should return the same path', () => {
    expect(addStartSlash('/')).toBe('/');
  });
});

describe('when invoking addEndSlash', () => {
  it('passing an undefined path it should return undefined.', () => {
    expect(addEndSlash(undefined)).toBeUndefined();
  });
  it('passing a null path it should return undefined', () => {
    expect(addEndSlash(null)).toBeUndefined();
  });
  it('passing a empty path it should return undefined', () => {
    expect(addEndSlash('')).toBeUndefined();
  });
  it('passing a false path it should return undefined', () => {
    expect(addEndSlash(false)).toBeUndefined();
  });

  it('passing an empty space path it should return undefined', () => {
    expect(addEndSlash(' ')).toBeUndefined();
  });

  it('passing a slashed path in begining should return the same path', () => {
    expect(addEndSlash('path/')).toBe('path/');
  });

  it('passing a non slashed path in begining should return the same path but slashed', () => {
    expect(addEndSlash('path')).toBe('path/');
  });
  it('passing a slashed path should return the same path', () => {
    expect(addStartSlash('/')).toBe('/');
  });
});

describe('when invoking addSlashes', () => {
  it('passing an undefined path it should return undefined.', () => {
    expect(addSlashes(undefined)).toBeUndefined();
  });
  it('passing a null path it should return undefined', () => {
    expect(addSlashes(null)).toBeUndefined();
  });
  it('passing a empty path it should return undefined', () => {
    expect(addSlashes('')).toBeUndefined();
  });
  it('passing a false path it should return undefined', () => {
    expect(addSlashes(false)).toBeUndefined();
  });

  it('passing an empty space path it should return undefined', () => {
    expect(addSlashes(' ')).toBeUndefined();
  });

  it('passing a slashed path in end  should return the same path', () => {
    expect(addSlashes('path/')).toBe('/path/');
  });

  it('passing a slashed path in begining should return the same path', () => {
    expect(addSlashes('/path')).toBe('/path/');
  });

  it('passing a non slashed path in begining should return the same path but slashed', () => {
    expect(addSlashes('path')).toBe('/path/');
  });
  it('passing a slashed path should return the same path', () => {
    expect(addStartSlash('/')).toBe('/');
  });
});

describe('when invoking removeStartSlash', () => {
  it('passing an undefined path it should return undefined.', () => {
    expect(removeStartSlash(undefined)).toBeUndefined();
  });
  it('passing a null path it should return undefined', () => {
    expect(removeStartSlash(null)).toBeUndefined();
  });
  it('passing a empty path it should return undefined', () => {
    expect(removeStartSlash('')).toBeUndefined();
  });
  it('passing a false path it should return undefined', () => {
    expect(removeStartSlash(false)).toBeUndefined();
  });

  it('passing an empty space path it should return undefined', () => {
    expect(removeStartSlash(' ')).toBeUndefined();
  });

  it('passing a slashed path in begining should return the path without slash', () => {
    expect(removeStartSlash('/path')).toBe('path');
  });

  it('passing a non slashed path in begining should return the same path', () => {
    expect(removeStartSlash('path')).toBe('path');
  });

  it('passing a slashed path should return the same path', () => {
    expect(removeStartSlash('/')).toBe('/');
  });
});

describe('when invoking removeEndSlash', () => {
  it('passing an undefined path it should return undefined.', () => {
    expect(removeEndSlash(undefined)).toBeUndefined();
  });
  it('passing a null path it should return undefined', () => {
    expect(removeEndSlash(null)).toBeUndefined();
  });
  it('passing a empty path it should return undefined', () => {
    expect(removeEndSlash('')).toBeUndefined();
  });
  it('passing a false path it should return undefined', () => {
    expect(removeEndSlash(false)).toBeUndefined();
  });

  it('passing an empty space path it should return undefined', () => {
    expect(removeEndSlash(' ')).toBeUndefined();
  });

  it('passing a slashed path in end should return the path without slash', () => {
    expect(removeEndSlash('path/')).toBe('path');
  });

  it('passing a non slashed path in beginin should return the same path', () => {
    expect(removeEndSlash('path')).toBe('path');
  });

  it('passing a slashed path should return the same path', () => {
    expect(removeEndSlash('/')).toBe('/');
  });
});

describe('when invoking removeSlashes', () => {
  it('passing an undefined path it should return undefined.', () => {
    expect(removeSlashes(undefined)).toBeUndefined();
  });
  it('passing a null path it should return undefined', () => {
    expect(removeSlashes(null)).toBeUndefined();
  });
  it('passing a empty path it should return undefined', () => {
    expect(removeSlashes('')).toBeUndefined();
  });
  it('passing a false path it should return undefined', () => {
    expect(removeSlashes(false)).toBeUndefined();
  });

  it('passing an empty space path it should return undefined', () => {
    expect(removeSlashes(' ')).toBeUndefined();
  });

  it('passing a slashed path in end should return the path without slash', () => {
    expect(removeSlashes('path/')).toBe('path');
  });

  it('passing a slashed path in begin should return the path without slash', () => {
    expect(removeSlashes('/path')).toBe('path');
  });

  it('passing a non slashed path  should return the same path', () => {
    expect(removeSlashes('path')).toBe('path');
  });

  it('passing a slashed path should return the same path', () => {
    expect(removeSlashes('/')).toBe('/');
  });
});
