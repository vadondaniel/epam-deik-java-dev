package com.epam.training.webshop.core.cart.grosspricecalculator.impl;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.cart.grosspricecalculator.GrossPriceCalculator;

public class GrossPriceCalculatorImpl implements GrossPriceCalculator {

  @Override
  public Money getAggregatedGrossPrice(Cart cart) {
    return cart.getAggregatedNetPrice();
  }
}
