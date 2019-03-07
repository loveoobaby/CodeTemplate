package com.yss.codetemplate;

import com.intellij.openapi.util.text.StringUtil;

/**
 * @author hansong.xhs
 * @version $Id: CodeTemplate.java, v 0.1 2017-01-28 9:41 hansong.xhs Exp $$
 */
public class CodeTemplate {

    public static final String DEFAULT_ENCODING = "UTF-8";

    public CodeTemplate() {}

    public CodeTemplate(String name, String template){
        this.name = name;
        this.template = template;
    }

    /**
     * template name
     */
    private String name;


    /**
     * code template in velocity
     */
    private String template;



    public boolean isValid() {
        return StringUtil.isNotEmpty(this.name) && StringUtil.isNotEmpty(this.template);
    }

    public static final CodeTemplate EMPTY_TEMPLATE = new CodeTemplate("",  "");

    public String getName() {
        return name;
    }

    public String getTemplate() {
        return template;
    }
}
