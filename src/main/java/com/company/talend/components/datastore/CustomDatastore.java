package com.company.talend.components.datastore;

import java.io.Serializable;

import com.company.talend.components.service.CompanyComponentService;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.action.Checkable;
import org.talend.sdk.component.api.configuration.constraint.Required;
import org.talend.sdk.component.api.configuration.type.DataStore;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@DataStore("CustomDatastore")
@GridLayout({
    // the generated layout put one configuration entry per line,
    // customize it as much as needed
    @GridLayout.Row({ "filePath" })
})
@Checkable(CompanyComponentService.ACTION_BASIC_HEALTH_CHECK)
@Documentation("TODO fill the documentation for this configuration")
public class CustomDatastore implements Serializable {
    @Option
    @Required
    @Documentation("TODO fill the documentation for this parameter")
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public CustomDatastore setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
}