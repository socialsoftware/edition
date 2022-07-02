const { importAssertions } = require('acorn-import-assertions');
const { importAssertionsPlugin } = require('./dist/import-assert.cjs');

module.exports = {
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
