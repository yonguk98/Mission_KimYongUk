package com.ll.service;

import com.ll.simpleDb.Article;
import com.ll.simpleDb.SimpleDb;
import com.ll.simpleDb.Sql;

import java.util.List;

public class SayService {
    private final SimpleDb simpleDb;

    public SayService(SimpleDb simpleDb){
        this.simpleDb = simpleDb;
    }

    public void showAll(){
        Sql sql = simpleDb.genSql();
        sql.append("select * from article"); // table name 가변성 필요
        List<Article> queryResultList = sql.selectRows(Article.class);
        System.out.println("번호 / 작가 / 명언");
        System.out.println("---------------");
        for (Article data: queryResultList) {
            System.out.println(data.getId() + " / " + data.getTitle() + " / " + data.getBody());
        }
    }

    public long insertSayToDb(String writer, String content){
        Sql sql = simpleDb.genSql();
        sql.append("insert into article")
                .append("SET createdDate = NOW()")
                .append(", modifiedDate = NOW()")
                .append(", title = ?", writer)
                .append(", body = ?", content);
        return sql.insert();
    }

    public Article selectOne(Long inputId){
        Sql sql = simpleDb.genSql();
        sql.append("select * from article")
                .append("where id = ?", inputId);
        return sql.selectRow(Article.class);
    }

    public Long updateSay(Long inputId, String inputWriter, String inputContent){
        Sql sql = simpleDb.genSql();
        sql.append("update article set")
                .append("title = ?", inputWriter)
                .append(",body = ?", inputContent)
                .append("where id = ?", inputId);
        return checkIdExistOne(inputId)? sql.update() : null;
    }

    public Long deleteSay(Long inputId){
        Sql sql = simpleDb.genSql();
        sql.append("delete from article")
                .append("where id = ?",inputId);
        return checkIdExistOne(inputId)? sql.delete() : null;
    }

    public boolean checkIdExistOne(long inputId){
        Sql sql = simpleDb.genSql();
        sql.append("select count(*) from article")
                .append("where id = ?",inputId);
        return sql.selectLong()==1? true : false;
    }

}
