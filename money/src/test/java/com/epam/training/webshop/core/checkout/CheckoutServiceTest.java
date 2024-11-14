package com.epam.training.webshop.core.checkout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grosspricecalculator.GrossPriceCalculator;
import com.epam.training.webshop.core.checkout.model.Order;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {
  private static final Currency HUF = Currency.getInstance("HUF");

  @Mock
  private GrossPriceCalculator grossPriceCalculator;
  @Mock
  private CheckoutObservable checkoutObservable;
  @Mock
  private Cart cart;

  @InjectMocks
  private CheckoutService underTest;

  @Test
  void checkoutTest() {
    //GIVEN
    Map<ProductDto, Integer> expectedProducts =
        new HashMap<>(
            Map.of(
                new ProductDto("Xiaomi", new Money(100_000, HUF)), 1));
    when(cart.getProducts()).thenReturn(expectedProducts);
    when(cart.getAggregatedNetPrice()).thenReturn(new Money(100_000, HUF));
    when(grossPriceCalculator.getAggregatedGrossPrice(cart)).thenReturn(new Money(127_000, HUF));
    //WHEN
    Order result = underTest.checkout(cart);
    //THEN
    assertEquals(expectedProducts, result.products());
    assertEquals(100_000, result.netPrice().value());
    assertEquals(127_000, result.grossPrice().value());
  }
}
