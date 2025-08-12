package com.minseop.admin_backoffice.dto;

import lombok.Getter;
import lombok.Setter;


@Setter @Getter
public class OrderForm {

    private String recipientName;
    private String recipientPhone;
    private String deliveryAddress;
    private String paymentMethod;

    private String postcode;       // 우편번호
    private String detailAddress;   // 상세주소

}
