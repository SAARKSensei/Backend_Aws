package com.sensei.backend.service;

import com.sensei.backend.dto.planpurchase.PlanPurchaseRequestDTO;
import com.sensei.backend.entity.PaymentTransaction;

public interface PlanPurchaseService {

    PaymentTransaction purchasePlan(PlanPurchaseRequestDTO dto);
}
