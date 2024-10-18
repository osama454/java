package com.example;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CalculatorServiceTest {

    @Mock
    private ExternalService externalService;

    @InjectMocks
    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        // Initialize mocks using the older method
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNumbers() {
        // Define behavior of the mock for specific inputs
        when(externalService.add(10, 20)).thenReturn(30);

        // Call the method and assert the result
        int result = calculatorService.addNumbers(10, 20);

        // Verify that the mock method was called
        verify(externalService).add(10, 20);

        // Check if the result is as expected
        assertEquals(30, result);
    }
}
