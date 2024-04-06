package edu.java.clients.sites.linkCheckers;

import edu.java.clients.sites.StackOverflowClient;
import edu.java.clients.sites.util.Utils;
import edu.java.dto.responses.StackOverflowResponseDTO;
import edu.java.repository.StackOverflowQuestionRepository;
import edu.java.repository.entity.Link;
import edu.java.repository.entity.StackOverflowQuestion;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class StackOverflowUpdateChecker extends UpdateChecker {
    private final StackOverflowClient stackOverflowClient;
    private final StackOverflowQuestionRepository stackOverflowQuestionRepository;
    private final Utils utils;

    public Mono<String> getUpdate(Link link) {
        String questionId = utils.extractPathForStackoverflow(link.getUrl());
        return stackOverflowClient.getQuestionsInfo(questionId)
            .mapNotNull(response -> {
                StackOverflowResponseDTO.Question question = response.items().getFirst();
                if (question.lastActivityDate().isAfter(link.getLastUpdatedAt())) {
                    int oldCountQuestions = stackOverflowQuestionRepository.findByLinkId(link.getId()).getAnswerCount();
                    if (oldCountQuestions < question.answerCount()) {
                        stackOverflowQuestionRepository.updateData(new StackOverflowQuestion(
                            link.getId(),
                            question.answerCount()
                        ));
                        return "Новый ответ на вопрос: %s".formatted(link.getUrl());
                    }
                }
                return null;
            });
    }

    public boolean isMatched(URI uri) {
        return uri.getHost().equals("stackoverflow.com");
    }
}
