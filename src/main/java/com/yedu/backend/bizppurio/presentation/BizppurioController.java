package com.yedu.backend.bizppurio.presentation;

import com.yedu.backend.bizppurio.application.dto.req.MessageStatusRequest;
import com.yedu.backend.bizppurio.application.usecase.BizppurioCheckStep;
import com.yedu.backend.bizppurio.application.usecase.BizppurioSend;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bizppurio")
public class BizppurioController {
    private final BizppurioSend bizppurioSend;
    private final BizppurioCheckStep bizppurioCheckStep;

    @PostMapping("/result/webhook")
    public void resultWebHook(@RequestBody MessageStatusRequest request) {
        bizppurioSend.checkByWebHook(request);
    }
}
