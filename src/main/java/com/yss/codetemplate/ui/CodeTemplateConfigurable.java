package com.yss.codetemplate.ui;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.yss.codetemplate.CodeTemplate;
import com.yss.codetemplate.CodeTemplateSettings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

/**
 * @author lixingjun
 * @date 2019/3/7
 * @description:
 */
public class CodeTemplateConfigurable implements SearchableConfigurable {

    private CodeTemplateSettings settings;

    private CodeTemplateConfiguration configuration;

    public CodeTemplateConfigurable() {
        settings = ServiceManager.getService(CodeTemplateSettings.class);
    }

    @NotNull
    @Override
    public String getId() {
        return "plugins.codeTemplate";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String option) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "CodeTemplate";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "help.codetemplate.configuration";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (configuration == null) {
            configuration = new CodeTemplateConfiguration(settings);
        }
        return configuration.getMainPane();
    }

    /**
     * Compare the data to see if we are modified
     *
     * @return true if the settings should be 'applied'
     */
    @Override
    public boolean isModified() {
        if (settings.getCodeTemplates().size() != configuration.getTabTemplates().size()) {
            return true;
        }
        for (Map.Entry<String, CodeTemplate> entry : configuration.getTabTemplates().entrySet()) {
            CodeTemplate codeTemplate = settings.getCodeTemplate(entry.getKey());
            if (codeTemplate == null || !codeTemplate.equals(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        for (Map.Entry<String, CodeTemplate> entry : configuration.getTabTemplates().entrySet()) {
            if (!entry.getValue().isValid()) {
                throw new ConfigurationException(
                        "Not property can be empty and classNumber should be a number");
            }
        }
        settings.setCodeTemplates(configuration.getTabTemplates());
        configuration.refresh(settings);
    }

    @Override
    public void reset() {
        if (configuration != null) {
            configuration.refresh(settings);
        }
    }

    @Override
    public void disposeUIResources() {
        this.configuration = null;
    }
}
