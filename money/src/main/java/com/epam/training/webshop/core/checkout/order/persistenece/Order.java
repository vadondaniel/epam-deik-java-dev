package com.epam.training.webshop.core.checkout.order.persistenece;

import com.epam.training.webshop.core.user.persistence.User;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Orders")

public class Order {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  private User user;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<OrderItem> orderItemList;

  public Order(User user, List<OrderItem> orderItemList) {
    this.user = user;
    this.orderItemList = orderItemList;
  }

  public Order() {
  }
}
