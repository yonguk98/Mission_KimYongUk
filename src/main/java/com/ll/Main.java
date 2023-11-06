package com.ll;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int sequence = 1;
        Map<Integer,Say> map = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("===명언 앱===");
        while(true){
            System.out.print("명령) ");
            String cmd = scanner.next();

            if(cmd.contains("종료")){ // 1단계
                System.exit(0);
            }
            if(cmd.contains("등록")){ // 2단계
                System.out.print("명언:");
                String content = scanner.next();
                System.out.print("작가");
                String writer = scanner.next();
                System.out.println(sequence + "번 명언이 등록되었습니다."); // 3단계

                map.put(sequence,new Say(sequence,content,writer));
                sequence++; // 4단계
            }
            if(cmd.contains("목록")){ // 5단계
                for (Say say : map.values()) {
                    System.out.println(say.getId()+" / "+say.getWriter()+" / "+say.getContent());
                }
            }
            if(cmd.contains("삭제")){ // 6,7 단계
                String[] cmdBits = cmd.split("\\?");
                Integer id = Integer.parseInt(cmdBits[1].split("id=",-1)[1].trim());

                if(!id.equals(null)){
                    if(map.containsKey(id)) {
                        map.remove(id);
                        System.out.println(id + "번 명언이 삭제되었습니다.");
                    }else{
                        System.out.println(id + "번 명언은 존재하지 않습니다.");
                    }
                }
            }

        }
    }
}