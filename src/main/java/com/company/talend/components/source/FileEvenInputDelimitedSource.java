package com.company.talend.components.source;

import java.io.*;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.company.talend.components.entities.SchemaInfo;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.input.Producer;
import org.talend.sdk.component.api.meta.Documentation;
import org.talend.sdk.component.api.record.Record;
import org.talend.sdk.component.api.service.record.RecordBuilderFactory;


import com.company.talend.components.service.CompanyComponentService;

@Documentation("TODO fill the documentation for this source")
public class FileEvenInputDelimitedSource implements Serializable {
    private final FileEvenInputDelimitedMapperConfiguration configuration;
    private final CompanyComponentService service;
    private final RecordBuilderFactory builderFactory;

    private BufferedReader reader;

    public FileEvenInputDelimitedSource(@Option("configuration") final FileEvenInputDelimitedMapperConfiguration configuration,
                        final CompanyComponentService service,
                        final RecordBuilderFactory builderFactory) {
        this.configuration = configuration;
        this.service = service;
        this.builderFactory = builderFactory;
    }

    @PostConstruct
    public void init() throws IOException {
        try {
            reader = new BufferedReader(new FileReader(configuration
                                                        .getDataset()
                                                        .getDatastore()
                                                        .getFilePath()));

            int header = configuration.getHeader();

            for (int i = 0; i < header; i++) {
                reader.readLine();
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File path is incorrect!");
        }
    }

    @Producer
    public Record next() throws IOException {
        reader.readLine();
        String evenLine = reader.readLine();

        if (evenLine == null) {
            return null;
        }

        String[] input = evenLine.split(configuration.getDataset().getDelimiter());

        List<SchemaInfo> schema = configuration.getSchema();

        Record.Builder newRecord = builderFactory.newRecordBuilder();

        for (int i = 0; i < schema.size(); i++) {
            SchemaInfo schemaInfo = schema.get(i);

            if (i >= input.length) {
                newRecord.withString(schemaInfo.getLabel(), null);
                continue;
            }

            switch (schemaInfo.getTalendType()) {
                case "id_Integer":
                    newRecord
                            .withInt(schemaInfo.getLabel(), Integer.parseInt(input[i]));
                    break;

                default:
                    newRecord
                            .withString(schemaInfo.getLabel(), input[i]);
            }
        }

        return newRecord.build();
    }

    @PreDestroy
    public void release() throws IOException {
        reader.close();
    }
}