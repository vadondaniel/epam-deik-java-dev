package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.cart.Cart;

public class UserCartClearCommand extends AbstractCommand {

  private Cart cart;
  public UserCartClearCommand(Cart cart) {
    super("user", "cart", "clear");
    this.cart = cart;
  }

  @Override
  protected String process(String[] commands) {
    cart.clear();
    return "Cart is cleared!";
  }
}
