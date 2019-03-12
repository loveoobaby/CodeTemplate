package com.yss.codetemplate.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.yss.codetemplate.CodeTemplate;

/**
 * @author lixingjun
 * @date 2019/3/7
 * @description: 生成代码Action
 */
public class CodeTemplateAction extends AnAction implements DumbAware {


    private CodeTemplate codeTemplate;

    CodeTemplateAction(CodeTemplate codeTemplate) {
        this.codeTemplate = codeTemplate;
        getTemplatePresentation().setDescription("description");
        getTemplatePresentation().setText(codeTemplate.getName(), false);
    }


    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        final Editor editor = DataKeys.EDITOR.getData(e.getDataContext());

        if(editor == null){
            Messages.showInfoMessage("请打开编辑器, 光标放在插入位置", "Error");
            return;
        }

        final int offset = editor.getCaretModel().getOffset();
        final Document document = editor.getDocument();
        int lineNum = document.getLineNumber(offset);
        int startOffset = document.getLineStartOffset(lineNum);

        new WriteCommandAction(project) {
            @Override
            protected void run(Result result) throws Throwable {
                document.insertString(startOffset, codeTemplate.getTemplate() + "\n");
            }
        }.execute();
    }

}
