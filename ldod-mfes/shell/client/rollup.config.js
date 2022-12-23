// rollup.config.js

//import terser from '@rollup/plugin-terser';
import { terser } from 'rollup-plugin-terser';
import css from 'rollup-plugin-import-css';

export default {
  external: [/^shared/, 'home'],
  input: 'src/shell.js',
  output: [
    {
      dir: 'dist',
      format: 'es',
      plugins: [terser()],
      sourcemap: true,
    },
  ],
  plugins: [css()],
};
