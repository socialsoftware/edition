// rollup.config.js

import { terser } from 'rollup-plugin-terser';
import { importAssertions } from 'acorn-import-assertions';
import { importAssertionsPlugin } from 'rollup-plugin-import-assert';
import dynamicImportVars from '@rollup/plugin-dynamic-import-vars';

const cwd = () => {
  const cwd = process.cwd();
  const array = cwd.split('/');
  return array[array.length - 1];
};

export default {
  input: 'build/user.js',
  output: [
    {
      dir: cwd(),
      format: 'es',
      plugins: [terser()],
    },
  ],
  acornInjectPlugins: [importAssertions],
  plugins: [importAssertionsPlugin(), dynamicImportVars()],
};
