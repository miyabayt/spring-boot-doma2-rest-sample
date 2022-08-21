package com.bigtreetc.sample.doma.base.web;

import com.bigtreetc.sample.doma.base.web.aop.ElapsedMillisLoggingInterceptor;
import com.bigtreetc.sample.doma.base.web.aop.SetAuditInfoInterceptor;
import com.bigtreetc.sample.doma.base.web.controller.IntegerValueEnumConverterFactory;
import com.bigtreetc.sample.doma.base.web.controller.StringValueEnumConverterFactory;
import com.bigtreetc.sample.doma.base.web.filter.ClearMDCFilter;
import java.util.List;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/** 基底アプリケーション設定 */
public abstract class BaseApiConfig implements WebMvcConfigurer {

  @Value("${application.cors.allow-credentials:false}")
  Boolean allowCredentials;

  @Value("#{'${application.cors.allowed-headers:*}'.split(',')}")
  List<String> allowedHeaders;

  @Value("#{'${application.cors.allowed-methods:*}'.split(',')}")
  List<String> allowedMethods;

  @Value("#{'${application.cors.allowed-origins:*}'.split(',')}")
  List<String> allowedOrigins;

  @Value("#{'${application.cors.exposed-headers:*}'.split(',')}")
  List<String> exposedHeaders;

  @Value("${application.cors.max-age:86400}")
  Long maxAge;

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer.defaultContentType(MediaType.APPLICATION_JSON);
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverterFactory(new StringValueEnumConverterFactory());
    registry.addConverterFactory(new IntegerValueEnumConverterFactory());
  }

  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilter() {
    val config = new CorsConfiguration();
    config.setAllowCredentials(allowCredentials);
    config.setAllowedHeaders(allowedHeaders);
    config.setAllowedOrigins(allowedOrigins);
    config.setAllowedMethods(allowedMethods);
    config.setExposedHeaders(exposedHeaders);
    config.setMaxAge(maxAge);

    val source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    val bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
  }

  @Bean
  public FilterRegistrationBean<ClearMDCFilter> clearMDCFilter() {
    val filter = new ClearMDCFilter();
    val bean = new FilterRegistrationBean<ClearMDCFilter>(filter);
    bean.setOrder(Ordered.LOWEST_PRECEDENCE);
    return bean;
  }

  @Bean
  public ModelMapper modelMapper() {
    // ObjectMappingのためのマッパー
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

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
    registry.addInterceptor(elapsedMillisLoggingInterceptor());
    registry.addInterceptor(setAuditInfoInterceptor());
  }
}
