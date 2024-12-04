package com.epam.training.webshop.core.checkout.order;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.epam.training.webshop.core.checkout.order.model.OrderDto;
import com.epam.training.webshop.core.checkout.order.persistenece.OrderRepository;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.user.UserService;
import com.epam.training.webshop.core.user.model.UserDto;
import com.epam.training.webshop.core.user.persistence.User;
import com.epam.training.webshop.core.user.persistence.User.Role;
import com.epam.training.webshop.core.user.persistence.UserRepository;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  private static final UserDto USER_DTO = new UserDto("user", Role.USER);
  private static final User USER = new User("user", "user", Role.USER);
  private static OrderDto ORDER_DTO =
      new OrderDto(
          Map.of(),
          new Money(100, Currency.getInstance("HUF")),
          new Money(67, Currency.getInstance("HUF")));

  @Mock
  private OrderRepository orderRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserService userService;

  @InjectMocks
  private OrderService underTest;

  @Test
  void testHandleOrderWhenUserIsNotLoggedIn() {
    //GIVEN
    when(userService.describe()).thenReturn(Optional.empty());
    //WHEN
    assertThrows(
        IllegalArgumentException.class,
        () -> underTest.handleOrder(ORDER_DTO));

    //THEN
    verify(userService).describe();
    verifyNoMoreInteractions(userRepository, orderRepository);
  }

  @Test
  void testHandleOrderWhenUserIsNotFoundInDb() {
    //GIVEN
    when(userService.describe()).thenReturn(Optional.of(USER_DTO));
    when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
    //WHEN
    assertThrows(
        IllegalArgumentException.class,
        () -> underTest.handleOrder(ORDER_DTO));

    //THEN
    verify(userService).describe();
    verify(userRepository).findByUsername("user");
    verifyNoMoreInteractions(orderRepository);
  }

  @Test
  void testHandleOrderWhenEverythingWentWell() {
    //GIVEN
    when(userService.describe()).thenReturn(Optional.of(USER_DTO));
    when(userRepository.findByUsername("user")).thenReturn(Optional.of(USER));

    //WHEN
    underTest.handleOrder(ORDER_DTO);

    //THEN
    verify(userService).describe();
    verify(userRepository).findByUsername("user");
    verify(orderRepository).save(any());
  }
}
