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
        // this method will be executed once for the whole component execution,
        // this is where you can establish a connection for instance

        try {
            reader = new BufferedReader(new FileReader(configuration
                                                        .getDataset()
                                                        .getDatastore()
                                                        .getFilePath()));

            int header = configuration.getHeader();
            int rowCount = 0;

            while (rowCount < header) {
                reader.readLine();
                rowCount++;
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File path is incorrect!");
        }
    }

    @Producer
    public Record next() throws IOException {
        // this is the method allowing you to go through the dataset associated
        // to the component configuration
        //
        // return null means the dataset has no more data to go through
        // you can use the builderFactory to create a new Record.

        reader.readLine();
        String evenLine = reader.readLine();

        if (evenLine == null) {
            return null;
        }

        String[] input = evenLine.split(configuration.getDataset().getDelimiter());

        List<SchemaInfo> schemas = configuration.getSchema();

        if (input.length != schemas.size()) {
            throw new IllegalStateException("Schema doesn't match input!");
        }

        Record.Builder newRecord = builderFactory.newRecordBuilder();

        for (int i = 0; i < input.length; i++) {
            SchemaInfo schemaInfo = schemas.get(i);

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
        // this is the symmetric method of the init() one,
        // release potential connections you created or data you cached

        reader.close();
    }
}