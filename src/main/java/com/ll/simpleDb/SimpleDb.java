package com.ll.simpleDb;

import java.sql.*;

public class SimpleDb {

    private String addr;
    private String user;
    private String password;
    private String dbName;
    private String url;

    public SimpleDb(String addr, String user, String password, String dbName) {
        this.addr = addr;
        this.user = user;
        this.password = password;
        this.dbName = dbName;
        this.url = "jdbc:mysql://" + this.addr +":3306/" + this.dbName;
    }

    public void setDevMode(boolean b) {

    }

    public Connection connection() throws SQLException {
        return DriverManager.getConnection(url,user,password);
    }
    
    public Sql genSql(){
        return new Sql(this);
    }

    public void run(String s, Object... objects){
        Connection connection;
        try {
            connection = connection();
            PreparedStatement preparedStatement = connection.prepareStatement(s);
            for (int i = 0; i < objects.length; i++) {
                preparedStatement.setObject(i+1,objects[i]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
