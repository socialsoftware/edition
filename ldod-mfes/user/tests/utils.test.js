import 'vitest';
import { expect, test } from 'vitest';
import { capitalizeFirstLetter } from '../src/utils';

test('capitalizing words', () => {
  expect(capitalizeFirstLetter('')).toBe('');
  expect(capitalizeFirstLetter('nonCapitalized')).toBe('NonCapitalized');
  expect(capitalizeFirstLetter('one test')).toBe('One test');
  expect(capitalizeFirstLetter('Capitalized')).toBe('Capitalized');
});
