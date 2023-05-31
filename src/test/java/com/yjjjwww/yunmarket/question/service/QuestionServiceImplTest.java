package com.yjjjwww.yunmarket.question.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.yjjjwww.yunmarket.common.UserVo;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.customer.repository.CustomerRepository;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.entity.Category;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

  @Mock
  private JwtTokenProvider provider;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private SellerRepository sellerRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private AnswerRepository answerRepository;

  @InjectMocks
  private QuestionServiceImpl questionService;

  @Test
  void registerQuestionSuccess() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Customer customer = Customer.builder()
        .id(1L)
        .build();
    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(customer));

    QuestionRegisterServiceForm form = QuestionRegisterServiceForm.builder()
        .productId(1L)
        .contents("문의 내용")
        .build();

    Product product = Product.builder()
        .id(1L)
        .price(4000)
        .name("상품 이름")
        .image("이미지")
        .quantity(1000)
        .category(Category.builder().build())
        .description("상품 설명")
        .orderedCnt(1)
        .deletedYn(false)
        .seller(Seller.builder()
            .id(1L)
            .build())
        .build();

    given(productRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(product));

    //when
    questionService.registerQuestion("token", form);

    //then
    verify(questionRepository, times(1)).save(any(Question.class));
  }

  @Test
  void registerQuestionFail_PRODUCT_NOT_FOUND() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Customer customer = Customer.builder()
        .id(1L)
        .build();
    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(customer));

    QuestionRegisterServiceForm form = QuestionRegisterServiceForm.builder()
        .productId(1L)
        .contents("문의 내용")
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> questionService.registerQuestion("token", form));

    //then
    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void registerAnswerSuccess() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Seller seller = Seller.builder()
        .id(1L)
        .build();
    given(sellerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(seller));

    AnswerRegisterServiceForm form = AnswerRegisterServiceForm.builder()
        .questionId(1L)
        .contents("답변 내용")
        .build();

    Question question = Question.builder()
        .id(1L)
        .build();

    given(questionRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(question));

    //when
    questionService.registerAnswer("token", form);

    //then
    verify(answerRepository, times(1)).save(any(Answer.class));
  }

  @Test
  void registerAnswerFail_QUESTION_NOT_FOUND() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Seller seller = Seller.builder()
        .id(1L)
        .build();
    given(sellerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(seller));

    AnswerRegisterServiceForm form = AnswerRegisterServiceForm.builder()
        .questionId(1L)
        .contents("답변 내용")
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> questionService.registerAnswer("token", form));

    //then
    assertEquals(ErrorCode.QUESTION_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void getQuestionsSuccess() {
    //given
    List<Question> questionList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      List<Answer> answerList = new ArrayList<>();

      Question question = Question.builder()
          .id((long) i)
          .customer(Customer.builder()
              .email("이메일")
              .build())
          .contents("질문 내용")
          .answerList(answerList)
          .build();
      questionList.add(question);
    }

    given(questionRepository.findByProductId(anyLong(), any()))
        .willReturn(questionList);

    //when
    List<QuestionDto> questionDtos = questionService.getQuestions(1L, 1, 5);

    //then
    assertEquals(3, questionDtos.size());
  }

  @Test
  void getQuestionsFail_QUESTION_NOT_FOUND() {
    //given
    List<Question> questionList = new ArrayList<>();

    given(questionRepository.findByProductId(anyLong(), any()))
        .willReturn(questionList);

    CustomException exception = assertThrows(CustomException.class,
        () -> questionService.getQuestions(1L, 1, 5));

    //then
    assertEquals(ErrorCode.QUESTION_NOT_FOUND, exception.getErrorCode());
  }
}