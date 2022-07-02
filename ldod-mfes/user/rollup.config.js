// rollup.config.js

import { terser } from 'rollup-plugin-terser';
import { importAssertions } from 'acorn-import-assertions';
import { importAssertionsPlugin } from 'rollup-plugin-import-assert';
import dynamicImportVars from '@rollup/plugin-dynamic-import-vars';

export default {
  input: 'transpiled-src/user.js',
  output: [
    {
      dir: 'build',
      format: 'es',
      plugins: [terser()],
    },
  ],
  acornInjectPlugins: [importAssertions],
  plugins: [importAssertionsPlugin(), dynamicImportVars()],
};
