package com.yjjjwww.yunmarket.seller.controller;

import com.yjjjwww.yunmarket.seller.model.SellerSignUpForm;
import com.yjjjwww.yunmarket.seller.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {

  private final SellerService sellerService;
  private static final String SIGNUP_SUCCESS = "회원가입 성공";

  @PostMapping("/signUp")
  public ResponseEntity<String> signUp(@RequestBody SellerSignUpForm sellerSignUpForm) {
    sellerService.signUp(sellerSignUpForm.toServiceForm());
    return ResponseEntity.ok(SIGNUP_SUCCESS);
  }
}
