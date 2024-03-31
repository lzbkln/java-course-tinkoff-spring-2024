package edu.java.repository.jooq;

import edu.java.repository.LinkRepository;
import edu.java.repository.entity.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.tables.Links.LINKS;

@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;

    @Override
    public void save(Link link) {
        dslContext.insertInto(LINKS, LINKS.URL, LINKS.LAST_UPDATED_AT)
            .values(link.getUrl(), link.getLastUpdatedAt())
            .execute();
    }

    @Override
    public Link getById(Long id) {
        return dslContext.select(LINKS.fields())
            .from(LINKS)
            .where(LINKS.ID.eq(id))
            .fetchOneInto(Link.class);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        Link link = dslContext.select(LINKS.fields())
            .from(LINKS)
            .where(LINKS.URL.eq(url))
            .fetchOneInto(Link.class);
        return Optional.ofNullable(link);
    }

    @Override
    public boolean findByUrlBool(String url) {
        return dslContext.fetchExists(LINKS, LINKS.URL.eq(url));
    }

    @Override
    public void removeById(Long id) {
        dslContext.deleteFrom(LINKS)
            .where(LINKS.ID.eq(id))
            .execute();
    }

    @Override
    public void updateLink(Link link) {
        dslContext.update(LINKS)
            .set(LINKS.URL, link.getUrl())
            .set(LINKS.LAST_UPDATED_AT, link.getLastUpdatedAt())
            .where(LINKS.ID.eq(link.getId()))
            .execute();
    }

    @Override
    public List<Link> findByLastUpdatedAtBefore(OffsetDateTime time) {
        return dslContext.select(LINKS.fields())
            .from(LINKS)
            .where(LINKS.LAST_UPDATED_AT.lt(time))
            .fetchInto(Link.class);
    }
}
