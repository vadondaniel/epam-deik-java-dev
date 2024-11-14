package com.epam.training.webshop.core.product.model;

import com.epam.training.webshop.core.finance.money.Money;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ProductDto {
    private String name;
    private Money netPrice;
}
