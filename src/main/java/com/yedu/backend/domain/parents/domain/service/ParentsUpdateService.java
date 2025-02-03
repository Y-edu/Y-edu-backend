package com.yedu.backend.domain.parents.domain.service;

import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;
import org.springframework.stereotype.Service;

@Service
public class ParentsUpdateService {
    public void updateDistrict(Parents parents, District district, String dong) {
        parents.updateDistrict(district, dong);
    }

    public void updateCount(Parents parents) {
        parents.updateCount();
    }
}
