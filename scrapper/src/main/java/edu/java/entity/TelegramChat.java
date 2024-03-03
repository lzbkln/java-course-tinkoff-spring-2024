package edu.java.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TelegramChat {
    private Long id;
    private List<Link> links;
}
