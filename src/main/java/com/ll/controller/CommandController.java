package com.ll.controller;

import com.ll.model.Command;
import com.ll.service.SayService;

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
            case Add -> sayService.insertSayToDb(inputId);
            case Update -> sayService.updateSay(inputId);
            case Delete -> sayService.deleteSay(inputId);
            case Quit -> System.exit(0);
            case None -> System.out.println("명령어를 다시 입력해주세요.");
        }
    }
}
