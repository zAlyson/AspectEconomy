package com.alysonsantos.aspect.database.connection;

import com.alysonsantos.aspect.api.EconomyApi;
import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.database.ExceptionFunction;
import com.alysonsantos.aspect.database.SQLResponse;
import com.alysonsantos.aspect.database.ServerStatement;
import com.alysonsantos.aspect.models.Account;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class MySQLDatabase implements SQLDatabase {

    private static final int MAXIMUM_POOL_SIZE = (Runtime.getRuntime().availableProcessors() * 2) + 1;
    private static final int MINIMUM_IDLE = Math.min(MAXIMUM_POOL_SIZE, 10);

    private static final long MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30); // 30 Minutes
    private static final long CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(100); // 10 seconds
    private static final long LEAK_DETECTION_THRESHOLD = TimeUnit.SECONDS.toMillis(10); // 10 seconds

    private boolean active;
    private final HikariDataSource dataSource;

    public MySQLDatabase() {
        final HikariConfig hikari = new HikariConfig();

        String url = "jdbc:mysql://" + "189.127.164.93" + ":3306/"
                + "mc_1888" + "?autoReconnect=true";

        hikari.setJdbcUrl(url);
        hikari.setUsername("mc_1888");
        hikari.setPassword("f22e4fe789");

        hikari.setMinimumIdle(MINIMUM_IDLE);
        hikari.setMaximumPoolSize(MAXIMUM_POOL_SIZE);

        hikari.setMaxLifetime(MAX_LIFETIME);
        hikari.setConnectionTimeout(CONNECTION_TIMEOUT);
        hikari.setLeakDetectionThreshold(LEAK_DETECTION_THRESHOLD);

        this.dataSource = new HikariDataSource(hikari);
        this.active = true;
    }

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
        try (Connection connection = this.dataSource.getConnection()) {
            try (ServerStatement statement = new ServerStatement(connection.prepareStatement(query))) {
                statement.setObjects(objects);
                ResultSet set = statement.executeQuery();
                if (!set.next())
                    return new SQLResponse<>(new SQLException("ResultSet is empty"));

                return new SQLResponse<>(function.apply(set));
            }
        } catch (SQLException e) {
            return new SQLResponse<>(e);
        }
    }

    @SneakyThrows
    @Override
    public <R> SQLResponse<Collection<R>> queryCollection(String query, ExceptionFunction<ResultSet, R> function, Objects... objects) {
        if (!active) return new SQLResponse<>(new SQLException("Connection is closed"));

        try (Connection connection = this.dataSource.getConnection()) {
            try (ServerStatement statement = new ServerStatement(connection.prepareStatement(query))) {

                if (objects != null) statement.setObjects(objects);

                ResultSet set = statement.executeQuery();

                List<R> collection = new LinkedList<>();
                while (set.next()) {
                    collection.add(function.apply(set));
                }

                return new SQLResponse<>(collection);
            }
        } catch (SQLException e) {
            return new SQLResponse<>(e);
        }
    }

    @SneakyThrows
    public SQLResponse<Integer> update(String update, Object... objects) {
        if (!active) return new SQLResponse<>(new SQLException("Connection is closed"));

        try (Connection connection = this.dataSource.getConnection()) {
            try (ServerStatement statement = new ServerStatement(connection.prepareStatement(update))) {
                statement.setObjects(objects);

                return new SQLResponse<>(statement.executeUpdate());
            }
        } catch (SQLException e) {
            return new SQLResponse<>(e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (this.dataSource.getConnection() == null) {
            throw new SQLException("Unable to get a connection from the pool. (hikari is null)");
        }
        Connection connection = this.dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to get a connection from the pool. (getConnection returned null)");
        }
        return connection;
    }
}
