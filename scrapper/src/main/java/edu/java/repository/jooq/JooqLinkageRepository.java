package edu.java.repository.jooq;

import edu.java.repository.LinkageRepository;
import edu.java.repository.entity.Linkage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.transaction.annotation.Transactional;
import static edu.java.domain.jooq.tables.Linkage.LINKAGE;

@RequiredArgsConstructor
public class JooqLinkageRepository implements LinkageRepository {
    private final DSLContext dslContext;

    @Override
    @Transactional
    public void save(Linkage linkage) {
        dslContext.insertInto(LINKAGE)
            .values(linkage.getChatId(), linkage.getLinkId())
            .execute();
    }

    @Override
    @Transactional
    public List<Linkage> findByChatId(Long chatId) {
        return dslContext.selectFrom(LINKAGE)
            .where(LINKAGE.CHAT_ID.eq(chatId))
            .fetchInto(Linkage.class);
    }

    @Override
    @Transactional
    public List<Linkage> findByLinkId(Long linkId) {
        return dslContext.selectFrom(LINKAGE)
            .where(LINKAGE.LINK_ID.eq(linkId))
            .fetchInto(Linkage.class);
    }

    @Override
    @Transactional
    public void removeByChatIdAndLinkId(Long chatId, Long linkId) {
        dslContext.deleteFrom(LINKAGE)
            .where(LINKAGE.CHAT_ID.eq(chatId).and(LINKAGE.LINK_ID.eq(linkId)))
            .execute();
    }

    @Override
    @Transactional
    public Integer countByLinkId(Long linkId) {
        return dslContext.selectCount()
            .from(LINKAGE)
            .where(LINKAGE.LINK_ID.eq(linkId))
            .fetchOne(0, Integer.class);
    }
}
