package edu.java.clients.sites.util;

import edu.java.clients.sites.GitHubClient;
import edu.java.dto.responses.GithubBranchResponseDTO;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Utils {
    private final GitHubClient gitHubClient;

    public Set<String> getBranches(String url) {
        GithubBranchResponseDTO[] branches =
            gitHubClient.getBranchesFromUserRepository(extractPathForGithub(url)).block();
        return convertBranchesToSet(branches);
    }

    public Set<String> convertBranchesToSet(GithubBranchResponseDTO[] branches) {
        return Arrays.stream(branches)
            .map(GithubBranchResponseDTO::name)
            .collect(Collectors.toSet());
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
