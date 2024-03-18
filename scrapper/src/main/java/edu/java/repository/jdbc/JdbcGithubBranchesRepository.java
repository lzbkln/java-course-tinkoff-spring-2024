package edu.java.repository.jdbc;

import edu.java.repository.GithubBranchesRepository;
import edu.java.repository.entity.GithubBranches;
import edu.java.repository.jdbc.rowMappers.GithubBranchesRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JdbcGithubBranchesRepository implements GithubBranchesRepository {
    private final JdbcClient jdbcClient;
    private static final RowMapper<GithubBranches> ROW_MAPPER = new GithubBranchesRowMapper();
    private static final String INSERT_SQL =
        "INSERT INTO github_branches (link_id, branches) VALUES (:link_id, :branches)";
    private static final String FIND_BY_ID_SQL = "SELECT * FROM github_branches WHERE link_id = :link_id";
    private static final String UPDATE_LINK_SQL =
        "UPDATE github_branches SET branches = :branches WHERE link_id = :link_id";
    private static final String REMOVE_BY_ID_SQL = "DELETE FROM github_branches WHERE link_id = :link_id";

    private static final String LINK_ID = "link_id";
    private static final String BRANCHES = "branches";

    @Override
    @Transactional
    public void save(GithubBranches githubBranches) {
        jdbcClient.sql(INSERT_SQL)
            .param(LINK_ID, githubBranches.getLinkId())
            .param(BRANCHES, githubBranches.getBranches().toArray(new String[0]))
            .update();
    }

    @Override
    @Transactional
    public GithubBranches findByLinkId(Long linkId) {
        return jdbcClient.sql(FIND_BY_ID_SQL)
            .param(LINK_ID, linkId)
            .query(ROW_MAPPER)
            .single();
    }

    @Override
    @Transactional
    public void updateData(GithubBranches githubBranches) {
        jdbcClient.sql(UPDATE_LINK_SQL)
            .param(LINK_ID, githubBranches.getLinkId())
            .param(BRANCHES, githubBranches.getBranches().toArray(new String[0]))
            .update();
    }

    @Override
    @Transactional
    public void removeByLinkId(Long linkId) {
        jdbcClient.sql(REMOVE_BY_ID_SQL)
            .param(LINK_ID, linkId)
            .update();
    }
}
