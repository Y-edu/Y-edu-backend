package com.yedu.api.domain.matching.application.dto.req;

import java.time.LocalDate;
import java.time.LocalTime;

public record ChangeSessionDateRequest(LocalDate sessionDate, LocalTime start) {}
