package com.ll.service;

import com.ll.simpleDb.Article;
import com.ll.simpleDb.SimpleDb;
import com.ll.simpleDb.Sql;

import java.util.List;
import java.util.Map;
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

    public void insertSayToDb(Long inputId){ // service 함수는 디비와 접근만하고 컨트롤러가 입력받도록 변경 필요?
        System.out.print("명언: ");
        String content = scanner.next();
        System.out.print("작가: ");
        String writer = scanner.next();

        Sql sql = simpleDb.genSql();
        sql.append("insert into article")
                .append("SET createdDate = NOW()")
                .append(", modifiedDate = NOW()")
                .append(", title = ?", writer)
                .append(", body = ?", content);
        long id = sql.insert();
        System.out.println(id + "번 명언이 등록되었습니다.");
    }

    public void updateSay(Long inputId){
        if(inputId==-1){
            System.out.println("수정할 명언의 번호를 입력해주세요.");
            return;
        }
        Sql sql = simpleDb.genSql();
        sql.append("select * from article")
                .append("where id = ?", inputId);
        Map<String , Object> oldData = sql.selectRow();
        System.out.println("명언(기존): " + oldData.get("body"));
        System.out.print("명언: ");
        String inputContent = scanner.next();

        System.out.println("작가(기존): " + oldData.get("title"));
        System.out.print("작가: ");
        String inputWriter = scanner.next();

        sql = simpleDb.genSql();
        sql.append("update article set")
                .append("title = ?", inputWriter)
                .append(",body = ?", inputContent)
                .append("where id = ?", inputId);
        long count = sql.update();
        System.out.println(count + "개의 명언이 수정되었습니다.");
    }

    public void deleteSay(Long inputId){
        if(inputId==-1){
            System.out.println("삭제할 명언의 번호를 다시 입력해주세요");
            return;
        }
        Sql sql = simpleDb.genSql();
        sql.append("delete from article")
                .append("where id = ?",inputId);
        long id = sql.delete();
        System.out.println(id + "번 명언이 삭제되었습니다.");
    }

}
