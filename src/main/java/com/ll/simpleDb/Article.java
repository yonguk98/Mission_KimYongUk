package com.ll.simpleDb;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Article {
    private long id;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String title;
    private String body;
    private boolean isBlind;

    public Article(){
    }
    public Article(Map<String,Object> map){
        setId((long)map.get("id"));
        setCreatedDate((LocalDateTime) map.get("createdDate"));
        setModifiedDate((LocalDateTime) map.get("modifiedDate"));
        setTitle((String) map.get("title"));
        setBody((String) map.get("body"));
        setBlind((Boolean) map.get("isBlind"));
    }
}
