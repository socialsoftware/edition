// rollup.config.js

import { terser } from 'rollup-plugin-terser';
import { importAssertions } from 'acorn-import-assertions';
import { importAssertionsPlugin } from 'rollup-plugin-import-assert';

export default {
  input: './src/shell.js',
  output: [
    {
      dir: 'dist',
      format: 'es',
      plugins: [terser()],
    },
  ],
  acornInjectPlugins: [importAssertions],
  plugins: [importAssertionsPlugin()],
};
