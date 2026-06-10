/*
 * Copyright 2013-2019 the HotswapAgent authors.
 *
 * This file is part of HotswapAgent.
 *
 * HotswapAgent is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 2 of the License, or (at your
 * option) any later version.
 *
 * HotswapAgent is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with HotswapAgent. If not, see http://www.gnu.org/licenses/.
 */
package org.hotswap.agent.plugin.mybatis;

import org.hotswap.agent.annotation.*;
import org.hotswap.agent.command.Command;
import org.hotswap.agent.command.ReflectionCommand;
import org.hotswap.agent.command.Scheduler;
import org.hotswap.agent.config.PluginConfiguration;
import org.hotswap.agent.javassist.*;
import org.hotswap.agent.logging.AgentLogger;
import org.hotswap.agent.plugin.mybatis.transformers.MyBatisTransformers;

import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Reload MyBatis configuration after entity create/change.
 *
 * @author Vladimir Dvorak
 */
@Plugin(name = "MyBatis",
        description = "Reload MyBatis configuration after configuration create/change.",
        testedVersions = {"All between 5.3.2"},
        expectedVersions = {"5.3.2" },
        supportClass = { MyBatisTransformers.class })
public class MyBatisPlugin {
    private static AgentLogger LOGGER = AgentLogger.getLogger(MyBatisPlugin.class);

    @Init
    Scheduler scheduler;

    @Init
    ClassLoader appClassLoader;

    Map<String, Object> configurationMap = new ConcurrentHashMap<>();

    Command reloadConfigurationCommand =
            new ReflectionCommand(this, MyBatisRefreshCommands.class.getName(), "reloadConfiguration");

    Command reloadSingleMapperCommand =
            new ReflectionCommand(this, MyBatisRefreshCommands.class.getName(), "reloadSingleMapper");

    @Init
    public void init(PluginConfiguration pluginConfiguration) {
        LOGGER.info("MyBatis plugin initialized.");
    }

    public void registerConfigurationFile(String configFile, Object configObject) {
        if (configFile != null && !configurationMap.containsKey(configFile)) {
            LOGGER.debug("MyBatisPlugin - configuration file registered : {}", configFile);
            configurationMap.put(configFile, configObject);
        }
    }

    public void registerMapperBuilder(String resourcePath, Object mapperBuilder) {
        if (resourcePath != null && mapperBuilder instanceof org.apache.ibatis.builder.xml.XMLMapperBuilder) {
            LOGGER.debug("MyBatisPlugin - mapper builder registered : {}", resourcePath);
            org.hotswap.agent.plugin.mybatis.proxy.ConfigurationProxy.registerMapperBuilder(
                    resourcePath, (org.apache.ibatis.builder.xml.XMLMapperBuilder) mapperBuilder);
        }
    }

    public void registerAnnotationMapperConfig(String basePackage, Object scannerConfigurer) {
        if (basePackage != null && !configurationMap.containsKey(basePackage)) {
            LOGGER.debug("MyBatisPlugin - annotation mapper config registered : {}", basePackage);
            configurationMap.put(basePackage, scannerConfigurer);
        }
    }

    @OnResourceFileEvent(path="/", filter = ".*.xml", events = {FileEvent.MODIFY})
    public void registerResourceListeners(URL url) {
        String path = url.getPath();
        if (configurationMap.containsKey(path)) {
            if (path.endsWith("mybatis-config.xml") || path.endsWith("mybatis-configuration.xml")) {
                refresh(500);
            } else {
                Command singleMapperCmd = new ReflectionCommand(this, MyBatisRefreshCommands.class.getName(), "reloadSingleMapper", new Class[]{String.class}, new Object[]{path});
                scheduler.scheduleCommand(singleMapperCmd, 500);
            }
        }
    }

    @OnClassLoadEvent(
            classNameRegexp = "org.apache.ibatis.session.Configuration\\$StrictMap"
    )
    public static void patchStrictMap(CtClass ctClass, ClassPool classPool) throws NotFoundException, CannotCompileException {
        CtMethod method = ctClass.getDeclaredMethod("put", new CtClass[]{classPool.get(String.class.getName()), classPool.get(Object.class.getName())});
        method.insertBefore("if(containsKey($1)){remove($1);}");
    }

    @OnClassLoadEvent(
            classNameRegexp = "com.baomidou.mybatisplus.core.MybatisConfiguration\\$StrictMap",
            events = {LoadEvent.DEFINE}
    )
    public static void patchPlusStrictMap(CtClass ctClass, ClassPool classPool) throws NotFoundException, CannotCompileException {
        CtMethod method = ctClass.getDeclaredMethod("put", new CtClass[]{classPool.get(String.class.getName()), classPool.get(Object.class.getName())});
        method.insertBefore("if(containsKey($1)){remove($1);}");
    }


    @OnClassLoadEvent(
            classNameRegexp = "org.apache.ibatis.session.Configuration"
    )
    public static void transformConfiguration(CtClass ctClass, ClassPool classPool) throws NotFoundException, CannotCompileException {
        try {
            CtMethod addMappedStatementMethod = ctClass.getDeclaredMethod("addMappedStatement", new CtClass[]{classPool.get("org.apache.ibatis.mapping.MappedStatement")});
            addMappedStatementMethod.setBody("{if(mappedStatements.containsKey($1.getId())){mappedStatements.remove($1.getId());}mappedStatements.put($1.getId(),$1);}");
            CtMethod addParameterMapMethod = ctClass.getDeclaredMethod("addParameterMap", new CtClass[]{classPool.get("org.apache.ibatis.mapping.ParameterMap")});
            addParameterMapMethod.setBody("{if(parameterMaps.containsKey($1.getId())){parameterMaps.remove($1.getId());}parameterMaps.put($1.getId(),$1);}");
            CtMethod addResultMapMethod = ctClass.getDeclaredMethod("addResultMap", new CtClass[]{classPool.get("org.apache.ibatis.mapping.ResultMap")});
            addResultMapMethod.setBody("{if(resultMaps.containsKey($1.getId())){resultMaps.remove($1.getId());}resultMaps.put($1.getId(),$1);checkLocallyForDiscriminatedNestedResultMaps($1);checkGloballyForDiscriminatedNestedResultMaps($1);}");
            CtMethod addKeyGeneratorMethod = ctClass.getDeclaredMethod("addKeyGenerator", new CtClass[]{classPool.get("java.lang.String"), classPool.get("org.apache.ibatis.executor.keygen.KeyGenerator")});
            addKeyGeneratorMethod.setBody("{if(keyGenerators.containsKey($1)){keyGenerators.remove($1);}keyGenerators.put($1,$2);}");
            CtMethod addCacheMethod = ctClass.getDeclaredMethod("addCache", new CtClass[]{classPool.get("org.apache.ibatis.cache.Cache")});
            addCacheMethod.setBody("{if(caches.containsKey($1.getId())){caches.remove($1.getId());}caches.put($1.getId(),$1);}");
        } catch (Throwable var9) {
            LOGGER.warning("mybatis class enhance error:" + var9.getMessage(), new Object[0]);
        }

    }

    @OnClassLoadEvent(
            classNameRegexp = "com.baomidou.mybatisplus.core.MybatisConfiguration",
            events = {LoadEvent.DEFINE}
    )
    public static void transformPlusConfiguration(CtClass ctClass, ClassPool classPool) throws NotFoundException, CannotCompileException {
        CtMethod removeMappedStatementMethod = CtNewMethod.make("public void $$removeMappedStatement(String statementName){if(mappedStatements.containsKey(statementName)){mappedStatements.remove(statementName);}}", ctClass);
        ctClass.addMethod(removeMappedStatementMethod);
        ctClass.getDeclaredMethod("addMappedStatement", new CtClass[]{classPool.get("org.apache.ibatis.mapping.MappedStatement")}).insertBefore("$$removeMappedStatement($1.getId());");

        try {
            CtMethod addResultMapMethod = ctClass.getDeclaredMethod("addResultMap", new CtClass[]{classPool.get("org.apache.ibatis.mapping.ResultMap")});
            addResultMapMethod.insertBefore("if(resultMaps.containsKey($1.getId())){resultMaps.remove($1.getId());}");
        } catch (NotFoundException e) {
            LOGGER.debug("MybatisConfiguration.addResultMap not found, skipping.");
        }
        try {
            CtMethod addParameterMapMethod = ctClass.getDeclaredMethod("addParameterMap", new CtClass[]{classPool.get("org.apache.ibatis.mapping.ParameterMap")});
            addParameterMapMethod.insertBefore("if(parameterMaps.containsKey($1.getId())){parameterMaps.remove($1.getId());}");
        } catch (NotFoundException e) {
            LOGGER.debug("MybatisConfiguration.addParameterMap not found, skipping.");
        }
        try {
            CtMethod addCacheMethod = ctClass.getDeclaredMethod("addCache", new CtClass[]{classPool.get("org.apache.ibatis.cache.Cache")});
            addCacheMethod.insertBefore("if(caches.containsKey($1.getId())){caches.remove($1.getId());}");
        } catch (NotFoundException e) {
            LOGGER.debug("MybatisConfiguration.addCache not found, skipping.");
        }
    }


    // reload the configuration - schedule a command to run in the application classloader and merge
    // duplicate commands.
    private void refresh(int timeout) {
        scheduler.scheduleCommand(reloadConfigurationCommand, timeout);
    }

}
