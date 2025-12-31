package com.saas.delivery.controller;

import com.saas.delivery.model.PromoCode;
import com.saas.delivery.service.PromoCodeService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/promo-codes")
public class PromoCodeController {

    private final PromoCodeService service;

    public PromoCodeController(PromoCodeService service) {
        this.service = service;
    }

    @PostMapping
    public void createPromoCode(@RequestBody PromoCode promoCode) {
        try {
            service.createPromoCode(promoCode);
        } catch (Exception e) {
            throw new RuntimeException("Error creating promo code", e);
        }
    }

    @PostMapping("/verify")
    public Map<String, Object> verifyPromoCode(@RequestBody Map<String, Object> payload) {
        String code = (String) payload.get("code");
        Double amount = Double.valueOf(payload.get("amount").toString());

        try {
            double discount = service.calculateDiscount(code, amount);
            return Map.of("valid", true, "discount", discount);
        } catch (Exception e) {
            return Map.of("valid", false, "message", e.getMessage());
        }
    }
}
