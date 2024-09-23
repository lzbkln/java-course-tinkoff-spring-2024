package edu.java.repository.jpa;

import edu.java.repository.jpa.entity.JpaGithubBranches;
import edu.java.repository.jpa.entity.JpaLink;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaGithubBranchesRepository extends JpaRepository<JpaGithubBranches, Long> {
    @Modifying
    @Query("delete from JpaGithubBranches g where g.linkId = :link_id")
    void deleteByLinkId(@Param("link_id") JpaLink linkId);

    Optional<JpaGithubBranches> findByLinkId(JpaLink linkId);
}
