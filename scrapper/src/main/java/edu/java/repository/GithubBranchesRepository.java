package edu.java.repository;

import edu.java.repository.entity.GithubBranches;

public interface GithubBranchesRepository {
    void save(GithubBranches githubBranches);

    GithubBranches getByLinkId(Long linkId);

    void updateData(GithubBranches githubBranches);

    void removeByLinkId(Long linkId);

}
