package com.epam.training.webshop.ui.config;

import org.jline.utils.AttributedString;
import org.springframework.context.annotation.Configuration;
import org.springframework.shell.jline.PromptProvider;

@Configuration
public class PromptConfig implements PromptProvider {

  @Override
  public AttributedString getPrompt() {
    return new AttributedString("WebShop>");
  }
}