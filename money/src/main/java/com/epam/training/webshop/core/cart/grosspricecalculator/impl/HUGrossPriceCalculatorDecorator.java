package com.epam.training.webshop.core.cart.grosspricecalculator.impl;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.cart.grosspricecalculator.GrossPriceCalculator;

public class HUGrossPriceCalculatorDecorator extends GrossPriceCalculatorDecorator {

  public HUGrossPriceCalculatorDecorator(GrossPriceCalculator grossPriceCalculator) {
    super(grossPriceCalculator);
  }

  @Override
  public Money getAggregatedGrossPrice(Cart cart) {
    return super.getAggregatedGrossPrice(cart).multiply(1.27);
  }
}
