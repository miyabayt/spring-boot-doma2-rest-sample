package com.bigtreetc.sample.doma;

import com.bigtreetc.sample.doma.base.util.MessageUtils;
import com.bigtreetc.sample.doma.base.web.aop.ElapsedMillisLoggingInterceptor;
import com.bigtreetc.sample.doma.base.web.aop.SetAuditInfoInterceptor;
import com.bigtreetc.sample.doma.base.web.controller.IntegerValueEnumConverterFactory;
import com.bigtreetc.sample.doma.base.web.controller.StringValueEnumConverterFactory;
import com.bigtreetc.sample.doma.base.web.filter.ClearMDCFilter;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class ApiConfig implements WebMvcConfigurer {

  @Autowired
  public void initUtils(MessageSource messageSource) {
    MessageUtils.init(messageSource);
  }

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.APPLICATION_JSON);
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverterFactory(new StringValueEnumConverterFactory());
    registry.addConverterFactory(new IntegerValueEnumConverterFactory());
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
    registry.addInterceptor(elapsedMillisLoggingInterceptor());
    registry.addInterceptor(setAuditInfoInterceptor());
  }

  @Bean
  public FilterRegistrationBean<ClearMDCFilter> clearMDCFilter() {
    val filter = new ClearMDCFilter();
    val bean = new FilterRegistrationBean<>(filter);
    bean.setOrder(Ordered.LOWEST_PRECEDENCE);
    return bean;
  }

  @Bean
  public ModelMapper modelMapper() {
    val modelMapper = new ModelMapper();
    val configuration = modelMapper.getConfiguration();
    configuration.setMatchingStrategy(MatchingStrategies.STRICT); // 厳格にマッピングする
    return modelMapper;
  }

  @Bean
  public LocaleResolver localeResolver() {
    // ヘッダーで言語を指定する
    return new AcceptHeaderLocaleResolver();
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    // langパラメータでロケールを切り替える
    val interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");
    return interceptor;
  }

  @Bean
  public LocalValidatorFactoryBean beanValidator(MessageSource messageSource) {
    val bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }

  @Bean
  public ElapsedMillisLoggingInterceptor elapsedMillisLoggingInterceptor() {
    return new ElapsedMillisLoggingInterceptor();
  }

  @Bean
  public SetAuditInfoInterceptor setAuditInfoInterceptor() {
    // システム制御項目を保存してDB保存時に利用する
    return new SetAuditInfoInterceptor();
  }
}
