package com.ll.controller;

import com.ll.model.Command;

public class Request {
    public Command findCmd(String input){
        for (Command value : Command.values()) {
            if(input.contains(value.getKr())){
                return value;
            }
        }
        return Command.None;
    }
    public Long getIdParam(String input){
        if(findCmd(input)== Command.Delete || findCmd(input) == Command.Update){
            int eqIndex = input.indexOf("=");
            if(eqIndex==-1){
                return -1L;
            }else{
                return Long.parseLong(input.substring(eqIndex+1,input.length()));
            }
        }
        return -1L;
    }

}
