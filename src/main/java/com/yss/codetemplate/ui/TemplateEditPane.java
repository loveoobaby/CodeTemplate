package com.yss.codetemplate.ui;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.uiDesigner.core.GridConstraints;
import com.yss.codetemplate.CodeTemplate;
import com.yss.codetemplate.CodeTemplateSettings;

import javax.swing.*;
import java.awt.*;

/**
 * @author lixingjun
 * @date 2019/3/7
 * @description: TODO
 */
public class TemplateEditPane {

    private JPanel templateEdit;
    private JTextField templateNameText;
    private JButton deleteTemplateButton;
    private JPanel editorPane;
    private Editor editor;

    public TemplateEditPane(CodeTemplateSettings settings, String template,
                            CodeTemplateConfiguration parentPane) {
        CodeTemplate codeTemplate = settings.getCodeTemplate(template);
        if (codeTemplate == null) {
            codeTemplate = CodeTemplate.EMPTY_TEMPLATE;
        }

        templateNameText.setText(codeTemplate.getName());

        addVmEditor(codeTemplate.getTemplate());
        deleteTemplateButton.addActionListener(e -> {
            int result = Messages.showYesNoDialog("Delete this template?", "Delete", null);
            if (result == Messages.OK) {
                settings.removeCodeTemplate(template);
                parentPane.refresh(settings);
            }
        });
    }

    private void addVmEditor(String template) {
        EditorFactory factory = EditorFactory.getInstance();
        Document velocityTemplate = factory.createDocument(template);
        editor = factory.createEditor(velocityTemplate, null, FileTypeManager.getInstance()
                .getFileTypeByExtension("vm"), false);
        GridConstraints constraints = new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST,
                GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(300, 300), null, 0, true);
        editorPane.add(editor.getComponent(), constraints);
    }

    public JPanel getTemplateEdit() {
        return templateEdit;
    }

    public String getTemplateName() {
        return templateNameText.getText();
    }

    public String getTemplate() {
        return editor.getDocument().getText();
    }


}
