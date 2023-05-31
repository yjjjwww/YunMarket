package com.yjjjwww.yunmarket.question.service;

import com.yjjjwww.yunmarket.common.UserVo;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.customer.repository.CustomerRepository;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.product.repository.ProductRepository;
import com.yjjjwww.yunmarket.question.entity.Answer;
import com.yjjjwww.yunmarket.question.entity.Question;
import com.yjjjwww.yunmarket.question.model.AnswerRegisterServiceForm;
import com.yjjjwww.yunmarket.question.model.QuestionDto;
import com.yjjjwww.yunmarket.question.model.QuestionRegisterServiceForm;
import com.yjjjwww.yunmarket.question.repository.AnswerRepository;
import com.yjjjwww.yunmarket.question.repository.QuestionRepository;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import com.yjjjwww.yunmarket.seller.repository.SellerRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuestionServiceImpl implements QuestionService{

  private final JwtTokenProvider provider;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;
  private final SellerRepository sellerRepository;
  private final QuestionRepository questionRepository;
  private final AnswerRepository answerRepository;

  @Override
  public void registerQuestion(String token, QuestionRegisterServiceForm form) {
    Customer customer = getCustomerFromToken(token);

    Product product = productRepository.findById(form.getProductId())
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    Seller seller = product.getSeller();

    Question question = Question.builder()
        .product(product)
        .customer(customer)
        .seller(seller)
        .contents(form.getContents())
        .deletedYn(false)
        .build();

    questionRepository.save(question);
  }

  @Override
  public void registerAnswer(String token, AnswerRegisterServiceForm form) {
    Seller seller = getSellerFromToken(token);

    Question question = questionRepository.findById(form.getQuestionId())
        .orElseThrow(() -> new CustomException(ErrorCode.QUESTION_NOT_FOUND));

    Answer answer = Answer.builder()
        .question(question)
        .seller(seller)
        .contents(form.getContents())
        .deletedYn(false)
        .build();

    answerRepository.save(answer);
  }

  @Override
  public List<QuestionDto> getQuestions(Long productId, Integer page, Integer size) {
    Pageable pageable = PageRequest.of(page - 1, size);

    List<Question> questionList = questionRepository.findByProductId(productId, pageable);

    if (questionList.size() == 0) {
      throw new CustomException(ErrorCode.QUESTION_NOT_FOUND);
    }

    return QuestionDto.toDtoList(questionList);
  }

  private Customer getCustomerFromToken(String token) {
    UserVo vo = provider.getUserVo(token);

    Customer customer = customerRepository.findById(vo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return customer;
  }

  private Seller getSellerFromToken(String token) {
    UserVo vo = provider.getUserVo(token);

    Seller seller = sellerRepository.findById(vo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return seller;
  }
}
