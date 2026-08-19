package com.gdg.shop.service;

import com.gdg.shop.domain.Product;
import com.gdg.shop.dto.ProductCreateRequest;
import com.gdg.shop.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServ {

    private final ProductRepo productRepo;

    @Transactional
    public Long createProduct(ProductCreateRequest request) {
        Product product = new Product(
                request.getName(),
                request.getPrice(),
                request.getStockQuantity()
        );

        productRepo.save(product);

        return product.getId();
    }
}
