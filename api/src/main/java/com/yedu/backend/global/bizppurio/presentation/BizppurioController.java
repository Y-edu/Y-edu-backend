package com.yedu.backend.global.bizppurio.presentation;

import com.yedu.backend.global.bizppurio.handler.BizppurioCheckStep;
import com.yedu.backend.global.bizppurio.dto.MessageStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bizppurio")
public class BizppurioController {
    private final BizppurioCheckStep bizppurioCheckStep;

    @PostMapping("/result/webhook")
    public void resultWebHook(@RequestBody MessageStatusRequest request) {
        bizppurioCheckStep.checkByWebHook(request);
    }
}
