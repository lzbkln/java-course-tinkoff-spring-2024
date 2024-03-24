package edu.java.repository.jooq;

import edu.java.repository.TelegramChatRepository;
import edu.java.repository.entity.TelegramChat;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import static edu.java.domain.jooq.tables.Chats.CHATS;

@RequiredArgsConstructor
public class JooqTelegramChatRepository implements TelegramChatRepository {
    private final DSLContext dslContext;

    @Override
    public void saveUser(TelegramChat user) {
        dslContext.insertInto(CHATS, CHATS.ID, CHATS.CREATED_AT)
            .values(user.getId(), user.getCreatedAt())
            .execute();
    }

    @Override
    public TelegramChat findById(Long id) {
        return dslContext.select(CHATS.fields())
            .from(CHATS)
            .where(CHATS.ID.eq(id))
            .fetchOneInto(TelegramChat.class);
    }

    @Override
    public void deleteById(Long id) {
        dslContext.deleteFrom(CHATS)
            .where(CHATS.ID.eq(id))
            .execute();
    }
}
