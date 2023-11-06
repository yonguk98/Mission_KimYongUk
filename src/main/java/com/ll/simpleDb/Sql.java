package com.ll.simpleDb;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Sql {
    private SimpleDb simpleDb;
    private StringBuilder sb;
    private Connection connection;
    private List<Object> objectList;

    public Sql(SimpleDb simpleDb) {
        this.simpleDb = simpleDb;
        this.sb = new StringBuilder();
        try {
            this.connection = simpleDb.connection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.objectList = new ArrayList<>();

    }

//    public Sql append(String sql){
//        sb.append(sql + " ");
//        return this;
//    }

    public Sql append(String sql, Object... objects) {
        sb.append(sql + " ");
        for (Object object : objects) {
            objectList.add(object);
        }
        return this;
    }

    public Sql appendIn(String sql, List<Long> list){
        sb.append(sql + " ");
        objectList.add(list);
        return this;
    }

    public long insert(){
        long id = 0L;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());
            for (int i = 0; i < objectList.size(); i++) {
                preparedStatement.setObject(i+1,objectList.get(i));
            }
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.executeQuery("select * from article");
            while(rs.next()){
//                System.out.println(rs.getInt(1) + "\t" + rs.getString(4) + " " + rs.getString(5));
                id = rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return id;
    }

//    public long update(){
//
//    }
}
