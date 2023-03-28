package com.bigtreetc.sample.doma.controller.holidays;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HolidayRequest {

  private static final long serialVersionUID = -1L;

  Long id;

  // 祝日名
  @NotEmpty String holidayName;

  // 日付
  LocalDate holidayDate;

  // 改定番号
  Integer version;
}
