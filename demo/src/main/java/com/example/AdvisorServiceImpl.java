package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = AdvisorService.class)
public class AdvisorServiceImpl implements AdvisorService {
    private static final String CSV_FILE_PATH = "/content/dam/corient/advisors/advisors.csv";

    private static final Logger LOG = LoggerFactory.getLogger(AdvisorServiceImpl.class);

    @Reference
    public transient CSVService csvService;

    public List<Advisor> advisors;

    @Activate
    public void activate() {
        final ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
        s.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (advisors != null) {
                    advisors.clear();
                }
            }
        }, 1, 1, TimeUnit.HOURS);
    }

    @Override
    public List<Advisor> getAdvisor(ResourceResolver resourceResolver) {
        if (advisors == null || advisors.isEmpty()) {
            try {
                advisors = this.csvService.loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true);
            } catch (IOException e) {
                LOG.error("Exception loading Advisor csv data", e);
            }
        }
        return new ArrayList<>(advisors);
    }
}

// Dummy classes to complete the program

class Advisor {
    // Fields and methods for the Advisor class
    private String name;
    private String id;

    public Advisor(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public Advisor() {
        this.name = "name";
        this.id = "id";
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Advisor{name='" + name + "', id='" + id + "'}";
    }
}

interface CSVService {
    <T> List<T> loadData(Class<T> clazz, String filePath, ResourceResolver resourceResolver, boolean skipHeader)
            throws IOException;
}

class CSVServiceImpl implements CSVService {
    @Override
    public <T> List<T> loadData(Class<T> clazz, String filePath, ResourceResolver resourceResolver, boolean skipHeader)
            throws IOException {
        // Dummy implementation for testing purposes
        List<T> data = new ArrayList<>();
        data.add((T) new Advisor("John Doe", "123"));
        data.add((T) new Advisor("Jane Smith", "456"));
        return data;
    }
}

interface AdvisorService {
    List<Advisor> getAdvisor(ResourceResolver resourceResolver);
}
