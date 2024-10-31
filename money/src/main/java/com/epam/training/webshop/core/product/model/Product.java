package com.epam.training.webshop.core.product.model;

import com.epam.training.webshop.core.finance.money.Money;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Product {
    private String name;
    private Money netPrice;
}
