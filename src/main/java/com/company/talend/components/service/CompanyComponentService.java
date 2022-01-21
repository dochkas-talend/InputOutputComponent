package com.company.talend.components.service;

import com.company.talend.components.datastore.CustomDatastore;
import org.talend.sdk.component.api.configuration.Option;
import org.talend.sdk.component.api.service.Service;
import org.talend.sdk.component.api.service.healthcheck.HealthCheck;
import org.talend.sdk.component.api.service.healthcheck.HealthCheckStatus;

import java.io.File;

@Service
public class CompanyComponentService {

    public static final String ACTION_BASIC_HEALTH_CHECK = "ACTION_BASIC_HEALTH_CHECK";

    @HealthCheck(ACTION_BASIC_HEALTH_CHECK)
    public HealthCheckStatus validateFilePath(@Option CustomDatastore datastore) {
        try {
            File file = new File(datastore.getFilePath());

            if (!file.isFile()) {
                return new HealthCheckStatus(HealthCheckStatus.Status.KO, "Not a file specified!");
            }

            if (file.getParentFile().exists()) {
                return new HealthCheckStatus(HealthCheckStatus.Status.OK, "Connection successful!");
            }
            else {
                return new HealthCheckStatus(HealthCheckStatus.Status.KO, "Directory doesn't exist");
            }
        } catch (Exception e) {
            return new HealthCheckStatus(HealthCheckStatus.Status.KO, e.getMessage());
        }
    }
}