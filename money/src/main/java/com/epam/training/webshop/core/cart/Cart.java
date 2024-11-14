package com.epam.training.webshop.core.cart;

import com.epam.training.webshop.core.checkout.CheckoutObserver;
import com.epam.training.webshop.core.checkout.model.Order;
import com.epam.training.webshop.core.finance.bank.Bank;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.model.ProductDto;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Cart implements CheckoutObserver {

  private Bank bank;
  @Getter
  private Map<ProductDto, Integer> products;

  public static Cart createEmptyCart(Bank bank) {
    return new Cart(bank, new HashMap<>());
  }

  public void add(ProductDto product, int amount) {
    if (product != null && amount > 0) {
      products.merge(product, amount, Integer::sum);
    }
  }

  public boolean isEmpty() {
    return products.isEmpty();
  }

  public void clear() {
    products.clear();
  }

  public boolean containsProduct(ProductDto product) {
    return products.containsKey(product);
  }

  public void removeProduct(ProductDto product) {
    products.remove(product);
  }

  public Money getAggregatedNetPrice() {
    Money aggregatedPrice = new Money(0, Currency.getInstance("HUF"));
    for (Map.Entry<ProductDto, Integer> entry : products.entrySet()) {
      aggregatedPrice = aggregatedPrice.add(entry.getKey().getNetPrice().multiply(entry.getValue()),
          bank);
    }
    return aggregatedPrice;
  }

  @Override
  public void handleOrder(Order order) {
    clear();
  }
}
