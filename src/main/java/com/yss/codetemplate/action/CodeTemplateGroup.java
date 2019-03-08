package com.yss.codetemplate.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.yss.codetemplate.CodeTemplateSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lixingjun
 * @date 2019/3/7
 * @description: 菜单组
 */
public class CodeTemplateGroup extends ActionGroup {

    private CodeTemplateSettings settings;

    public CodeTemplateGroup() {
        settings = ServiceManager.getService(CodeTemplateSettings.class);
    }


    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        if (anActionEvent == null) {
            return AnAction.EMPTY_ARRAY;
        }
        Project project = PlatformDataKeys.PROJECT.getData(anActionEvent.getDataContext());
        if (project == null) {
            return AnAction.EMPTY_ARRAY;
        }
        final List<AnAction> children = new ArrayList<>();
        settings.getCodeTemplates().forEach((key, value) -> children.add(getOrCreateAction(key)));

        return children.toArray(new AnAction[children.size()]);
    }

    private AnAction getOrCreateAction(String templateName) {
        final String actionId = "CodeMaker.Menu.Action." + templateName;
        AnAction action = ActionManager.getInstance().getAction(actionId);
        if (action == null) {
            action = new CodeTemplateAction(templateName);
            ActionManager.getInstance().registerAction(actionId, action);
        }
        return action;
    }
}
