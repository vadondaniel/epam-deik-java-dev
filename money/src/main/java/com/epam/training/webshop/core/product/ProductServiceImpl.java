package com.epam.training.webshop.core.product;

import com.epam.training.webshop.core.product.model.ProductDto;
import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.persistence.Product;
import com.epam.training.webshop.core.product.persistence.ProductRepository;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  @Override
  public List<ProductDto> getAllProducts() {
    return productRepository.findAll()
        .stream()
        .map(this::mapEntityToDto)
        .toList();
  }

  @Override
  public Optional<ProductDto> getProductByName(String name) {
    return mapEntityToDto(productRepository.findByName(name));
  }

  @Override
  public Optional<ProductDto> getProductById(Long id) {
    return Optional.ofNullable(mapEntityToDto(productRepository.getReferenceById(id)));
  }

  @Override
  public void createProduct(ProductDto productDto) {
    Product product = new Product(
        productDto.getName(),
        productDto.getNetPrice().value(),
        productDto.getNetPrice().currency().getCurrencyCode()
    );
    productRepository.save(product);
  }

  private ProductDto mapEntityToDto(Product product) {
    return ProductDto.builder()
        .name(product.getName())
        .netPrice(new Money(
            product.getNetPriceAmount(),
            Currency.getInstance(product.getNetPriceCurrencyCode()))
        )
        .build();
  }

  private Optional<ProductDto> mapEntityToDto(Optional<Product> product) {
    return product.map(this::mapEntityToDto);
  }
}
