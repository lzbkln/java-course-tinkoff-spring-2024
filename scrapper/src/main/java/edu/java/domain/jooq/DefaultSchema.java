/*
 * This file is generated by jOOQ.
 */

package edu.java.domain.jooq;

import edu.java.domain.jooq.tables.Chats;
import edu.java.domain.jooq.tables.GithubBranches;
import edu.java.domain.jooq.tables.Linkage;
import edu.java.domain.jooq.tables.Links;
import edu.java.domain.jooq.tables.StackoverflowQuestion;
import java.util.Arrays;
import java.util.List;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>CHATS</code>.
     */
    public final Chats CHATS = Chats.CHATS;

    /**
     * The table <code>GITHUB_BRANCHES</code>.
     */
    public final GithubBranches GITHUB_BRANCHES = GithubBranches.GITHUB_BRANCHES;

    /**
     * The table <code>LINKAGE</code>.
     */
    public final Linkage LINKAGE = Linkage.LINKAGE;

    /**
     * The table <code>LINKS</code>.
     */
    public final Links LINKS = Links.LINKS;

    /**
     * The table <code>STACKOVERFLOW_QUESTION</code>.
     */
    public final StackoverflowQuestion STACKOVERFLOW_QUESTION = StackoverflowQuestion.STACKOVERFLOW_QUESTION;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }

    @Override
    @NotNull
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    @NotNull
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Chats.CHATS,
            GithubBranches.GITHUB_BRANCHES,
            Linkage.LINKAGE,
            Links.LINKS,
            StackoverflowQuestion.STACKOVERFLOW_QUESTION
        );
    }
}
