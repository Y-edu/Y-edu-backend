package com.yedu.sheet.support;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile({"prod","dev"})
public class SheetService implements SheetApi{

  private final Sheets sheet;

  @Value("${google.sheet.id}")
  private String spreadsheetId;

  private final String sheetName = "data";

  @SneakyThrows
  public SheetService() {
    InputStream inputStream = SheetService.class.getClassLoader().getResourceAsStream("key.json");
    GoogleCredential credential =
        GoogleCredential.fromStream(inputStream)
            .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));

    this.sheet =
        new Sheets.Builder(credential.getTransport(), credential.getJsonFactory(), credential)
            .build();
  }

  @SneakyThrows
  public void write(List<List<Object>> values) {
    ValueRange body = new ValueRange().setValues(values);

    AppendValuesResponse appendValuesResponse =
        sheet
            .spreadsheets()
            .values()
            .append(spreadsheetId, sheetName, body)
            .setValueInputOption("RAW") // 또는 USER_ENTERED
            .setInsertDataOption("INSERT_ROWS")
            .setIncludeValuesInResponse(true)
            .execute();

    log.info(">>> append row : appendValuesResponse:{}", appendValuesResponse);
  }
}
