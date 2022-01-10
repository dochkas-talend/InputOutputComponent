package com.company.talend.components.entities;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

import java.io.Serializable;

@GridLayout(value = {@GridLayout.Row({"label", "key", "talendType", "nullable", "pattern"})})
@Documentation("Schema definition.")
public class SchemaInfo implements Serializable {

    @Option
    @Documentation("Column name.")
    private String label;

    @Option
    @Documentation("Is it a Key column.")
    private boolean key;

    @Option
    @Documentation("Talend type such as id_String.")
    private String talendType;

    @Option
    @Documentation("Is it a Nullable column.")
    private boolean nullable;

    @Option
    @Documentation("Pattern used for datetime processing.")
    private String pattern = "yyyy-MM-dd HH:mm";

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public String getTalendType() {
        return talendType;
    }

    public void setTalendType(String talendType) {
        this.talendType = talendType;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}