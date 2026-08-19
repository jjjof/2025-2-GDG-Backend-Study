package com.gdg.shop.repository;

import com.gdg.shop.domain.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepo {

    @PersistenceContext
    private EntityManager em;

    public void save(Product product) {
        em.persist(product);
    }

    public Product findById(Long id) {
        return em.find(Product.class, id);
    }
}
