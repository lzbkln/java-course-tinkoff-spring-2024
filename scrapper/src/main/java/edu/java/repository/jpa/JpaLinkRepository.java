package edu.java.repository.jpa;

import edu.java.repository.jpa.entity.JpaLink;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<JpaLink, Long> {
    Optional<JpaLink> findByUrl(String url);

    boolean existsByUrl(String url);

    @Modifying
    @Query("delete from JpaLink l where l.id = :id")
    void deleteById(Long id);

    List<JpaLink> findByLastUpdatedAtBefore(OffsetDateTime offsetDateTime);
}
