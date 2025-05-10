package com.yedu.common;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationEnvironment {

  private static final String DEV = "개발";
  private static final String MAIN = "메인";

  private final Environment environment;

  public String currentProfile(){
    return Arrays.asList(environment.getActiveProfiles()).contains("dev") ? DEV : MAIN;
  }

}
