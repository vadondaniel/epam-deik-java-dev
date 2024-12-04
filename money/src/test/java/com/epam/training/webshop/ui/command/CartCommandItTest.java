package com.epam.training.webshop.ui.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.epam.training.webshop.core.checkout.CheckoutService;
import com.epam.training.webshop.core.checkout.order.persistenece.OrderRepository;
import com.epam.training.webshop.core.user.persistence.User;
import com.epam.training.webshop.core.user.persistence.User.Role;
import com.epam.training.webshop.core.user.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.shell.Shell;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("it")
class CartCommandItTest {

  @Autowired
  Shell shell;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  UserRepository userRepository;

  @SpyBean
  CheckoutService checkoutService;

  @Test
  void testCartCheckoutCommandWhenCartIsEmpty(){
    //GIVEN
    //WHEN
    shell.evaluate(() -> "user cart checkout");
    //THEN
    verify(checkoutService, never()).checkout(any());
  }

  @Test
  void testCartCheckoutCommandWhenCartIsNotEmpty(){
    //GIVEN
    userRepository.save(new User("admin", "admin", Role.ADMIN));
    shell.evaluate(() -> "user login admin admin");
    shell.evaluate(() -> "admin product create xiaomi 100");
    shell.evaluate(() -> "user cart addProduct xiaomi 2");
    //WHEN
    shell.evaluate(() -> "user cart checkout");
    //THEN
    verify(checkoutService).checkout(any());
    assertEquals(1, orderRepository.findAll().size());
  }

}
