package com.company.talend.components.output;

import static org.talend.sdk.component.api.component.Icon.IconType.CUSTOM;

import java.io.*;
import java.util.stream.Collectors;

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

@Version(1) // default version is 1, if some configuration changes happen between 2 versions you can add a migrationHandler
@Icon(value = CUSTOM, custom = "FileTwiceOutputDelimited") // icon is located at src/main/resources/icons/FileTwiceOutputDelimited.svg
@Processor(name = "FileTwiceOutputDelimited")
@Documentation("TODO fill the documentation for this processor")
public class FileTwiceOutputDelimitedOutput implements Serializable {
    private final FileTwiceOutputDelimitedOutputConfiguration configuration;
    private final CompanyComponentService service;

    private BufferedWriter writer;

    public FileTwiceOutputDelimitedOutput(@Option("configuration") final FileTwiceOutputDelimitedOutputConfiguration configuration,
                          final CompanyComponentService service) {
        this.configuration = configuration;
        this.service = service;
    }

    @PostConstruct
    public void init() throws IOException {
        // this method will be executed once for the whole component execution,
        // this is where you can establish a connection for instance
        // Note: if you don't need it you can delete it

        try {
            writer = new BufferedWriter(new FileWriter(configuration
                                                        .getDataset()
                                                        .getDatastore()
                                                        .getFilePath()));

            if (configuration.isHeaderIncluded()) {
                StringBuilder headerLine = new StringBuilder();

                for (String header : configuration
                        .getSchema()
                        .stream()
                        .map(SchemaInfo::getLabel)
                        .collect(Collectors.toList())) {
                    headerLine.append(header).append(configuration.getDataset().getDelimiter());
                }

                headerLine.delete(headerLine.length() - 1, headerLine.length());
                headerLine.append('\n');

                writer.write(headerLine.toString());
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File path is incorrect!");
        }
    }

    @BeforeGroup
    public void beforeGroup() {
        // if the environment supports chunking this method is called at the beginning if a chunk
        // it can be used to start a local transaction specific to the backend you use
        // Note: if you don't need it you can delete it
    }

    @ElementListener
    public void onNext(
            @Input final Record defaultInput) throws IOException {
        // this is the method allowing you to handle the input(s) and emit the output(s)
        // after some custom logic you put here, to send a value to next element you can use an
        // output parameter and call emit(value).

        StringBuilder row = new StringBuilder();

        for (SchemaInfo schemaInfo : configuration.getSchema()) {
            switch (schemaInfo.getTalendType()) {
                case "id_Integer":
                    row.append(defaultInput.getInt(schemaInfo.getLabel()));
                    break;
                default:
                    row.append(defaultInput.getString(schemaInfo.getLabel()));
            }

            row.append(configuration.getDataset().getDelimiter());
        }

        row.delete(row.length() - 1, row.length());
        row.append('\n');

        writer.write(row.toString());
        writer.write(row.toString());
    }

    @AfterGroup
    public void afterGroup() {
        // symmetric method of the beforeGroup() executed after the chunk processing
        // Note: if you don't need it you can delete it
    }

    @PreDestroy
    public void release() throws IOException {
        // this is the symmetric method of the init() one,
        // release potential connections you created or data you cached
        // Note: if you don't need it you can delete it
        writer.close();
    }
}