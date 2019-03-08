package com.yss.codetemplate.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.yss.codetemplate.CodeTemplateSettings;

/**
 * @author lixingjun
 * @date 2019/3/7
 * @description: 生成代码Action
 */
public class CodeTemplateAction extends AnAction implements DumbAware {

    private static final Logger log = Logger.getInstance(CodeTemplateAction.class);

    private CodeTemplateSettings settings;

    private String templateKey;

    CodeTemplateAction(String templateKey) {
        this.settings = ServiceManager.getService(CodeTemplateSettings.class);
        this.templateKey = templateKey;
        getTemplatePresentation().setDescription("description");
        getTemplatePresentation().setText(templateKey, false);
    }


    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = DataKeys.EDITOR.getData(e.getDataContext());
        final int offset = editor.getCaretModel().getOffset();
        final Document document = editor.getDocument();
        int lineNum = document.getLineNumber(offset);
        int startOffset = document.getLineStartOffset(lineNum + 1);

        new WriteCommandAction(project) {
            @Override
            protected void run(Result result) throws Throwable {
                document.insertString(startOffset, settings.getCodeTemplate(templateKey).getTemplate() + "\n");
            }
        }.execute();
    }

}
