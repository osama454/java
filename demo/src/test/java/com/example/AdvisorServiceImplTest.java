package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.apache.sling.api.resource.ResourceResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdvisorServiceImplTest {

    private static final String CSV_FILE_PATH = "/content/dam/corient/advisors/advisors.csv";

    @Mock
    private CSVSerivce csvService;

    @Mock
    private Logger LOG;

    @Mock
    private ResourceResolver resourceResolver;

    @InjectMocks
    private AdvisorServiceImpl advisorService;

    @Test
    void testGetAdvisor_whenAdvisorsIsNull() throws IOException {
        // Mock the behavior of csvService.loadData() to return a list of advisors
        List<Advisor> mockAdvisors = new ArrayList<>();
        mockAdvisors.add(new Advisor()); // Add a sample advisor
        when(csvService.loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true))
                .thenReturn(mockAdvisors);

        // Call the getAdvisor() method
        List<Advisor> advisors = advisorService.getAdvisor(resourceResolver);

        // Verify the results
        assertNotNull(advisors);
        assertEquals(1, advisors.size()); // Check if the returned list has the expected size
        verify(csvService, times(1)).loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true);
    }

    @Test
    void testGetAdvisor_whenAdvisorsIsEmpty() throws IOException {
        // Initialize the advisors list to be empty
        advisorService.advisors = new ArrayList<>();

        // Mock the behavior of csvService.loadData() to return a list of advisors
        List<Advisor> mockAdvisors = new ArrayList<>();
        mockAdvisors.add(new Advisor()); // Add a sample advisor
        when(csvService.loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true))
                .thenReturn(mockAdvisors);

        // Call the getAdvisor() method
        List<Advisor> advisors = advisorService.getAdvisor(resourceResolver);

        // Verify the results
        assertNotNull(advisors);
        assertEquals(1, advisors.size()); // Check if the returned list has the expected size
        verify(csvService, times(1)).loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true);
    }

    @Test
    void testGetAdvisor_whenAdvisorsIsNotNullOrEmpty() throws IOException {
        // Initialize the advisors list with some data
        advisorService.advisors = new ArrayList<>();
        advisorService.advisors.add(new Advisor());

        // Call the getAdvisor() method
        List<Advisor> advisors = advisorService.getAdvisor(resourceResolver);

        // Verify the results
        assertNotNull(advisors);
        assertEquals(1, advisors.size()); // Check if the returned list has the expected size
        verify(csvService, never()).loadData(any(), any(), any(), anyBoolean()); // Verify loadData is not called
    }

    @Test
    void testGetAdvisor_whenIOExceptionOccurs() throws IOException {
        // Mock the behavior of csvService.loadData() to throw an IOException
        when(csvService.loadData(Advisor.class, CSV_FILE_PATH, resourceResolver, true))
                .thenThrow(new IOException("Simulated IO Exception"));

        // Call the getAdvisor() method
        List<Advisor> advisors = advisorService.getAdvisor(resourceResolver);

        // Verify the results
        assertNotNull(advisors);
        assertEquals(0, advisors.size()); // The list should be empty in case of an exception
        verify(LOG, times(1)).error(eq("Exception loading Advisor csv dara"), any(IOException.class));
    }
}