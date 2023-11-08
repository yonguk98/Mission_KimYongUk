package com.ll.controller;

import com.ll.model.Command;
import com.ll.service.SayService;
import com.ll.simpleDb.Article;

import java.util.Scanner;

public class CommandController {
    private Scanner scanner;
    private Request request;
    private SayService sayService;

    public CommandController(SayService sayService){
        this.scanner = new Scanner(System.in);
        this.request = new Request();
        this.sayService = sayService;
    }

    public void start(){
        System.out.print("명령) ");
        String input = scanner.next();

        Command cmd = request.findCmd(input);
        Long inputId = request.getIdParam(input);

        switch (cmd){
            case List -> sayService.showAll();
            case Add -> {
                System.out.print("작가: ");
                String writer = scanner.next();
                System.out.print("명언: ");
                String content = scanner.next();

                long id = sayService.insertSayToDb(writer,content);
                System.out.println(id + "번 명언이 등록되었습니다.");
            }
            case Update -> {
                Article data = sayService.selectOne(inputId);
                if(data.equals(null)){
                    System.out.println("수정할 명언의 번호를 입력해주세요.");
                    break;
                }

                System.out.println("명언(기존): " + data.getBody());
                System.out.print("명언: ");
                String inputContent = scanner.next();

                System.out.println("작가(기존): " + data.getTitle());
                System.out.print("작가: ");
                String inputWriter = scanner.next();

                long count = sayService.updateSay(inputId,inputWriter,inputContent);
                System.out.println(count + "개의 명언이 수정되었습니다."); // 갯수말고 번호로 수정필요
            }
            case Delete -> {
                long id = sayService.deleteSay(inputId);
                System.out.println(id + "번 명언이 삭제되었습니다.");
            }
            case Quit -> System.exit(0);
            case None -> System.out.println("명령어를 다시 입력해주세요.");
        }
    }
}
