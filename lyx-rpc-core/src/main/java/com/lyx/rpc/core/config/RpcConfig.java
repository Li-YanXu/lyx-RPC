package com.lyx.rpc.core.config;

import com.lyx.rpc.core.serializer.SerializerKeys;
import lombok.Data;

@Data
public class RpcConfig {
    private String name = "lyx-rpc";
    private String version = "1.0";
    private String serverHost = "localhost";
    private Integer port = 8080;
    private boolean mock = false;
    private String serializer = SerializerKeys.JDK;
    private RegistryConfig registryConfig = new RegistryConfig();
}
