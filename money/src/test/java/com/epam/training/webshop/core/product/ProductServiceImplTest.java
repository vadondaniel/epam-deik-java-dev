package com.epam.training.webshop.core.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import java.util.Currency;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductServiceImplTest {

  private ProductServiceImpl underTest;

  @BeforeEach
  void setUp() {
    underTest = new ProductServiceImpl();
    underTest.initDb();
  }

  @Test
  void testGetProductByNameShouldReturnHypoWhenInputProductNameIsHypo() {
    // Given
    ProductDto expected = new ProductDto("Xiaomi", new Money(100_000, Currency.getInstance("HUF")));
    // When
    Optional<ProductDto> actual = underTest.getProductByName("Xiaomi");
    // Then
    assertTrue(actual.isPresent());
    assertEquals(expected, actual.get());
  }

  @Test
  void testGetProductByNameShouldReturnOptionalEmptyWhenInputProductNameDoesNotExist() {
    // Given
    Optional<ProductDto> expected = Optional.empty();
    // When
    Optional<ProductDto> actual = underTest.getProductByName("Liszt");
    // Then
    assertTrue(actual.isEmpty());
    assertEquals(expected, actual);
  }

  @Test
  void testGetProductByNameShouldReturnOptionalEmptyWhenInputProductNameIsNull() {
    // Given
    Optional<ProductDto> expected = Optional.empty();
    // When
    Optional<ProductDto> actual = underTest.getProductByName(null);
    // Then
    assertTrue(actual.isEmpty());
    assertEquals(expected, actual);
  }

}
