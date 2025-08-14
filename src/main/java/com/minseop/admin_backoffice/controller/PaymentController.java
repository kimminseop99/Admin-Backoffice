package com.minseop.admin_backoffice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    private static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    /**
     * 결제 성공 시 토스 서버에 승인 요청
     */
    @GetMapping("/success")
    public String paymentSuccess(@RequestParam String paymentKey,
                                 @RequestParam String orderId,
                                 @RequestParam Long amount,
                                 Model model) {

        // 1. 요청 바디 생성
        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", paymentKey);
        body.put("orderId", orderId);
        body.put("amount", amount);

        // 2. Basic Auth 헤더
        String authHeader = "Basic " + Base64.getEncoder()
                .encodeToString((tossSecretKey + ":").getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", authHeader);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                    TOSS_CONFIRM_URL, requestEntity, Map.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                // 승인 성공
                Map<String, Object> response = responseEntity.getBody();

                // TODO: 주문 상태 DB 업데이트 (orderId 기준)
                model.addAttribute("orderId", orderId);
                model.addAttribute("amount", amount);
                model.addAttribute("paymentMethod", response.get("method"));
                model.addAttribute("approvedAt", response.get("approvedAt"));
                return "payment/success";
            } else {
                // 승인 실패
                model.addAttribute("errorCode", responseEntity.getStatusCodeValue());
                model.addAttribute("errorMessage", "승인 요청 실패");
                return "payment/fail";
            }
        } catch (Exception e) {
            model.addAttribute("errorCode", "EXCEPTION");
            model.addAttribute("errorMessage", e.getMessage());
            return "payment/fail";
        }
    }

    /**
     * 결제 실패 시 처리
     */
    @GetMapping("/fail")
    public String paymentFail(@RequestParam String code,
                              @RequestParam String message,
                              Model model) {
        model.addAttribute("errorCode", code);
        model.addAttribute("errorMessage", message);
        return "payment/fail";
    }
}
