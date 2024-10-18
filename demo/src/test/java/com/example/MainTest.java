package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MainTest {

    @Test
    public void testMain() {
        // Create a stream to hold the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        // Redirect System.out to the outputStream
        System.setOut(new PrintStream(outputStream));

        // Call the main method
        Main.main(null);

        // Verify the output
        assertEquals("Hello, world!\r\n", outputStream.toString());

        // Reset System.out to its original state
        System.setOut(originalOut);
    }
}
