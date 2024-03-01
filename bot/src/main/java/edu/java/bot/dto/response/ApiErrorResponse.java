package edu.java.bot.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiErrorResponse {
    String description;
    String code;
    String exceptionName;
    String exceptionMessage;
    List<String> stacktrace;
}
