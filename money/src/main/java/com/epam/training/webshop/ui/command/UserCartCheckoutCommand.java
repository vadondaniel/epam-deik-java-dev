package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grosspricecalculator.GrossPriceCalculator;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.ui.session.Session;

public class UserCartCheckoutCommand extends AbstractCommand{

  private GrossPriceCalculator grossPriceCalculator;

  public UserCartCheckoutCommand(GrossPriceCalculator grossPriceCalculator) {
    super("user", "cart", "checkout");
    this.grossPriceCalculator = grossPriceCalculator;
  }

  @Override
  protected String process(String[] commands) {
    Cart cart = Session.INSTANCE.getCart();
    if (cart.isEmpty()) {
      return "Cart is empty!";
    } else {
      Money amountToPay = grossPriceCalculator.getAggregatedGrossPrice(cart);
      return "Amount to pay: " + amountToPay;
    }
  }
}
