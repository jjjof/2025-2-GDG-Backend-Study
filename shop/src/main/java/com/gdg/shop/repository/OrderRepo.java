package com.gdg.shop.repository;

import com.gdg.shop.domain.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepo {

    @PersistenceContext
    private EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public List<Order> findAll() {
        return em.createQuery("select o from Order o", Order.class)
                .getResultList();
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }
}
