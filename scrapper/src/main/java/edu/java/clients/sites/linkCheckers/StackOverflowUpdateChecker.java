package edu.java.clients.sites.linkCheckers;

import edu.java.clients.sites.StackOverflowClient;
import edu.java.dto.responses.StackOverflowResponseDTO;
import edu.java.repository.entity.Link;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class StackOverflowUpdateChecker extends UpdateChecker {
    private final StackOverflowClient stackOverflowClient;

    public Mono<String> getUpdate(Link link) {
        String questionId = extractQuestionIdFromUrl(link.getUrl());
        return stackOverflowClient.getQuestionsInfo(questionId)
            .mapNotNull(response -> {
                StackOverflowResponseDTO.Question question = response.items().getFirst();
                if (question.lastActivityDate().isAfter(OffsetDateTime.from(link.getLastUpdatedAt()))) {
                    return "Обновление по ссылке %s".formatted(link.getUrl());
                }
                return null;
            });
    }

    private String extractQuestionIdFromUrl(String url) {
        String regex = "https?://stackoverflow.com/questions/(\\d+)/.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    public boolean isMatched(URI uri) {
        return uri.getHost().equals("https://stackoverflow.com/");
    }
}
