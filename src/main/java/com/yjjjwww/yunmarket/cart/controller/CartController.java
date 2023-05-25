package com.yjjjwww.yunmarket.cart.controller;

import com.yjjjwww.yunmarket.cart.model.AddCartForm;
import com.yjjjwww.yunmarket.cart.model.EditCartForm;
import com.yjjjwww.yunmarket.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

  private final CartService cartService;

  private static final String ADD_CART_SUCCESS = "장바구니 추가 완료";
  private static final String EDIT_CART_SUCCESS = "장바구니 수정 완료";
  private static final String DELETE_CART_ITEM_SUCCESS = "장바구니 상품 삭제 완료";
  private static final String DELETE_ALL_CART_SUCCESS = "삭제 완료";

  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<String> registerProduct(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestBody AddCartForm form
  ) {
    cartService.addCart(token, form);
    return ResponseEntity.ok(ADD_CART_SUCCESS);
  }

  @PutMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<String> editCart(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestBody EditCartForm form
  ) {
    cartService.editCart(token, form);
    return ResponseEntity.ok(EDIT_CART_SUCCESS);
  }

  @DeleteMapping("/item/{id}")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<String> deleteCartItem(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @PathVariable("id") long id
  ) {
    cartService.deleteCartItem(token, id);
    return ResponseEntity.ok(DELETE_CART_ITEM_SUCCESS);
  }

  @DeleteMapping("/items")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<String> deleteAllCart(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token
  ) {
    long cnt = cartService.deleteAllCart(token);
    return ResponseEntity.ok(cnt + "개의 상품 " + DELETE_ALL_CART_SUCCESS);
  }
}
