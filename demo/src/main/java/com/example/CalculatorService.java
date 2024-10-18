package com.example;

public class CalculatorService {

  private ExternalService externalService;

  public CalculatorService(ExternalService externalService) {
      this.externalService = externalService;
  }

  public int addNumbers(int a, int b) {
      return externalService.add(a, b);
  }
}

interface ExternalService {
  int add(int a, int b);
}
