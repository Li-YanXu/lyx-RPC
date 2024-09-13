package com.lyx.rpc.core;

import com.lyx.rpc.core.config.RegistryConfig;
import com.lyx.rpc.core.config.RpcConfig;
import com.lyx.rpc.core.constant.RpcConstant;
import com.lyx.rpc.core.registry.Registry;
import com.lyx.rpc.core.registry.RegistryFactory;
import com.lyx.rpc.core.utils.ConfigUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;

    /**
     * 框架初始化，支持传入自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
        log.info("rpc init, config = {}", newRpcConfig.toString());
        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        registry.init(registryConfig);
        log.info("registry init, config = {}", registryConfig);

        Runtime.getRuntime().addShutdownHook(new Thread(registry::destroy));
    }


    public static void init(){
        RpcConfig newRpcConfig = null;
        try {
            newRpcConfig = ConfigUtil.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        } catch (Exception e) {
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig(){
        if (rpcConfig == null){
            synchronized(RpcApplication.class){
                if (rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
