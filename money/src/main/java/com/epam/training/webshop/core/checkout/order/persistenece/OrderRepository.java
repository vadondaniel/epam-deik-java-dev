package com.epam.training.webshop.core.checkout.order.persistenece;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
