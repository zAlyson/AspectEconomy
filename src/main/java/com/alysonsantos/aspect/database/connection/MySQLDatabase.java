package com.alysonsantos.aspect.database.connection;

import com.alysonsantos.aspect.database.ExceptionFunction;
import com.alysonsantos.aspect.database.SQLResponse;
import com.alysonsantos.aspect.database.ServerStatement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.lucko.helper.sql.Sql;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class MySQLDatabase implements SQLDatabase {

    private Sql sql;
    private boolean active;

    public void closeConnection() {
        if (!active) return;

        try {
            getConnection().close();
            active = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public <R> SQLResponse<R> query(String query, ExceptionFunction<ResultSet, R> function, Object... objects) {
        if (!active) return new SQLResponse<>(new SQLException("Connection is closed"));
        try {
            try (Connection connection = this.getConnection()) {
                try (ServerStatement statement = new ServerStatement(connection.prepareStatement(query))) {
                    statement.setObjects(objects);
                    ResultSet set = statement.executeQuery();
                    if (!set.next())
                        return new SQLResponse<>(new SQLException("ResultSet is empty"));

                    return new SQLResponse<>(function.apply(set));
                }
            }
        } catch (SQLException e) {
            return new SQLResponse<>(e);
        }
    }


    @SneakyThrows
    @Override
    public <R> SQLResponse<Collection<R>> queryCollection(String query, ExceptionFunction<ResultSet, R> function, Objects... objects) {
        if (!active) return new SQLResponse<>(new SQLException("Connection is closed"));

        try {
            try (Connection connection = this.getConnection()) {
                try (ServerStatement statement = new ServerStatement(connection.prepareStatement(query))) {

                    if (objects != null) statement.setObjects(objects);

                    ResultSet set = statement.executeQuery();

                    List<R> collection = new LinkedList<>();
                    while (set.next()) {
                        collection.add(function.apply(set));
                    }

                    return new SQLResponse<>(collection);
                }
            }
        } catch (SQLException e) {
            return new SQLResponse<>(e);
        }
    }

    @SneakyThrows
    public SQLResponse<Integer> update(String update, Object... objects) {
        if (!active) return new SQLResponse<>(new SQLException("Connection is closed"));

        try {
            try (Connection connection = this.getConnection()) {
                try (ServerStatement statement = new ServerStatement(connection.prepareStatement(update))) {
                    statement.setObjects(objects);

                    return new SQLResponse<>(statement.executeUpdate());
                }
            }
        } catch (SQLException e) {
            return new SQLResponse<>(e);
        }
    }


    @Override
    public Connection getConnection() throws SQLException {
        if (this.sql.getConnection() == null) {
            throw new SQLException("Unable to get a connection from the pool. (hikari is null)");
        }
        Connection connection = this.sql.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to get a connection from the pool. (getConnection returned null)");
        }
        return connection;
    }
}
