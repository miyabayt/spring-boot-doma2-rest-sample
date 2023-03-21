package com.bigtreetc.sample.doma.controller.staffs;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchStaffRequest implements Serializable {

  private static final long serialVersionUID = -1L;

  Long id;

  String firstName;

  String lastName;

  String email;
}
