package com.company.talend.components.output;

import static org.talend.sdk.component.api.component.Icon.IconType.CUSTOM;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.company.talend.components.entities.SchemaInfo;
import org.talend.sdk.component.api.component.Icon;
import org.talend.sdk.component.api.component.Version;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.processor.AfterGroup;
import org.talend.sdk.component.api.processor.BeforeGroup;
import org.talend.sdk.component.api.processor.ElementListener;
import org.talend.sdk.component.api.processor.Input;
import org.talend.sdk.component.api.processor.Processor;
import org.talend.sdk.component.api.record.Record;

import com.company.talend.components.service.CompanyComponentService;

@Version(1)
@Icon(value = CUSTOM, custom = "FileTwiceOutputDelimited")
@Processor(name = "FileTwiceOutputDelimited")
@Documentation("TODO fill the documentation for this processor")
public class FileTwiceOutputDelimitedOutput implements Serializable {
    private final static int NUMBER_OF_DUPLICATED_ROWS = 2;

    private final FileTwiceOutputDelimitedOutputConfiguration configuration;
    private final CompanyComponentService service;

    private BufferedWriter writer;
    private boolean closed = false;
    private int groups = 0;
    private ArrayList<String> buffer;

    public FileTwiceOutputDelimitedOutput(@Option("configuration") final FileTwiceOutputDelimitedOutputConfiguration configuration,
                          final CompanyComponentService service) {
        this.configuration = configuration;
        this.service = service;
    }

    @PostConstruct
    public void init() throws IOException {
        try {
            writer = new BufferedWriter(new FileWriter(configuration
                                                        .getDataset()
                                                        .getDatastore()
                                                        .getFilePath()));

            if (configuration.isHeaderIncluded()) {
                StringBuilder headerLine = new StringBuilder();

                List<SchemaInfo> schema = configuration.getSchema();

                for (int i = 0; i < schema.size() - 1; i++) {
                    String label = schema.get(i).getLabel();

                    headerLine.append(label).append(configuration.getDataset().getDelimiter());
                }

                headerLine.append(schema.get(schema.size() - 1).getLabel());

                writer.write(headerLine.toString());
                writer.newLine();
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File path is incorrect!");
        }
    }

    @BeforeGroup
    public void refreshBuffer() {
        buffer = new ArrayList<>();
        groups++;
    }

    @ElementListener
    public void onNext(
            @Input final Record defaultInput) throws IOException {
        StringBuilder row = new StringBuilder();

        List<SchemaInfo> schema = configuration.getSchema();

        for (int i = 0; i < schema.size(); i++) {
            SchemaInfo schemaInfo = schema.get(i);

            switch (schemaInfo.getTalendType()) {
                case "id_Integer":
                    row.append(defaultInput.getInt(schemaInfo.getLabel()));
                    break;
                default:
                    row.append(defaultInput.getString(schemaInfo.getLabel()));
            }

            if (i != schema.size() - 1) {
                row.append(configuration.getDataset().getDelimiter());
            }
        }

        for (int i = 0; i < NUMBER_OF_DUPLICATED_ROWS; i++) {
            buffer.add(row.toString());
        }
    }

    @AfterGroup
    public void writeBulk() throws IOException {
        if (!closed) {
            for (String row : buffer) {
                writer.write(row);
                writer.newLine();
            }

            groups--;
        }
    }

    @PreDestroy
    public void release() throws IOException {
        if (groups != 0) {
            writeBulk();
        }

        writer.close();
        closed = true;
    }
}