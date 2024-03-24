package edu.java.repository.jooq;

import edu.java.repository.LinkageRepository;
import edu.java.repository.entity.Linkage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.tables.Linkage.LINKAGE;

@RequiredArgsConstructor
public class JooqLinkageRepository implements LinkageRepository {
    private final DSLContext dslContext;

    @Override
    public void save(Linkage linkage) {
        dslContext.insertInto(LINKAGE)
            .values(linkage.getChatId(), linkage.getLinkId())
            .execute();
    }

    @Override
    public List<Linkage> findByChatId(Long chatId) {
        return dslContext.selectFrom(LINKAGE)
            .where(LINKAGE.CHAT_ID.eq(chatId))
            .fetchInto(Linkage.class);
    }

    @Override
    public List<Linkage> findByLinkId(Long linkId) {
        return dslContext.selectFrom(LINKAGE)
            .where(LINKAGE.LINK_ID.eq(linkId))
            .fetchInto(Linkage.class);
    }

    @Override
    public void removeByChatIdAndLinkId(Long chatId, Long linkId) {
        dslContext.deleteFrom(LINKAGE)
            .where(LINKAGE.CHAT_ID.eq(chatId).and(LINKAGE.LINK_ID.eq(linkId)))
            .execute();
    }

    @Override
    public Integer countByLinkId(Long linkId) {
        return dslContext.selectCount()
            .from(LINKAGE)
            .where(LINKAGE.LINK_ID.eq(linkId))
            .fetchOne(0, Integer.class);
    }
}
