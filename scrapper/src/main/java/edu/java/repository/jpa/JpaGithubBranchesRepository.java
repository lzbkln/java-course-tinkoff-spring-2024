package edu.java.repository.jpa;

import edu.java.repository.jpa.entity.JpaGithubBranches;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGithubBranchesRepository extends JpaRepository<JpaGithubBranches, Long> {
    @Modifying
    @Query("delete from JpaGithubBranches g where g.linkId.id = :linkId")
    void deleteByLinkId(@Param("linkId") Long linkId);

    @Query("select g from JpaGithubBranches g where g.linkId.id = :linkId")
    JpaGithubBranches findByLinkId(@Param("linkId") Long linkId);

    @Modifying
    @Query("update JpaGithubBranches g set g.branches = :githubBranches where g.linkId.id = :linkId")
    void updateData(@Param("linkId") Long linkId, @Param("githubBranches") Set<String> githubBranches);
}
