package com.yjjjwww.yunmarket.product.service;

import com.yjjjwww.yunmarket.common.UserVo;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.entity.Category;
import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.product.entity.ProductDocument;
import com.yjjjwww.yunmarket.product.model.ProductInfo;
import com.yjjjwww.yunmarket.product.model.ProductRegisterServiceForm;
import com.yjjjwww.yunmarket.product.repository.CategoryRepository;
import com.yjjjwww.yunmarket.product.repository.ElasticSearchProductRepository;
import com.yjjjwww.yunmarket.product.repository.ProductRepository;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import com.yjjjwww.yunmarket.seller.repository.SellerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

  private final JwtTokenProvider provider;
  private final SellerRepository sellerRepository;
  private final ProductRepository productRepository;
  private final ElasticSearchProductRepository elasticSearchProductRepository;
  private final CategoryRepository categoryRepository;

  @Override
  public void register(String token, ProductRegisterServiceForm form) {
    if (isStringEmpty(form.getName()) || form.getPrice() <= 0 || isStringEmpty(
        form.getDescription()) || form.getQuantity() <= 0) {
      throw new CustomException(ErrorCode.INVALID_PRODUCT_REGISTER);
    }

    UserVo vo = provider.getUserVo(token);

    Seller seller = sellerRepository.findByEmail(vo.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Category category = categoryRepository.findById(form.getCategoryId())
        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

    Product product = Product.builder()
        .category(category)
        .seller(seller)
        .name(form.getName())
        .price(form.getPrice())
        .description(form.getDescription())
        .quantity(form.getQuantity())
        .image(form.getImage())
        .orderedCnt(0)
        .deletedYn(false)
        .build();

    productRepository.save(product);
    elasticSearchProductRepository.save(ProductDocument.from(product));
  }

  @Override
  public List<ProductInfo> getProducts(Pageable pageable) {
    Page<Product> products = productRepository.findAll(pageable);

    return ProductInfo.toList(products);
  }

  @Override
  public List<ProductInfo> searchProducts(String keyword, Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    List<ProductDocument> products = elasticSearchProductRepository.findByNameOrDescription(keyword,
        pageable);

    return ProductInfo.toListFromDocument(products);
  }

  private static boolean isStringEmpty(String str) {
    return str == null || str.isBlank();
  }
}
