package com.alysonsantos.aspect.database;

import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
public class ServerStatement implements AutoCloseable {

	@Delegate
	private PreparedStatement instance;

	public void setObjects(Object... objects) throws SQLException {
		for (int i = 0; i < objects.length; i++) {
			setGeneric(i + 1, objects[i]);
		}
	}

	public void setUUID(int index, UUID id) throws SQLException {
		setString(index, id.toString());
	}

	public void setGeneric(int parameterIndex, Object parameterObj) throws SQLException {
		if (parameterObj == null) {
			setNull(parameterIndex, java.sql.Types.OTHER);
		} else {
			if (parameterObj instanceof Byte) {
				setInt(parameterIndex, ((Byte) parameterObj).intValue());
			} else if (parameterObj instanceof String) {
				setString(parameterIndex, (String) parameterObj);
			} else if (parameterObj instanceof BigDecimal) {
				setBigDecimal(parameterIndex, (BigDecimal) parameterObj);
			} else if (parameterObj instanceof Short) {
				setShort(parameterIndex, ((Short) parameterObj));
			} else if (parameterObj instanceof Integer) {
				setInt(parameterIndex, ((Integer) parameterObj));
			} else if (parameterObj instanceof Long) {
				setLong(parameterIndex, ((Long) parameterObj));
			} else if (parameterObj instanceof Float) {
				setFloat(parameterIndex, ((Float) parameterObj));
			} else if (parameterObj instanceof Double) {
				setDouble(parameterIndex, ((Double) parameterObj));
			} else if (parameterObj instanceof byte[]) {
				setBytes(parameterIndex, (byte[]) parameterObj);
			} else if (parameterObj instanceof java.sql.Date) {
				setDate(parameterIndex, (java.sql.Date) parameterObj);
			} else if (parameterObj instanceof Time) {
				setTime(parameterIndex, (Time) parameterObj);
			} else if (parameterObj instanceof Timestamp) {
				setTimestamp(parameterIndex, (Timestamp) parameterObj);
			} else if (parameterObj instanceof Boolean) {
				setBoolean(parameterIndex, ((Boolean) parameterObj));
			} else if (parameterObj instanceof InputStream) {
				setBinaryStream(parameterIndex, (InputStream) parameterObj, -1);
			} else if (parameterObj instanceof java.sql.Blob) {
				setBlob(parameterIndex, (java.sql.Blob) parameterObj);
			} else if (parameterObj instanceof java.sql.Clob) {
				setClob(parameterIndex, (java.sql.Clob) parameterObj);
			} else if (parameterObj instanceof java.util.Date) {
				setTimestamp(parameterIndex, new Timestamp(((java.util.Date) parameterObj).getTime()));
			} else if (parameterObj instanceof BigInteger) {
				setString(parameterIndex, parameterObj.toString());
			} else if (parameterObj instanceof UUID) {
				setUUID(parameterIndex, (UUID) parameterObj);
			}
		}
	}

}
