// rollup.config.js
import sourcemaps from 'rollup-plugin-sourcemaps';
import { terser } from 'rollup-plugin-terser';

export default {
  input: './src/router.js',
  plugins: [sourcemaps()],
  output: [
    {
      sourcemap: true,
      dir: '../dist',
      format: 'es',
      plugins: [terser()],
    },
  ],
};
