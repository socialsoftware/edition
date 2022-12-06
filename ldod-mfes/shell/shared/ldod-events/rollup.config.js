// rollup.config.js
import { terser } from 'rollup-plugin-terser';
import sourcemaps from 'rollup-plugin-sourcemaps';

export default {
  input: './src/ldod-events.js',
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
