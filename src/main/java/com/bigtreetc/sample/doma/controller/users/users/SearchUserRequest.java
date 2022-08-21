package com.bigtreetc.sample.doma.controller.users.users;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchUserRequest {

  private static final long serialVersionUID = -1L;

  Long id;

  String firstName;

  String lastName;

  String password;
}
