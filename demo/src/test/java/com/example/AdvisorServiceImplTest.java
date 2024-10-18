package com.example;



import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdvisorServiceImplTest {

    private static final String CSV_FILE_PATH = "/content/dam/corient/advisors/advisors.csv";
    private static final Logger LOG = LoggerFactory.getLogger(AdvisorServiceImplTest.class);

    @Mock
    private CSVService csvService;

    @Mock
    private ResourceResolver resourceResolver;

    @InjectMocks
    private AdvisorServiceImpl advisorService;

    @Test
    public void testGetAdvisor_whenAdvisorsIsNull() throws IOException {
        // Mock the behavior of csvService.loadData() to return a list of advisors
        List<Advisor> mockAdvisors = new ArrayList<>();
        mockAdvisors.add(new Advisor()); // Add some sample advisor data
        when(csvService.loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true)).thenReturn(mockAdvisors);

        // Call the getAdvisor() method
        List<Advisor> advisors = advisorService.getAdvisor(resourceResolver);

        // Verify the results
        assertEquals(mockAdvisors.size(), advisors.size()); // Check if the returned list has the expected size
        verify(csvService, times(1)).loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true); // Verify that csvService.loadData() was called once
    }

    @Test
    public void testGetAdvisor_whenAdvisorsIsEmpty() throws IOException {
        // Initialize the advisors list to an empty list
        advisorService.advisors = new ArrayList<>();

        // Mock the behavior of csvService.loadData() to return a list of advisors
        List<Advisor> mockAdvisors = new ArrayList<>();
        mockAdvisors.add(new Advisor()); // Add some sample advisor data
        when(csvService.loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true)).thenReturn(mockAdvisors);

        // Call the getAdvisor() method
        List<Advisor> advisors = advisorService.getAdvisor(resourceResolver);

        // Verify the results
        assertEquals(mockAdvisors.size(), advisors.size()); // Check if the returned list has the expected size
        verify(csvService, times(1)).loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true); // Verify that csvService.loadData() was called once
    }

    @Test
    public void testGetAdvisor_whenAdvisorsIsNotNullOrEmpty() throws IOException {
        // Initialize the advisors list with some data
        List<Advisor> initialAdvisors = new ArrayList<>();
        initialAdvisors.add(new Advisor());
        advisorService.advisors = initialAdvisors;

        // Call the getAdvisor() method
        List<Advisor> advisors = advisorService.getAdvisor(resourceResolver);

        // Verify the results
        assertEquals(initialAdvisors.size(), advisors.size()); // Check if the returned list has the expected size
        verify(csvService, never()).loadData(any(), any(), any(), anyBoolean()); // Verify that csvService.loadData() was not called
    }

    @Test
    public void testGetAdvisor_whenIOExceptionOccurs() throws IOException {
        // Mock the behavior of csvService.loadData() to throw an IOException
        when(csvService.loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true)).thenThrow(new IOException("Simulated IO Exception"));

        // Call the getAdvisor() method
        List<Advisor> advisors = advisorService.getAdvisor(resourceResolver);

        // Verify the results
        assertEquals(0, advisors.size()); // Check if the returned list is empty
        verify(csvService, times(1)).loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true); // Verify that csvService.loadData() was called once
    }

    // You might need to add more test cases to cover different scenarios and edge cases
    // For example, you could test the activate() method and its scheduling behavior
}