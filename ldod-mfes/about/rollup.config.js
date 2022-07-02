// rollup.config.js
import { terser } from 'rollup-plugin-terser';
import dynamicImportVars from '@rollup/plugin-dynamic-import-vars';
import babel from '@rollup/plugin-babel';

export default {
  input: 'index.js',
  output: [
    {
      dir: 'public',
      format: 'es',
      plugins: [terser()],
    },
  ],
  plugins: [dynamicImportVars({})],
};
