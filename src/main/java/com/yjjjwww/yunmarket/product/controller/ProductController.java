package com.yjjjwww.yunmarket.product.controller;

import com.yjjjwww.yunmarket.product.model.ProductInfo;
import com.yjjjwww.yunmarket.product.model.ProductRegisterForm;
import com.yjjjwww.yunmarket.product.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

  private final ProductService productService;
  public static final String TOKEN_HEADER = "Authorization";

  private static final String REGISTER_PRODUCT_SUCCESS = "상품 등록 완료";

  @PostMapping
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<String> registerProduct(
      @RequestHeader(name = TOKEN_HEADER) String token,
      @RequestBody ProductRegisterForm productRegisterForm) {
    productService.register(token, productRegisterForm.toServiceForm());
    return ResponseEntity.ok(REGISTER_PRODUCT_SUCCESS);
  }

  @GetMapping("/list")
  public ResponseEntity<List<ProductInfo>> getLatestProducts(Pageable pageable) {
    return ResponseEntity.ok(productService.getProducts(pageable));
  }

  @GetMapping("/search")
  public ResponseEntity<List<ProductInfo>> searchProducts(
      @RequestParam("keyword") String keyword,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size
  ) {
    return ResponseEntity.ok(productService.searchProducts(keyword, page, size));
  }

  @GetMapping("/info/{id}")
  public ResponseEntity<ProductInfo> getProductInfo(
      @PathVariable long id
  ) {
    return ResponseEntity.ok(productService.getProductInfo(id));
  }
}
