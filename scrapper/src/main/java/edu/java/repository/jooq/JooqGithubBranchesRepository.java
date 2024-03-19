package edu.java.repository.jooq;

import edu.java.repository.GithubBranchesRepository;
import edu.java.repository.entity.GithubBranches;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.domain.jooq.tables.GithubBranches.GITHUB_BRANCHES;

@RequiredArgsConstructor
public class JooqGithubBranchesRepository implements GithubBranchesRepository {
    private final DSLContext dslContext;

    @Override
    @Transactional
    public void save(GithubBranches githubBranches) {
        dslContext.insertInto(GITHUB_BRANCHES)
            .columns(GITHUB_BRANCHES.LINK_ID, GITHUB_BRANCHES.BRANCHES)
            .values(githubBranches.getLinkId(), githubBranches.getBranches().toArray(new String[0]))
            .execute();
    }

    @Override
    @Transactional
    public GithubBranches findByLinkId(Long linkId) {
        return dslContext.selectFrom(GITHUB_BRANCHES)
            .where(GITHUB_BRANCHES.LINK_ID.eq(linkId))
            .fetchOneInto(GithubBranches.class);
    }

    @Override
    @Transactional
    public void updateData(GithubBranches githubBranches) {
        dslContext.update(GITHUB_BRANCHES)
            .set(GITHUB_BRANCHES.BRANCHES, githubBranches.getBranches().toArray(new String[0]))
            .where(GITHUB_BRANCHES.LINK_ID.eq(githubBranches.getLinkId()))
            .execute();
    }

    @Override
    @Transactional
    public void removeByLinkId(Long linkId) {
        dslContext.deleteFrom(GITHUB_BRANCHES)
            .where(GITHUB_BRANCHES.LINK_ID.eq(linkId))
            .execute();
    }
}
