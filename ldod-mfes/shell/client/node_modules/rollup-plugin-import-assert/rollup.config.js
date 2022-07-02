import { nodeResolve } from '@rollup/plugin-node-resolve';
import commonjs from '@rollup/plugin-commonjs';

export default {
    input: 'dist/import-assert.js',
    output: {
        format: 'cjs',
        file: 'dist/import-assert.cjs'
    },
    plugins: [
        nodeResolve({
            preferBuiltins: true
        }),
        commonjs()

    ]
}
