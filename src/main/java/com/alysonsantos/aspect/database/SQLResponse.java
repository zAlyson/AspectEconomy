package com.alysonsantos.aspect.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SQLResponse<T> {

	@Getter
	private T response;
	@Getter
	private Throwable error;

	public SQLResponse(T response) {
		this.response = response;
	}

	public SQLResponse(Throwable error) {
		this.error = error;
	}

	public T getResponse(Class<T> clazz) {
		return clazz.cast(response);
	}

	public boolean isError() {
		return error != null;
	}

	public void printStackTrace() {
		if(isError()) error.printStackTrace();
	}

	public T printAndReturn() {
		printStackTrace();
		return getResponse();
	}

	public T printAndReturn(Class<T> clazz) {
		printStackTrace();
		return getResponse(clazz);
	}

}
