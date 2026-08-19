package com.gdg.shop.service;

import com.gdg.shop.common.exception.BadRequestException;
import com.gdg.shop.common.exception.NotFoundException;
import com.gdg.shop.common.message.ErrorMessage;
import com.gdg.shop.domain.Member;
import com.gdg.shop.domain.Order;
import com.gdg.shop.domain.OrderItem;
import com.gdg.shop.domain.OrderStatus;
import com.gdg.shop.domain.Product;
import com.gdg.shop.dto.OrderCreateRequest;
import com.gdg.shop.dto.OrderItemRequest;
import com.gdg.shop.dto.OrderUpdateRequest;
import com.gdg.shop.repository.MemberRepository;
import com.gdg.shop.repository.OrderRepo;
import com.gdg.shop.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServ {

    private final MemberRepository memberRepository;
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;

    @Transactional
    public Long createOrder(OrderCreateRequest request) {
        Member member = memberRepository.findById(request.getMemberId());

        if (member == null) {
            throw new NotFoundException(ErrorMessage.Member_Not_Found);
        }

        Order order = new Order(
                member,
                LocalDateTime.now()
        );

        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepo.findById(itemRequest.getProductId());

            if (product == null) {
                throw new NotFoundException(ErrorMessage.Product_Not_Found);
            }

            product.decreaseStock(itemRequest.getQuantity());
            OrderItem.create(order, product, itemRequest.getQuantity());
        }

        order.calculateTotalPrice();

        orderRepo.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public List<Order> findAllOrders() {
        return orderRepo.findAll();
    }

    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepo.findById(id);

        if (order == null) {
            throw new NotFoundException(ErrorMessage.Order_Not_Found);
        }


        changeStatus(order, OrderStatus.CANCELED);
    }

    @Transactional
    public void updateOrderStatus(Long id, OrderUpdateRequest request) {
        Order order = orderRepo.findById(id);

        if (order == null) {
            throw new NotFoundException(ErrorMessage.Order_Not_Found);
        }

        changeStatus(order, request.getStatus());
    }

    private void changeStatus(Order order, OrderStatus nextStatus) {
        OrderStatus currentStatus = order.getStatus();

        if (!currentStatus.canTransitionTo(nextStatus)) {
            throw new BadRequestException(
                    currentStatus + "에서 " + nextStatus + "로 변경할 수 없습니다."
            );
        }

        if (nextStatus == OrderStatus.CANCELED) {
            order.getOrderItems().forEach(orderItem ->
                    orderItem.getProduct().increaseStock(orderItem.getQuantity())
            );
        }

        order.updateStatus(nextStatus);
    }
}
