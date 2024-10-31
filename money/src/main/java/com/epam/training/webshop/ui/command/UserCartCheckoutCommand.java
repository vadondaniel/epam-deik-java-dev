package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grosspricecalculator.GrossPriceCalculator;
import com.epam.training.webshop.core.checkout.CheckoutService;
import com.epam.training.webshop.core.checkout.model.Order;
import com.epam.training.webshop.core.finance.money.Money;

public class UserCartCheckoutCommand extends AbstractCommand{

  private CheckoutService checkoutService;
  private Cart cart;

  public UserCartCheckoutCommand(CheckoutService checkoutService, Cart cart) {
    super("user", "cart", "checkout");
    this.checkoutService = checkoutService;
    this.cart = cart;
  }

  @Override
  protected String process(String[] commands) {
    if (cart.isEmpty()) {
      return "Cart is empty!";
    } else {
      Order order = checkoutService.checkout();
      return "Your order: " + order;
    }
  }
}
