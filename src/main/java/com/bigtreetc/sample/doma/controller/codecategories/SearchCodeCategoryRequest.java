package com.bigtreetc.sample.doma.controller.codecategories;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchCodeCategoryRequest {

  private static final long serialVersionUID = -1L;

  Long id;

  String categoryCode;

  String categoryName;
}
