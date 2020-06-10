package com.alysonsantos.aspect.database.connection;

import com.alysonsantos.aspect.database.ExceptionFunction;
import com.alysonsantos.aspect.database.SQLResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

public interface SQLDatabase {
	
	boolean isActive();
	
	Connection getConnection() throws SQLException;

	void closeConnection();
	
	<R> SQLResponse<R> query(String query, ExceptionFunction<ResultSet, R> function, Object... objects);
	<R> SQLResponse<Collection<R>> queryCollection(String query, ExceptionFunction<ResultSet, R> function, Objects... objects);

	SQLResponse<Integer> update(String update, Object... objects);
	
}