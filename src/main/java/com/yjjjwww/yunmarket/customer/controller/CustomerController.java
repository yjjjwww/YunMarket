package com.yjjjwww.yunmarket.customer.controller;

import com.yjjjwww.yunmarket.customer.model.CustomerSignInForm;
import com.yjjjwww.yunmarket.customer.model.CustomerSignUpForm;
import com.yjjjwww.yunmarket.customer.service.CustomerService;
import io.swagger.annotations.ApiOperation;
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
  private static final String SIGNUP_SUCCESS = "회원가입 성공";

  @ApiOperation(value = "Customer 회원가입")
  @PostMapping("/signUp")
  public ResponseEntity<String> signUp(@RequestBody CustomerSignUpForm customerSignUpForm) {
    customerService.signUp(customerSignUpForm.toServiceForm());
    return ResponseEntity.ok(SIGNUP_SUCCESS);
  }

  @ApiOperation(value = "Customer 로그인")
  @PostMapping("/signIn")
  public ResponseEntity<String> signIn(@RequestBody CustomerSignInForm customerSignInForm) {
    return ResponseEntity.ok(customerService.signIn(customerSignInForm.toServiceForm()));
  }
}
