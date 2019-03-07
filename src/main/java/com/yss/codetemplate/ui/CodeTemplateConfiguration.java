package com.yss.codetemplate.ui;

import com.intellij.ui.components.JBTabbedPane;
import com.yss.codetemplate.CodeTemplate;
import com.yss.codetemplate.CodeTemplateSettings;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author hansong.xhs
 * @version $Id: CodeMakerConfiguration.java, v 0.1 2017-02-01 11:32 hansong.xhs Exp $$
 */
public class CodeTemplateConfiguration {
    private JPanel                        mainPane;
    private JButton                       addTemplateButton;
    private JBTabbedPane tabbedPane;
    private TreeMap<String, TemplateEditPane> editPaneMap;

    public CodeTemplateConfiguration(CodeTemplateSettings settings) {
        tabbedPane = new JBTabbedPane();
        editPaneMap = new TreeMap<>();
        addTemplateButton.addActionListener(e -> {
            TemplateEditPane editPane = new TemplateEditPane(settings, "", this);
            String title = "Untitled";
            tabbedPane.addTab(title, editPane.getTemplateEdit());
            tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            editPaneMap.put(title, editPane);
        });
        resetTabPane(settings);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 1;
        mainPane.add(tabbedPane, constraints);
    }

    public void refresh(CodeTemplateSettings settings) {
        tabbedPane.removeAll();
        editPaneMap.clear();
        resetTabPane(settings);
    }

    private void resetTabPane(CodeTemplateSettings settings) {
        settings.getCodeTemplates().forEach((key, value)->{
            System.out.println("key=" + key);
            System.out.println("value="+value.getName());
        });
        settings.getCodeTemplates().forEach((key, value) -> {
            TemplateEditPane editPane = new TemplateEditPane(settings, key, this);
            tabbedPane.addTab(key, editPane.getTemplateEdit());
            editPaneMap.put(key, editPane);
        });
    }

    public Map<String, CodeTemplate> getTabTemplates() {
        Map<String, CodeTemplate> map = new HashMap<>();
        editPaneMap.forEach((key, value) -> {
            CodeTemplate codeTemplate = new CodeTemplate(value.getTemplateName(), value.getTemplate());
            map.put(codeTemplate.getName(), codeTemplate);
        });
        return map;
    }

    /**
     * Getter method for property <tt>mainPane</tt>.
     *
     * @return property value of mainPane
     */
    public JPanel getMainPane() {
        return mainPane;
    }
}
