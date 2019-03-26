package com.yss.codetemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author lixingjun
 * @date 2019/3/7
 * @description: 持久化配置组件
 */
@State(name = "CodeTemplateSettings", storages = {@Storage(file = "$APP_CONFIG$/CodeTemplate-settings.xml")})
public class CodeTemplateSettings implements PersistentStateComponent<CodeTemplateSettings> {


    private static final Logger LOGGER = Logger.getInstance(CodeTemplateSettings.class);

    public CodeTemplateSettings() {

    }

    private void loadDefaultSettings() {
        try {
            {
                Map<String, CodeTemplate> codeTemplates = new TreeMap<>();
                this.codeTemplates = codeTemplates;
            }

            {
                Map<String, CodeTemplate> nettyTemplates = new TreeMap<>();

                String velocityTemplate = FileUtil.loadTextAndClose(CodeTemplateSettings.class.getResourceAsStream("/template/netty.json"));
                JSONArray array = JSON.parseArray(velocityTemplate);
                for (int i = 0; i < array.size(); i++) {
                    JSONObject codeTempJson = array.getJSONObject(i);
                    String name = codeTempJson.getString("name");
                    nettyTemplates.put(name, new CodeTemplate(name, codeTempJson.getString("template")));
                }

                this.nettyTemplates = nettyTemplates;

            }

            {
                Map<String, CodeTemplate> jdkTemplates = new TreeMap<>();

                String velocityTemplate = FileUtil.loadTextAndClose(CodeTemplateSettings.class.getResourceAsStream("/template/jdk.json"));
                JSONArray array = JSON.parseArray(velocityTemplate);
                for (int i = 0; i < array.size(); i++) {
                    JSONObject codeTempJson = array.getJSONObject(i);
                    String name = codeTempJson.getString("name");
                    jdkTemplates.put(name, new CodeTemplate(name, codeTempJson.getString("template")));
                }

                this.jdkTemplates = jdkTemplates;

            }

            {
                Map<String, CodeTemplate> jdkTemplates = new TreeMap<>();

                String velocityTemplate = FileUtil.loadTextAndClose(CodeTemplateSettings.class.getResourceAsStream("/template/rabbit.json"));
                JSONArray array = JSON.parseArray(velocityTemplate);
                for (int i = 0; i < array.size(); i++) {
                    JSONObject codeTempJson = array.getJSONObject(i);
                    String name = codeTempJson.getString("name");
                    jdkTemplates.put(name, new CodeTemplate(name, codeTempJson.getString("template")));
                }

                this.rabbitTemplates = jdkTemplates;
            }

        } catch (Exception e) {
            LOGGER.error("loadDefaultSettings failed", e);
        }
    }


    public Map<String, CodeTemplate> getCodeTemplates() {
        if (codeTemplates == null) {
            loadDefaultSettings();
        }

        return codeTemplates;
    }

    public Map<String, CodeTemplate> getNettyTemplates(){
        if(nettyTemplates == null){
            loadDefaultSettings();
        }
        return nettyTemplates;
    }

    public Map<String, CodeTemplate> getRabbitTemplates(){
        if(rabbitTemplates == null){
            loadDefaultSettings();
        }
        return rabbitTemplates;
    }


    private Map<String, CodeTemplate> codeTemplates;
    private Map<String, CodeTemplate> nettyTemplates;
    private Map<String, CodeTemplate> jdkTemplates;
    private Map<String, CodeTemplate> rabbitTemplates;

    public void setCodeTemplates(Map<String, CodeTemplate> codeTemplates) {
        this.codeTemplates = codeTemplates;
    }

    public Map<String, CodeTemplate> getJdkTemplates() {
        if (this.jdkTemplates == null) {
            loadDefaultSettings();
        }
        return jdkTemplates;
    }

    @Nullable
    @Override
    public CodeTemplateSettings getState() {
        if (this.codeTemplates == null) {
            loadDefaultSettings();
        }
        return this;
    }

    @Override
    public void loadState(CodeTemplateSettings codeMakerSettings) {
        XmlSerializerUtil.copyBean(codeMakerSettings, this);
    }

    public CodeTemplate getCodeTemplate(String template) {
        return codeTemplates.get(template);
    }


    public void removeCodeTemplate(String template) {
        codeTemplates.remove(template);
    }

}
