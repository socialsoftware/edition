const {
  override,
  fixBabelImports,
  addLessLoader,

  setWebpackPublicPath,
} = require('customize-cra');

module.exports = override(
  setWebpackPublicPath('ldod-mfes/ldod-visual'),
  fixBabelImports('import', {
    libraryName: 'antd',
    libraryDirectory: 'es',
    style: true, // change importing css to less
  }),
  addLessLoader({
    javascriptEnabled: true,
    modifyVars: { '@primary-color': '#1DA57A' },
  })
);
