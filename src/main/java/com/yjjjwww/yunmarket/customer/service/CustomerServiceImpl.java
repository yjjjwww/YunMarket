package com.yjjjwww.yunmarket.customer.service;

import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.customer.model.CustomerSignUpServiceForm;
import com.yjjjwww.yunmarket.customer.repository.CustomerRepository;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
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

  private static final String PHONE_REGEX = "^[0-9]*$";
  private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";

  @Override
  public void signUp(CustomerSignUpServiceForm customerSignUpServiceForm) {
    Optional<Customer> optionalCustomer =
        customerRepository.findByEmail(customerSignUpServiceForm.getEmail());

    optionalCustomer.ifPresent(it -> {
      throw new CustomException(ErrorCode.ALREADY_SIGNUP_EMAIL);
    });

    if (!isValidPassword(customerSignUpServiceForm.getPassword())) {
      throw new CustomException(ErrorCode.INVALID_PASSWORD);
    }

    if (!isValidPhone(customerSignUpServiceForm.getPhone())) {
      throw new CustomException(ErrorCode.INVALID_PHONE);
    }

    String encPassword = BCrypt.hashpw(customerSignUpServiceForm.getPassword(), BCrypt.gensalt());

    Customer customer = Customer.builder()
        .email(customerSignUpServiceForm.getEmail())
        .password(encPassword)
        .phone(customerSignUpServiceForm.getPhone())
        .address(customerSignUpServiceForm.getAddress())
        .point(0)
        .deletedYn(false)
        .build();

    customerRepository.save(customer);
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
