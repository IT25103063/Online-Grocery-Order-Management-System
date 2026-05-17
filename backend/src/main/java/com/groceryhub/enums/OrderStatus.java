package com.groceryhub.enums;

public enum OrderStatus {
    pending, confirmed, processing, out_for_delivery, delivered, cancelled;

    public String getDisplayName() {
        return name().replace('_', ' ').substring(0, 1).toUpperCase() +
               name().replace('_', ' ').substring(1);
    }
}
