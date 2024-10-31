package com.epam.training.webshop;

import com.epam.training.webshop.ui.interpreter.CommandLineInterpreter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext("com.epam.training.webshop");
    CommandLineInterpreter interpreter =
        context.getBean(CommandLineInterpreter.class);
    interpreter.handleUSerInput();
  }
}
