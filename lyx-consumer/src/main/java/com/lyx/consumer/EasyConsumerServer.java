package com.lyx.consumer;

import com.lyx.common.model.User;
import com.lyx.common.service.UserService;
import com.lyx.rpc.core.config.RpcConfig;
import com.lyx.rpc.core.proxy.ServiceProxyFactory;
import com.lyx.rpc.core.utils.ConfigUtil;

public class EasyConsumerServer {
    public static void main(String[] args) {

        RpcConfig rpcConfig = ConfigUtil.loadConfig(RpcConfig.class, "rpc");
        System.out.println(rpcConfig);
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setUserName("lyx");
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(newUser);
        } else {
            System.out.println("newUser为空");
        }
        System.out.println("age = "+userService.getAge());
    }
}
