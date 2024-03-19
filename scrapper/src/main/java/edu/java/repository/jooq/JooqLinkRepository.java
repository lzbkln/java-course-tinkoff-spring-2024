package edu.java.repository.jooq;

import edu.java.repository.LinkRepository;
import edu.java.repository.entity.Link;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.domain.jooq.tables.Links.LINKS;

@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;

    @Override
    @Transactional
    public void save(Link link) {
        dslContext.insertInto(LINKS)
            .values(link.getUrl(), link.getLastUpdatedAt())
            .execute();
    }

    @Override
    @Transactional
    public Link findById(Long id) {
        return dslContext.select(LINKS.fields())
            .from(LINKS)
            .where(LINKS.ID.eq(id))
            .fetchOneInto(Link.class);
    }

    @Override
    @Transactional
    public Link findByUrl(String url) {
        return dslContext.select(LINKS.fields())
            .from(LINKS)
            .where(LINKS.URL.eq(url))
            .fetchOneInto(Link.class);
    }

    @Override
    @Transactional
    public boolean findByUrlBool(String url) {
        return dslContext.fetchExists(LINKS, LINKS.URL.eq(url));
    }

    @Override
    @Transactional
    public void removeById(Long id) {
        dslContext.deleteFrom(LINKS)
            .where(LINKS.ID.eq(id))
            .execute();
    }

    @Override
    @Transactional
    public void updateLink(Link link) {
        dslContext.update(LINKS)
            .set(LINKS.URL, link.getUrl())
            .set(LINKS.LAST_UPDATED_AT, link.getLastUpdatedAt())
            .where(LINKS.ID.eq(link.getId()))
            .execute();
    }

    @Override
    @Transactional
    @SuppressWarnings("MagicNumber")
    public List<Link> findLinksToUpdate() {
        return dslContext.select(LINKS.fields())
            .from(LINKS)
            .where(LINKS.LAST_UPDATED_AT.lt(OffsetDateTime.now().minusMinutes(30)))
            .fetchInto(Link.class);
    }
}
