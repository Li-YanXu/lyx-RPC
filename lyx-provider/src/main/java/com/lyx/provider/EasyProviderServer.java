package com.lyx.provider;

import com.lyx.common.service.UserService;
import com.lyx.rpc.core.RpcApplication;
import com.lyx.rpc.core.registry.LocalRegistry;
import com.lyx.rpc.core.server.HttpServer;
import com.lyx.rpc.core.server.VertxHttpServer;

public class EasyProviderServer {
    public static void main(String[] args) {
        RpcApplication.init();
        LocalRegistry.register(UserService.class.getName(), UserServiceImpl.class);
        // 提供服务
        HttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(RpcApplication.getRpcConfig().getPort());
    }
}
