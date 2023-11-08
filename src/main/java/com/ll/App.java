package com.ll;

import com.ll.controller.CommandController;
import com.ll.service.SayService;
import com.ll.simpleDb.SimpleDb;

public class App {
    private SimpleDb simpleDb;
    private CommandController controller;
    private SayService service;

    public App(){
        this.simpleDb = new SimpleDb("localhost","root","yonguk1234","say_app");
        this.service = new SayService(this.simpleDb);
        this.controller = new CommandController(this.service);
    }

    public void run(){
        System.out.println("== 명언 앱 ==");
        while (true){
            controller.start();
        }
    }
}
