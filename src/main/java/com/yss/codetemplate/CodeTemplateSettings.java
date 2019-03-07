package com.yss.codetemplate;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hansong.xhs
 * @version $Id: CodeMakerSettings.java, v 0.1 2017-01-28 9:30 hansong.xhs Exp $$
 */
@State(name = "CodeTemplateSettings", storages = {@Storage(file = "$APP_CONFIG$/CodeTemplate-settings.xml")})
public class CodeTemplateSettings implements PersistentStateComponent<CodeTemplateSettings> {

    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getInstance(CodeTemplateSettings.class);

    public CodeTemplateSettings() {

    }

    private void loadDefaultSettings() {
        try {
            Map<String, CodeTemplate> codeTemplates = new HashMap<>();
            codeTemplates.put("Model",
                    createCodeTemplate("Model.vm"));
            codeTemplates.put("Converter",
                    createCodeTemplate("Converter.vm"));
            codeTemplates.put("Specs2 Matcher",
                    createCodeTemplate("specs2-matcher.vm"));
            codeTemplates.put("FieldComment",
                    createCodeTemplate("FieldComment.vm"));

            this.codeTemplates = codeTemplates;
        } catch (Exception e) {
            LOGGER.error("loadDefaultSettings failed", e);
        }
    }

    @NotNull
    private CodeTemplate createCodeTemplate(String sourceTemplateName) throws IOException {
        String velocityTemplate = FileUtil.loadTextAndClose(CodeTemplateSettings.class.getResourceAsStream("/template/" + sourceTemplateName));
        return new CodeTemplate(sourceTemplateName, velocityTemplate);
    }

    /**
     * Getter method for property <tt>codeTemplates</tt>.
     *
     * @return property value of codeTemplates
     */
    public Map<String, CodeTemplate> getCodeTemplates() {
        if (codeTemplates == null) {
            loadDefaultSettings();
        }

        return codeTemplates;
    }


    private Map<String, CodeTemplate> codeTemplates;

    public void setCodeTemplates(Map<String, CodeTemplate> codeTemplates) {
        this.codeTemplates = codeTemplates;
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
