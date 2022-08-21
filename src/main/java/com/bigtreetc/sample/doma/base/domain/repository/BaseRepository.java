package com.bigtreetc.sample.doma.base.domain.repository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseRepository {

  @Autowired protected ModelMapper modelMapper;
}
