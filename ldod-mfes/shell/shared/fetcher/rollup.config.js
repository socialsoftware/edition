// rollup.config.js
import { terser } from 'rollup-plugin-terser';

//import terser from '@rollup/plugin-terser';
export default {
  input: 'src/fetcher.js',
  external: /^shared/,
  output: {
    sourcemap: true,
    dir: '../dist',
    format: 'es',
    plugins: [terser()],
  },
};
