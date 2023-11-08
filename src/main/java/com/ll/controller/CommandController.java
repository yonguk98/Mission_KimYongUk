package com.ll.controller;

import com.ll.model.Command;
import com.ll.service.SayService;
import com.ll.model.Article;

import java.util.Scanner;

public class CommandController {

    private final Scanner scanner;
    private final Request request;
    private final SayService sayService;

    public CommandController(SayService sayService){
        this.scanner = new Scanner(System.in);
        this.request = new Request();
        this.sayService = sayService;
    }

    public void start(){
        System.out.print("명령) ");
        String input = scanner.nextLine();

        String[] inputString = input.split("\\?",2);
        Command cmd = request.findCmd(inputString[0]);

        switch (cmd){
            case List -> sayService.showAll();
            case Add -> {
                System.out.print("작가: ");
                String writer = scanner.nextLine();
                System.out.print("명언: ");
                String content = scanner.nextLine();

                long id = sayService.insertSayToDb(writer,content);
                System.out.println(id + "번 명언이 등록되었습니다.");
            }
            case Update -> {
                Long inputId = request.getIdParam(inputString[1]);
                if(inputId==-1){
                    System.out.println("수정할 명언의 번호를 입력해주세요.");
                    break;
                }

                Article data = sayService.selectOne(inputId);

                System.out.println("명언(기존): " + data.getBody());
                System.out.print("명언: ");
                String inputContent = scanner.nextLine();

                System.out.println("작가(기존): " + data.getTitle());
                System.out.print("작가: ");
                String inputWriter = scanner.nextLine();

                System.out.println(inputId
                        + (sayService.updateSay(inputId,inputWriter,inputContent) == null
                        ? "번 명언은 존재하지 않습니다."
                        : "번 명언이 수정되었습니다."));
            }
            case Delete -> {
                Long inputId = request.getIdParam(input);
                if(inputId==-1){
                    System.out.println("삭제할 명언의 번호를 다시 입력해주세요");
                    break;
                }
                System.out.println(inputId
                        + (sayService.deleteSay(inputId) == null
                        ? "번 명언은 존재하지 않습니다."
                        : "번 명언이 삭제되었습니다."));
            }
            case Quit -> System.exit(0);
            case None -> System.out.println("명령어를 다시 입력해주세요.");
        }
    }
}
