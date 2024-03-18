package edu.java.repository.jdbc.rowMappers;

import edu.java.repository.entity.GithubBranches;
import java.sql.ResultSet;
import java.util.Set;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

public class GithubBranchesRowMapper implements RowMapper<GithubBranches> {
    @SneakyThrows
    @Override
    public GithubBranches mapRow(ResultSet rs, int rowNum) {
        return new GithubBranches(
            rs.getLong("link_id"),
            (Set<String>) rs.getArray("branches")
        );
    }
}
