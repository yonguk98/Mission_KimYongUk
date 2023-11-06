package com.ll;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Say {
    private int id;
    private String content;
    private String writer;

    public Say(int id, String content, String writer) {
        this.id = id;
        this.content = content;
        this.writer = writer;
    }
    public Say(){}
}
