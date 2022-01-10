package com.company.talend.components.output;

import java.io.Serializable;
import java.util.List;

import com.company.talend.components.dataset.CustomDataset;

import com.company.talend.components.entities.SchemaInfo;
import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.action.Checkable;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.configuration.ui.widget.Structure;
import org.talend.sdk.component.api.meta.Documentation;

@GridLayout({
    // the generated layout put one configuration entry per line,
    // customize it as much as needed
    @GridLayout.Row({ "schema" }),
    @GridLayout.Row({ "isHeaderIncluded" }),
    @GridLayout.Row({ "dataset" })
})
@Documentation("TODO fill the documentation for this configuration")
public class FileTwiceOutputDelimitedOutputConfiguration implements Serializable {
    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private CustomDataset dataset;

    @Option
    @Documentation("")
    private boolean isHeaderIncluded;

    @Option
    @Documentation("Incoming metadata.")
    @Structure(type = Structure.Type.IN)
    private List<SchemaInfo> schema;

    public CustomDataset getDataset() {
        return dataset;
    }

    public FileTwiceOutputDelimitedOutputConfiguration setDataset(CustomDataset dataset) {
        this.dataset = dataset;
        return this;
    }

    public List<SchemaInfo> getSchema() {
        return schema;
    }

    public void setSchema(List<SchemaInfo> schema) {
        this.schema = schema;
    }

    public boolean isHeaderIncluded() {
        return isHeaderIncluded;
    }

    public void setHeaderIncluded(boolean headerIncluded) {
        isHeaderIncluded = headerIncluded;
    }
}