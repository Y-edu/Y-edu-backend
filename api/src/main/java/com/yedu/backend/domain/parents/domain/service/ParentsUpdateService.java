package com.yedu.backend.domain.parents.domain.service;

import com.yedu.backend.domain.parents.domain.entity.Parents;
import org.springframework.stereotype.Service;

@Service
public class ParentsUpdateService {

  public void updateCount(Parents parents) {
    parents.updateCount();
  }
}
