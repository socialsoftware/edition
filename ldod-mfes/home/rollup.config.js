// rollup.config.js

import { terser } from 'rollup-plugin-terser';
import sourcemaps from 'rollup-plugin-sourcemaps';
import dynamicImportVars from '@rollup/plugin-dynamic-import-vars';
import alias from '@rollup/plugin-alias';
import css from 'rollup-plugin-import-css';

export default {
  input: './src/home.js',
  external: ['shared/utils.js', 'shared/ldod-events.js'],
  output: [
    {
      dir: 'build',
      format: 'es',
      sourcemap: true,
      plugins: [terser()],
    },
  ],
  plugins: [
    css(),
    sourcemaps(),
    dynamicImportVars({ exclude: ['./src/external-deps.js'] }),
    alias({
      entries: [
        {
          find: 'shared',
          replacement: './node_modules/shared/dist',
        },
      ],
    }),
  ],
};
