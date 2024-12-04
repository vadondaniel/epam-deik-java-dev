package com.epam.training.webshop.core.checkout.order;

import com.epam.training.webshop.core.checkout.CheckoutObserver;
import com.epam.training.webshop.core.checkout.order.model.OrderDto;
import com.epam.training.webshop.core.checkout.order.persistenece.Order;
import com.epam.training.webshop.core.checkout.order.persistenece.OrderItem;
import com.epam.training.webshop.core.checkout.order.persistenece.OrderRepository;
import com.epam.training.webshop.core.product.model.ProductDto;
import com.epam.training.webshop.core.user.UserService;
import com.epam.training.webshop.core.user.model.UserDto;
import com.epam.training.webshop.core.user.persistence.User;
import com.epam.training.webshop.core.user.persistence.UserRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.InvalidIsolationLevelException;

@RequiredArgsConstructor
@Service
public class OrderService implements CheckoutObserver {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final UserService userService;

  @Override
  public void handleOrder(OrderDto orderDto) {
    UserDto userDto = userService.describe()
        .orElseThrow(() -> new IllegalArgumentException("User must log in first!"));
    User user = userRepository.findByUsername(userDto.username())
        .orElseThrow(() -> new IllegalArgumentException("No such user."));
    Order order = new Order(user, createOrderItemList(orderDto.products()));
    orderRepository.save(order);
  }

  private List<OrderItem> createOrderItemList(Map<ProductDto, Integer> products) {
    return products
        .keySet()
        .stream()
        .map(productDto -> new OrderItem(productDto.getName()))
        .toList();
  }
}
