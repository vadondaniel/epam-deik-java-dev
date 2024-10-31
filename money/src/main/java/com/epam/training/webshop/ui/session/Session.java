package com.epam.training.webshop.ui.session;

import com.epam.training.webshop.core.finance.bank.Bank;
import com.epam.training.webshop.core.finance.bank.StaticBank;
import com.epam.training.webshop.core.cart.Cart;
import lombok.Getter;

public enum Session {

  INSTANCE;

  private Bank bank = new StaticBank();

  @Getter
  private Cart cart = Cart.createEmptyCart(bank);
}
