package com.ll.simpleDb;


import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Sql {
    private SimpleDb simpleDb;
    private StringBuilder sb;
    private Connection connection;

    public Sql(SimpleDb simpleDb) {
        this.simpleDb = simpleDb;
        this.sb = new StringBuilder();
        try {
            this.connection = simpleDb.connection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Sql append(String sql, Object... objects) {
        sb.append(sql);

        return this;
    }

    public Sql appendIn(String sql, List<Long> list){
        Connection conn;
        try {
            conn = simpleDb.connection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setArray(1, (Array) list);
            preparedStatement.execute();

            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public long insert(){

    }
}
