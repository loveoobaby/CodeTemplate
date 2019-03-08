package com.yss.codetemplate;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author lixingjun
 * @date 2019/3/7
 * @description: 代码片段模板bean
 */
public class CodeTemplate implements Comparable<CodeTemplate> {

    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final CodeTemplate EMPTY_TEMPLATE = new CodeTemplate("", "");

    public CodeTemplate() {
    }

    public CodeTemplate(String name, String template) {
        this.name = name;
        this.template = template;
    }

    // 模板的名称
    private String name;

    // 模板的类型
    private String template;


    public boolean isValid() {
        return StringUtil.isNotEmpty(this.name) && StringUtil.isNotEmpty(this.template);
    }

    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeTemplate that = (CodeTemplate) o;


        return Objects.equals(name, that.name) &&
                Objects.equals(template, that.template);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, template);
    }


    @Override
    public int compareTo(@NotNull CodeTemplate o) {
        return o.getName().compareTo(this.name);
    }
}
