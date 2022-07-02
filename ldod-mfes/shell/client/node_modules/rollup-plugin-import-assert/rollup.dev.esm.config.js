import { importAssertions } from 'acorn-import-assertions';
import { importAssertionsPlugin } from './dist/import-assert.js';

export default {
    input: 'public/index.js',
    output: {
        format: 'esm',
        dir: 'lib'
    },
    acornInjectPlugins: [importAssertions],
    plugins: [
        importAssertionsPlugin()
    ]
}
