package com.company.talend.components.dataset;

import java.io.Serializable;

import com.company.talend.components.datastore.CustomDatastore;

import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.constraint.Required;
import org.talend.sdk.component.api.configuration.type.DataSet;
import org.talend.sdk.component.api.configuration.ui.DefaultValue;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.meta.Documentation;

@DataSet("CustomDataset")
@GridLayout({
    // the generated layout put one configuration entry per line,
    // customize it as much as needed
    @GridLayout.Row({ "datastore" }),
    @GridLayout.Row({ "delimiter" })
})
@Documentation("TODO fill the documentation for this configuration")
public class CustomDataset implements Serializable {
    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private CustomDatastore datastore;

    @Option
    @Required
    @DefaultValue(",")
    @Documentation("TODO fill the documentation for this parameter")
    private String delimiter;

    public CustomDatastore getDatastore() {
        return datastore;
    }

    public CustomDataset setDatastore(CustomDatastore datastore) {
        this.datastore = datastore;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public CustomDataset setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }
}