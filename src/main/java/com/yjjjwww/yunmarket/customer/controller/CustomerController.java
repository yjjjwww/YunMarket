package com.yjjjwww.yunmarket.customer.controller;

import com.yjjjwww.yunmarket.customer.model.CustomerSignUpForm;
import com.yjjjwww.yunmarket.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping("/signUp")
  public ResponseEntity<String> signUp(@RequestBody CustomerSignUpForm customerSignUpForm) {
    return ResponseEntity.ok(customerService.signUp(customerSignUpForm));
  }
}
