package com.lyx.rpc.core.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.lyx.rpc.core.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpiLoader {
    private static final Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>();
    private static final Map<String, Object> instanceCache = new ConcurrentHashMap<>();
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    public static void loadALL(){
        log.info("加载所有 SPI");
        for (Class<?> tClass : LOAD_CLASS_LIST) {
            load(tClass);
        }
    }

    public static <T> T getInstance(Class<?> tClass, String key){
        final String tClassName = tClass.getName();
        final Map<String, Class<?>> classMap = loaderMap.get(tClassName);
        if (classMap == null){
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型", tClass));
        }
        if (!classMap.containsKey(key)){
            throw new RuntimeException(String.format("SpiLoader 的 %s 不存在 key = %s 的类型", tClassName, key));
        }
        final Class<?> implClass = classMap.get(key);
        final String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)){
            try {
                log.info("实例化 {} 类", implClassName);
                instanceCache.put(implClassName, implClass.newInstance());
            }catch (InstantiationException | IllegalAccessException e){
                final String errorMsg = String.format("%s 类实例化失败", implClassName);
                throw new RuntimeException(errorMsg, e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }

    public static Map<String, Class<?>> load(Class<?> tClass){
        log.info("加载类型为 {} 的 SPI",tClass.getName());
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanDir : SCAN_DIRS) {
            List<URL> resources = ResourceUtil.getResources(scanDir + tClass.getName());
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null){
                        String[] strArray = line.split("=");
                        if (strArray.length > 1){
                            String key = strArray[0];
                            String className = strArray[1];
                            keyClassMap.put(key, Class.forName(className));
                        }
                    }
                } catch (Exception e){
                    log.error("spi resource load error", e);
                }
            }
        }
        loaderMap.put(tClass.getName(), keyClassMap);
        return keyClassMap;
    }
}

