
const path = require('path');
const webpack = require('webpack');
const ExtractTextPlugin = require('extract-text-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const InterpolateHtmlPlugin = require('react-dev-utils/InterpolateHtmlPlugin');
const autoprefixer = require('autoprefixer');

/**
 * webpack.config.js 可以 export 一个 object，或者是一个 env 为参数的 function
 */
module.exports = function (env) {
    /**
     * 判断是否是生产环境。 当命令行运行 `webpack --env.production` 时， env.production 的值为 true
     * https://webpack.js.org/configuration/configuration-types/#exporting-a-function-to-use-env
     */
    const isProduction = env && env.production === true;

    return {
        // 设定代码的入口，入口可以是一个或者多个，即可以传入一个 array
        // https://webpack.js.org/configuration/entry-context/
        context: path.resolve(__dirname, 'src'),
        entry: './index.js',

        // 打包好的代码存放的位置和文件名
        // 这里的 [name].[hash].js 的格式意味着会自动生成一个带 hash 的文件名
        // https://webpack.js.org/configuration/output/
        output: {
            path: path.resolve(__dirname, 'build'),
            filename: 'static/js/[name].[hash].js',
            publicPath: '/',
        },

        module: {
            /**
             *  loader 相当于一个预处理器。
             *  因为正常来说，webpack 只能解析 js，因此，如果过我们要引入非 js 的模块
             *  就需要一个预处理器来告诉 webpack 该如何处理这个文件，比如 css、图片等
             *  https://webpack.js.org/concepts/loaders/
             */
            loaders: [
                /**
                 * eslint-loader
                 * 在构建之前，自动进行 eslint 检查
                 * 这里的配置设定为，如果有 error，那么就不能继续构建
                 * https://github.com/MoOx/eslint-loader
                 */
                {
                    enforce: 'pre',
                    test: /\.js$/,
                    include: path.resolve(__dirname, 'src'),
                    loader: 'eslint-loader',
                    options: {
                        failOnError: true,
                        fix: true,
                    },
                },
                /**
                 * babel-loader
                 * 将 es6 转化为 es5
                 * https://github.com/babel/babel-loader
                 */
                {
                    test: /\.(js|jsx)$/,
                    include: path.resolve(__dirname, 'src'),
                    loader: 'babel-loader',
                    query: {
                        babelrc: false,
                        presets: [require.resolve('babel-preset-react-app')],
                    },
                },
                /**
                 * css-loader & style-loader
                 *  用来处理代码中 import './style.css' 的情况
                 *  如果是开发环境，那么作为 style 标签插入到 html 中
                 *  如果是正式环境，那么生成单独的文件，作为 link 插入的 html 中
                 *  https://github.com/webpack-contrib/css-loader
                 *  https://github.com/webpack-contrib/style-loader
                 */
                {
                    test: /\.css$/,
                    loader: isProduction ?
                        ExtractTextPlugin.extract({ fallback: 'style-loader',
                            use: [
                                {
                                    loader: require.resolve('css-loader'),
                                },
                                {
                                    loader: require.resolve('postcss-loader'),
                                    options: {
                                        plugins: () => [
                                            require('postcss-flexbugs-fixes'),
                                            autoprefixer({
                                                browsers: [
                                                    '>1%',
                                                    'last 4 versions',
                                                    'Firefox ESR',
                                                    'not ie < 9',
                                                ],
                                                flexbox: 'no-2009',
                                            }),
                                        ],
                                    },
                                },
                            ] }) :
                        ['style-loader', 'css-loader'],
                },
                /**
                 * json-loader
                 * https://github.com/webpack-contrib/json-loader
                 */
                {
                    test: /\.json$/,
                    loader: 'json-loader',
                },
                // "file" loader for svg
                // https://github.com/webpack-contrib/file-loader
                {
                    test: /\.(ico|jpg|jpeg|png|gif|eot|otf|webp|svg|ttf|woff|woff2)(\?.*)?$/,
                    loader: 'file-loader',
                },
            ],
        },
        /**
         * plugin 是 webpack 完成各种复杂功能的核心
         * 比如，这里面用的 HtmlWebpackPlugin，即可将生成的 js，生成 script 标签插入到指定的 html 里
         * 又比如，ExtractTextPlugin 可以加 css 生成 link 插入
         * https://webpack.js.org/concepts/plugins/
         */
        plugins: [
            new InterpolateHtmlPlugin({
                PUBLIC_URL: '', // 用于替换 index.html 里面的 %PUBLIC_URL%
            }),
            new HtmlWebpackPlugin({
                inject: true,
                template: '../public/index.html',
                minify: {
                    removeComments: true,
                    collapseWhitespace: true,
                    removeRedundantAttributes: true,
                    useShortDoctype: true,
                    removeEmptyAttributes: true,
                    removeStyleLinkTypeAttributes: true,
                    keepClosingSlash: true,
                    minifyJS: true,
                    minifyCSS: true,
                    minifyURLs: true,
                },
            }),
            new ExtractTextPlugin('static/css/[name].[hash].css'),
            new webpack.HotModuleReplacementPlugin(), // 用于热加载
            // 可以替换代码中的变量
            // https://webpack.js.org/plugins/define-plugin/#use-case-service-urls
            new webpack.DefinePlugin({
                SERVICE_URL: isProduction ?
                    JSON.stringify('http://pro.example.com') :
                    JSON.stringify('http://dev.example.com'),
                'process.env': {
                    NODE_ENV: isProduction ?
                        JSON.stringify('production') :
                        JSON.stringify('development'),
                },
            }),
            new webpack.ProvidePlugin({
                $: 'jquery',
                jQuery: 'jquery',
            }),
        ],
        /**
         * webpack 自带的开发 server，配合 webpack-dev-server 命令使用
         * https://webpack.js.org/guides/development/#webpack-dev-server
         * https://webpack.js.org/configuration/dev-server/
         */
        devServer: {
            hot: true,
            contentBase: [path.join(__dirname, 'public'),path.join(__dirname, 'src')],
            compress: true,
            port: 9000,
            publicPath: '/',
            // 设置代理，比如，请求 /api/abc 会代理制 http://localhost:7000/abc
            // https://webpack.js.org/configuration/dev-server/#devserver-proxy
            // https://github.com/chimurai/http-proxy-middleware#http-proxy-middleware-options
            proxy: {
                '/api': {
                    target: 'http://localhost:7000/',
                    pathRewrite: { '^/api': '' },
                    secure: false,
                    changeOrigin: true,
                },
            },
            historyApiFallback: true,
        },
        // 为压缩以后的代码提供 source map 方便调试
        // https://webpack.js.org/configuration/devtool/
        devtool: isProduction ?
            'hidden-source-map' :
            'cheap-module-eval-source-map',
    };
};
