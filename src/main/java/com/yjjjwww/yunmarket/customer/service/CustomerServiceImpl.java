package com.yjjjwww.yunmarket.customer.service;

import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.customer.model.CustomerSignUpForm;
import com.yjjjwww.yunmarket.customer.repository.CustomerRepository;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;

  private static final String SIGNUP_SUCCESS = "회원가입 성공";

  @Override
  public String signUp(CustomerSignUpForm customerSignUpForm) {
    Optional<Customer> optionalCustomer =
        customerRepository.findByEmail(customerSignUpForm.getEmail());
    if (optionalCustomer.isPresent()) {
      throw (new CustomException(ErrorCode.ALREADY_SIGNUP_EMAIL));
    }

    if (!isValidPassword(customerSignUpForm.getPassword())) {
      throw new CustomException(ErrorCode.INVALID_PASSWORD);
    }

    if (!isValidPhone(customerSignUpForm.getPhone())) {
      throw new CustomException(ErrorCode.INVALID_PHONE);
    }

    String encPassword = BCrypt.hashpw(customerSignUpForm.getPassword(), BCrypt.gensalt());

    Customer customer = Customer.builder()
        .email(customerSignUpForm.getEmail())
        .password(encPassword)
        .phone(customerSignUpForm.getPhone())
        .address(customerSignUpForm.getAddress())
        .point(0)
        .deletedYn(false)
        .build();

    customerRepository.save(customer);

    return SIGNUP_SUCCESS;
  }

  /**
   * 핸드폰 번호 형식 검사
   */
  private static boolean isValidPhone(String phone) {
    if (phone.length() > 11 || phone.length() < 5) {
      return false;
    }
    boolean err = false;
    String regex = "^[0-9]*$";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(phone);
    if (m.matches()) {
      err = true;
    }
    return err;
  }

  /**
   * 비밀번호 형식 검사(최소 8자리에 숫자, 문자, 특수문자 각각 1개 이상 포함)
   */
  private static boolean isValidPassword(String password) {
    boolean err = false;
    String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(password);
    if (m.matches()) {
      err = true;
    }
    return err;
  }
}
