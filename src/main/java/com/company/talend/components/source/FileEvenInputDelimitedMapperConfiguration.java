package com.company.talend.components.source;

import java.io.Serializable;
import java.util.List;

import com.company.talend.components.dataset.CustomDataset;

import com.company.talend.components.entities.SchemaInfo;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.configuration.ui.layout.GridLayout;
import org.talend.sdk.component.api.configuration.ui.widget.Structure;
import org.talend.sdk.component.api.meta.Documentation;

@GridLayout({
    // the generated layout put one configuration entry per line,
    // customize it as much as needed
    @GridLayout.Row({ "schema" }),
    @GridLayout.Row({ "header" }),
    @GridLayout.Row({ "dataset" })
})
@Documentation("TODO fill the documentation for this configuration")
public class FileEvenInputDelimitedMapperConfiguration implements Serializable {
    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private CustomDataset dataset;

    @Option
    @Documentation("TODO fill the documentation for this parameter")
    private int header;

    @Option
    @Documentation("Incoming metadata.")
    @Structure(type = Structure.Type.OUT)
    private List<SchemaInfo> schema;

    public CustomDataset getDataset() {
        return dataset;
    }

    public FileEvenInputDelimitedMapperConfiguration setDataset(CustomDataset dataset) {
        this.dataset = dataset;
        return this;
    }

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public List<SchemaInfo> getSchema() {
        return schema;
    }

    public void setSchema(List<SchemaInfo> schema) {
        this.schema = schema;
    }
}