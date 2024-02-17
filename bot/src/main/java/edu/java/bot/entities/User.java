package edu.java.bot.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    public final Long id;
    public List<String> links;

    public User(Long id) {
        this.id = id;
        this.links = new ArrayList<>();
    }
}
