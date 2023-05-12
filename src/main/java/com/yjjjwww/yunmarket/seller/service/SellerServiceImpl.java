package com.yjjjwww.yunmarket.seller.service;

import com.yjjjwww.yunmarket.common.UserType;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import com.yjjjwww.yunmarket.seller.model.SellerSignInServiceForm;
import com.yjjjwww.yunmarket.seller.model.SellerSignUpServiceForm;
import com.yjjjwww.yunmarket.seller.repository.SellerRepository;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SellerServiceImpl implements SellerService {

  private final SellerRepository sellerRepository;
  private final JwtTokenProvider provider;

  private static final String PHONE_REGEX = "^[0-9]*$";
  private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";

  @Override
  public void signUp(SellerSignUpServiceForm sellerSignUpServiceForm) {
    Optional<Seller> optionalSeller =
        sellerRepository.findByEmail(sellerSignUpServiceForm.getEmail());

    optionalSeller.ifPresent(it -> {
      throw new CustomException(ErrorCode.ALREADY_SIGNUP_EMAIL);
    });

    if (!isValidPassword(sellerSignUpServiceForm.getPassword())) {
      throw new CustomException(ErrorCode.INVALID_PASSWORD);
    }

    if (!isValidPhone(sellerSignUpServiceForm.getPhone())) {
      throw new CustomException(ErrorCode.INVALID_PHONE);
    }

    String encPassword = BCrypt.hashpw(sellerSignUpServiceForm.getPassword(), BCrypt.gensalt());

    Seller seller = Seller.builder()
        .email(sellerSignUpServiceForm.getEmail())
        .password(encPassword)
        .phone(sellerSignUpServiceForm.getPhone())
        .deletedYn(false)
        .build();

    sellerRepository.save(seller);
  }

  @Override
  public String signIn(SellerSignInServiceForm form) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    Optional<Seller> optionalSeller =
        sellerRepository.findByEmail(form.getEmail());

    if (optionalSeller.isEmpty() || !encoder.matches(form.getPassword(),
        optionalSeller.get().getPassword())) {
      throw new CustomException(ErrorCode.LOGIN_CHECK_FAIL);
    }

    Seller seller = optionalSeller.get();

    return provider.createToken(seller.getEmail(), seller.getId(), UserType.SELLER);
  }

  /**
   * 핸드폰 번호 형식 검사
   */
  private static boolean isValidPhone(String phone) {
    if (phone.length() > 11 || phone.length() < 5) {
      return false;
    }
    Pattern p = Pattern.compile(PHONE_REGEX);
    Matcher m = p.matcher(phone);
    return m.matches();
  }

  /**
   * 비밀번호 형식 검사(최소 8자리에 숫자, 문자, 특수문자 각각 1개 이상 포함)
   */
  private static boolean isValidPassword(String password) {
    Pattern p = Pattern.compile(PASSWORD_REGEX);
    Matcher m = p.matcher(password);
    return m.matches();
  }
}
