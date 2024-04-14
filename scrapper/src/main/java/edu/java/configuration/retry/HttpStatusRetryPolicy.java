package edu.java.configuration.retry;

import edu.java.dto.responses.ApiErrorResponse;
import edu.java.exception.ApiErrorResponseException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RequiredArgsConstructor
public class HttpStatusRetryPolicy {
    private final Set<HttpStatusCodes> retryOnStatuses;

    public boolean canRetry(Throwable throwable) {
        if (throwable instanceof ApiErrorResponseException) {
            ApiErrorResponse apiErrorResponse = ((ApiErrorResponseException) throwable).getApiErrorResponse();
            String code = apiErrorResponse.code();
            if (!code.isEmpty()) {
                int httpStatus = Integer.parseInt(apiErrorResponse.code().split(" ")[0]);
                var retryPolicyHttpStatusCode = HttpStatusCodes.getGroupByStatusCode(httpStatus);
                return retryOnStatuses.contains(retryPolicyHttpStatusCode);
            }
        } else if (throwable instanceof WebClientResponseException) {
            var httpStatus = ((WebClientResponseException) throwable).getStatusCode();
            var retryPolicyHttpStatusCode = HttpStatusCodes.getGroupByStatusCode(httpStatus.value());
            return retryOnStatuses.contains(retryPolicyHttpStatusCode);
        }
        return false;
    }

}
