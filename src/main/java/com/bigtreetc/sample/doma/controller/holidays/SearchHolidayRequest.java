package com.bigtreetc.sample.doma.controller.holidays;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchHolidayRequest {

  private static final long serialVersionUID = -1L;

  Long id;

  // 祝日名
  String holidayName;

  // 日付
  LocalDate holidayDate;
}
