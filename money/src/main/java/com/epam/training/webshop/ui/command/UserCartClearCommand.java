package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.ui.session.Session;

public class UserCartClearCommand extends AbstractCommand {

  public UserCartClearCommand() {
    super("user", "cart", "clear");
  }

  @Override
  protected String process(String[] commands) {
    Cart cart = Session.INSTANCE.getCart();
    cart.clear();
    return "Cart is cleared!";
  }
}
