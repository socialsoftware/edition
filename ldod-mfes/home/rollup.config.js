// rollup.config.js

import { terser } from 'rollup-plugin-terser';
import dynamicImportVars from '@rollup/plugin-dynamic-import-vars';
import alias from '@rollup/plugin-alias';
import css from 'rollup-plugin-import-css';
import minifyHTML from 'rollup-plugin-minify-html-literals';

export default {
  input: './src/home.js',
  external: [/^shared/, 'user'],
  output: [
    {
      dir: 'build',
      format: 'es',
      sourcemap: true,
      plugins: [terser()],
    },
  ],
  plugins: [
    minifyHTML(),
    css(),
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
