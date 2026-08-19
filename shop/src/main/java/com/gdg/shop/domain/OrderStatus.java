package com.gdg.shop.domain;

public enum OrderStatus {
    ORDERED,
    PAID,
    SHIPPING,
    DELIVERED,
    CANCELED;

    public boolean canTransitionTo(OrderStatus nextStatus) {
        return switch (this) {
            case ORDERED -> nextStatus == PAID || nextStatus == CANCELED;
            case PAID -> nextStatus == SHIPPING || nextStatus == CANCELED;
            case SHIPPING -> nextStatus == DELIVERED;
            case DELIVERED, CANCELED -> false;
        };
    }
}
