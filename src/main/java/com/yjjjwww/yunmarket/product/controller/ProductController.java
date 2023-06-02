package com.yjjjwww.yunmarket.product.controller;

import com.yjjjwww.yunmarket.common.ClientUtils;
import com.yjjjwww.yunmarket.product.model.ProductInfo;
import com.yjjjwww.yunmarket.product.model.ProductRegisterForm;
import com.yjjjwww.yunmarket.product.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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

  private static final String REGISTER_PRODUCT_SUCCESS = "상품 등록 완료";

  @ApiOperation(value = "Seller 상품 등록")
  @PostMapping
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<String> registerProduct(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestBody ProductRegisterForm productRegisterForm) {
    productService.register(token, productRegisterForm.toServiceForm());
    return ResponseEntity.ok(REGISTER_PRODUCT_SUCCESS);
  }

  @ApiOperation(value = "상품 Sort 기준 조회")
  @GetMapping("/list")
  public ResponseEntity<List<ProductInfo>> getLatestProducts(Pageable pageable) {
    return ResponseEntity.ok(productService.getProducts(pageable));
  }

  @ApiOperation(value = "상품 검색")
  @GetMapping("/search")
  public ResponseEntity<List<ProductInfo>> searchProducts(
      @Parameter(name = "keyword", description = "검색 키워드")
      @RequestParam("keyword") String keyword,
      @Parameter(name = "page", description = "페이지")
      @RequestParam("page") Integer page,
      @Parameter(name = "size", description = "페이지 크기")
      @RequestParam("size") Integer size
  ) {
    return ResponseEntity.ok(productService.searchProducts(keyword, page, size));
  }

  @ApiOperation(value = "한 상품의 상세정보 조회")
  @GetMapping("/info/{id}")
  public ResponseEntity<ProductInfo> getProductInfo(
      @PathVariable long id,
      HttpServletRequest request
  ) {
    String userIp = ClientUtils.getIp(request);
    return ResponseEntity.ok(productService.getProductInfo(id, userIp));
  }

  @ApiOperation(value = "접속한 Ip에서 조회한 상품과 관련된 인기 상품 조회")
  @GetMapping("/recent")
  public ResponseEntity<List<ProductInfo>> getRecentViewedProducts(
      HttpServletRequest request,
      @Parameter(name = "page", description = "페이지")
      @RequestParam("page") Integer page,
      @Parameter(name = "size", description = "페이지 크기")
      @RequestParam("size") Integer size
  ) {
    String userIp = ClientUtils.getIp(request);
    return ResponseEntity.ok(productService.getRecentViewedProducts(userIp, page, size));
  }
}
