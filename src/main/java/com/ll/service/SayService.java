package com.ll.service;

import com.ll.simpleDb.Article;
import com.ll.simpleDb.SimpleDb;
import com.ll.simpleDb.Sql;

import java.util.List;
import java.util.Scanner;

public class SayService {
    private final SimpleDb simpleDb;
    Scanner scanner;

    public SayService(SimpleDb simpleDb){
        this.simpleDb = simpleDb;
        this.scanner = new Scanner(System.in);
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

    public long updateSay(Long inputId, String inputWriter, String inputContent){
        Sql sql = simpleDb.genSql();
        sql.append("update article set")
                .append("title = ?", inputWriter)
                .append(",body = ?", inputContent)
                .append("where id = ?", inputId);
        return sql.update();
    }
    public Article selectOne(Long inputId){
        if(inputId==-1){
            return null;
        }
        Sql sql = simpleDb.genSql();
        sql.append("select * from article")
                .append("where id = ?", inputId);
        return sql.selectRow(Article.class);
    }

    public long deleteSay(Long inputId){
        if(inputId==-1){
            System.out.println("삭제할 명언의 번호를 다시 입력해주세요");
            return -1;
        }
        Sql sql = simpleDb.genSql();
        sql.append("delete from article")
                .append("where id = ?",inputId);
        return sql.delete();
    }

}
