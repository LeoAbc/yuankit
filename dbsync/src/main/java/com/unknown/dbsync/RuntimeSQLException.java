package com.unknown.dbsync;

import java.sql.SQLException;

public class RuntimeSQLException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8687567515210823439L;

	public RuntimeSQLException(SQLException sqlEx) {
		super(sqlEx);
	}
}
