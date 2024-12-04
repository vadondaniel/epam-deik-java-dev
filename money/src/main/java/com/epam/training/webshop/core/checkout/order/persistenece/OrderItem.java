package com.epam.training.webshop.core.checkout.order.persistenece;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderItem {
  @Id
  @GeneratedValue
  private Long id;
  private String name;

  public OrderItem(String name) {
    this.name = name;
  }
  public OrderItem() {
  }
}
