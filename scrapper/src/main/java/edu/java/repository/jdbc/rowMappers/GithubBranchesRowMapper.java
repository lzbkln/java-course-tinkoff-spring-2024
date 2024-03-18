package edu.java.repository.jdbc.rowMappers;

import edu.java.repository.entity.GithubBranches;
import java.sql.Array;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.RowMapper;

public class GithubBranchesRowMapper implements RowMapper<GithubBranches> {
    @SneakyThrows
    @Override
    public GithubBranches mapRow(ResultSet rs, int rowNum) {
        Array array = rs.getArray("branches");
        String[] branchesArray = (String[]) array.getArray();
        Set<String> branchesSet = new HashSet<>(Arrays.asList(branchesArray));

        return new GithubBranches(
            rs.getLong("link_id"),
            branchesSet
        );
    }
}
