package com.example;

import java.util.List;

import org.apache.sling.api.resource.ResourceResolver;

// Main method for testing purposes
public class Main {
  public static void main(String[] args) {
      AdvisorServiceImpl advisorService = new AdvisorServiceImpl();
      advisorService.csvService = new CSVServiceImpl();
      ResourceResolver resourceResolver = null; // Dummy resolver for the sake of this example

      List<Advisor> advisors = advisorService.getAdvisor(resourceResolver);
      for (Advisor advisor : advisors) {
          System.out.println(advisor);
      }
  }
}