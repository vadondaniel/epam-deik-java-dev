package com.epam.training.webshop.core.cart.grosspricecalculator.impl;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.cart.grosspricecalculator.GrossPriceCalculator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
abstract class GrossPriceCalculatorDecorator implements GrossPriceCalculator {

  private GrossPriceCalculator grossPriceCalculator;

  @Override
  public Money getAggregatedGrossPrice(Cart cart) {
    return grossPriceCalculator.getAggregatedGrossPrice(cart);
  }
}
