package com.minseop.admin_backoffice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartUpdateRequest {
    private Long cartItemId;
    private int quantity;
}

