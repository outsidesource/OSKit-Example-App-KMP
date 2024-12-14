config.devServer = {
    ...config.devServer,
    historyApiFallback: {
        index: "/index.html",
        rewrites: [
            { from: "./ixd-connect.wasm", to: "/ixd-connect.wasm" }
        ]
    },
};