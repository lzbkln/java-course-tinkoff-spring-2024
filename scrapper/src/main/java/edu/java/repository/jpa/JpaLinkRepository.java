package edu.java.repository.jpa;

import edu.java.repository.jpa.entity.JpaLink;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<JpaLink, Long> {
    Optional<JpaLink> findByUrl(String url);

    @Query("select case when count(l) > 0 then true else false end from JpaLink l where l.url = :url")
    boolean findByUrlBool(@Param("url") String url);

    void removeById(Long id);

    @Modifying
    @Query("update JpaLink link set link.lastUpdatedAt = :lastUpdatedAt where link.id = :linkId")
    void updateById(@Param("linkId") Long linkId, @Param("lastUpdatedAt") OffsetDateTime lastUpdatedAt);

    List<JpaLink> findByLastUpdatedAtBefore(OffsetDateTime offsetDateTime);
}
