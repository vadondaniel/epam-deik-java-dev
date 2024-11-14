package com.epam.training.webshop.core.product;

import com.epam.training.webshop.core.product.model.ProductDto;
import java.util.List;
import java.util.Optional;

public interface ProductService {

  List<ProductDto> getAllProducts();

  Optional<ProductDto> getProductByName(String name);
  Optional<ProductDto> getProductById(Long id);

  void createProduct(ProductDto product);
}
