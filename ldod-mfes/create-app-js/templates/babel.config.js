module.exports = (api) => {
  const isTest = api.env('test');

  if (isTest)
    return {
      presets: [['@babel/preset-env', { targets: { node: 'current' } }]],
    };

  return {
    presets: [
      [
        '@babel/preset-react',
        {
          pragma: 'createElement', // default pragma is React.createElement (only in classic runtime)
          pragmaFrag: 'createFragment', // default is React.Fragment (only in classic runtime)
        },
      ],
    ],
  };
};
