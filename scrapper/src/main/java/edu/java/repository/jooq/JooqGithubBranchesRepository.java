package edu.java.repository.jooq;

import edu.java.repository.GithubBranchesRepository;
import edu.java.repository.entity.GithubBranches;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.tables.GithubBranches.GITHUB_BRANCHES;

@RequiredArgsConstructor
public class JooqGithubBranchesRepository implements GithubBranchesRepository {
    private final DSLContext dslContext;

    @Override
    public void save(GithubBranches githubBranches) {
        dslContext.insertInto(GITHUB_BRANCHES, GITHUB_BRANCHES.LINK_ID, GITHUB_BRANCHES.BRANCHES)
            .values(githubBranches.getLinkId(), githubBranches.getBranches().toArray(new String[0]))
            .execute();
    }

    @Override
    public GithubBranches getByLinkId(Long linkId) {
        return dslContext.selectFrom(GITHUB_BRANCHES)
            .where(GITHUB_BRANCHES.LINK_ID.eq(linkId))
            .fetchOneInto(GithubBranches.class);
    }

    @Override
    public void updateData(GithubBranches githubBranches) {
        dslContext.update(GITHUB_BRANCHES)
            .set(GITHUB_BRANCHES.BRANCHES, githubBranches.getBranches().toArray(new String[0]))
            .where(GITHUB_BRANCHES.LINK_ID.eq(githubBranches.getLinkId()))
            .execute();
    }

    @Override
    public void removeByLinkId(Long linkId) {
        dslContext.deleteFrom(GITHUB_BRANCHES)
            .where(GITHUB_BRANCHES.LINK_ID.eq(linkId))
            .execute();
    }
}
