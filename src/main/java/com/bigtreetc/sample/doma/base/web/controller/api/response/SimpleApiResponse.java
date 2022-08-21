package com.bigtreetc.sample.doma.base.web.controller.api.response;

public interface SimpleApiResponse extends ApiResponse {

  Object getData();

  void setData(Object data);

  default SimpleApiResponse success(Object data) {
    this.success();
    this.setData(data);
    return this;
  }
}
