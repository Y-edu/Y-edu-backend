package com.yedu.bizppurio.support.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bizppurio")
public record BizppurioProperties(
    String id,
    String pw,
    String number,
    String yeduTutor,
    String yeduApply,
    String yeduMatching,
    String yeduOfficial,
    String landingUrl) {

  public String getKey(BizpurrioTemplate template) {
    return switch (template.getSenderProfile()) {
      case YEDU_APPLY -> yeduApply;
      case YEDU_TUTOR -> yeduTutor;
      case YEDU_MATCHING -> yeduMatching;
      case YEDU_OFFICIAL -> yeduOfficial;
    };
  }
}
