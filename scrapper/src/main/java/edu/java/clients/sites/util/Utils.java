package edu.java.clients.sites.util;

import edu.java.clients.sites.GitHubClient;
import edu.java.clients.sites.StackOverflowClient;
import edu.java.dto.responses.GithubBranchResponseDTO;
import edu.java.dto.responses.StackOverflowResponseDTO;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class Utils {
    private final GitHubClient gitHubClient;
    private final StackOverflowClient stackOverflowClient;

    public Mono<GithubBranchResponseDTO[]> getBranches(String url) {
        return gitHubClient.getBranchesFromUserRepository(extractPathForGithub(url));

    }

    public int getAnswerCount(String url) {
        return stackOverflowClient.getQuestionsInfo(extractPathForStackoverflow(url))
            .mapNotNull(response -> {
                StackOverflowResponseDTO.Question question = response.items().getFirst();
                return question.answerCount();
            }).blockOptional().get();
    }

    public String extractPathForGithub(String url) {
        String regex = "https?://github.com/([^/]+)/([^/]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {
            return matcher.group(1) + "/" + matcher.group(2);
        }
        return null;
    }

    public String extractPathForStackoverflow(String url) {
        String regex = "https?://stackoverflow.com/questions/(\\d+)/.*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
}
