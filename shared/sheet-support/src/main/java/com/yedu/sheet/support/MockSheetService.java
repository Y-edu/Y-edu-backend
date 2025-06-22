package com.yedu.sheet.support;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile({"local"})
public class MockSheetService implements SheetApi {

  @Override
  public void write(List<List<Object>> values, String sheetName) {

  }
}
