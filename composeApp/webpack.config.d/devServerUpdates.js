config.devServer = {
    ...config.devServer,
    historyApiFallback: {
        index: "/index.html",
        rewrites: []
    },
};