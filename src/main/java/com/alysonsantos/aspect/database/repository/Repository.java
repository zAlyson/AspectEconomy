package com.alysonsantos.aspect.database.repository;

import com.alysonsantos.aspect.database.ExceptionFunction;
import com.alysonsantos.aspect.database.connection.SQLDatabase;

import java.sql.ResultSet;
import java.util.Collection;

public interface Repository<ID, R> {

	SQLDatabase getDatabase();
	ExceptionFunction<ResultSet, R> getDeserializer();

	Collection<R> getAll();
	R find(ID id);
	int insert(ID id, R r);
	int update(ID id, R r);
	int delete(ID id);

	default AsyncRepository<ID, R> async() {
		return new AsyncRepository<>(this);
	}

}
