package com.alysonsantos.aspect.repository;

import com.alysonsantos.aspect.database.ExceptionFunction;
import com.alysonsantos.aspect.database.connection.SQLDatabase;
import com.alysonsantos.aspect.database.repository.AsyncRepository;
import com.alysonsantos.aspect.database.repository.Repository;
import com.alysonsantos.aspect.models.Account;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AccountRepository implements Repository<UUID, Account> {

    private final SQLDatabase database;
    private final Gson gson;

    @Override
    public ExceptionFunction<ResultSet, Account> getDeserializer() {
        return resultSet -> {
            return new Account(
                    UUID.fromString(resultSet.getString("uuid")),
                    resultSet.getString("name"),
                    resultSet.getDouble("balance"),
                    resultSet.getDouble("tokens"),
                    resultSet.getDouble("cash")
            );
        };
    }

    @Override
    public Collection<Account> getAll() {
        return database.queryCollection("SELECT * FROM economy ORDER BY `balance` DESC LIMIT 10", getDeserializer()).printAndReturn();
    }

    @Override
    public Account find(UUID uuid) {
        return database.query("SELECT * FROM economy WHERE uuid=?", getDeserializer(), uuid.toString()).getResponse();
    }

    @Override
    public int insert(UUID uuid, Account account) {
        return database.update("INSERT INTO economy (uuid, name, balance, tokens, cash) VALUES (?, ?, ?, ?, ?)",
                uuid.toString(),
                account.getName(),
                account.getBalance(),
                account.getTokens(),
                account.getCash()
        ).printAndReturn();
    }

    @Override
    public int update(UUID uuid, Account account) {
        return database.update("UPDATE economy SET balance=?, tokens=?, cash=? WHERE uuid=?",
                account.getBalance(),
                account.getTokens(),
                account.getCash(),
                uuid.toString()
        ).printAndReturn();
    }

    @Override
    public int delete(UUID uuid) {
        return database.update("DELETE FROM economy WHERE uuid=?", uuid.toString()).printAndReturn();
    }

    @Override
    public AsyncRepository<UUID, Account> async() {
        return new AsyncRepository<>(this);
    }
}
