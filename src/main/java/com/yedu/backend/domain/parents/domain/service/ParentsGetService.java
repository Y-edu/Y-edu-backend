package com.yedu.backend.domain.parents.domain.service;

import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.parents.domain.repository.ParentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParentsGetService {
    private final ParentsRepository parentsRepository;

    public Optional<Parents> optionalParentsByPhoneNumber(String phoneNumber) {
        return parentsRepository.findByPhoneNumber(phoneNumber);
    }
}
