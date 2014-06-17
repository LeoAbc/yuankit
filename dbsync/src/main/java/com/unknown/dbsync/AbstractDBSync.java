package com.unknown.dbsync;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

public abstract class AbstractDBSync {

	private int syncSize = 5000;

	private int startIndex = 0;

	private String orderColumn;

	private String extern;
	
	private String table;
	
	private DataSource dbSource;
	
	public AbstractDBSync(String target){
		this.table = target;
	}
	
	public int getSyncSize() {
		return syncSize;
	}

	public void setSyncSize(int syncSize) {
		this.syncSize = syncSize;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getExtern() {
		return extern;
	}

	public void setExtern(String extern) {
		this.extern = extern;
	}
	
	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	public DataSource getDbSource() {
		return dbSource;
	}

	public void setDbSource(DataSource dbSource) {
		this.dbSource = dbSource;
	}
	
	protected boolean isEmpty(String str){
		return str == null || str.isEmpty();
	}

	protected void invoke(DBOperation dbOperator){
		Connection conn = null;
		try{
			conn = getDbSource().getConnection();
			dbOperator.doDB(conn);
		}catch(SQLException sqlEx){
			throw new RuntimeSQLException(sqlEx);
		}finally{
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					throw new RuntimeSQLException(e);
				}
			}
		}
	}
	
	protected String[] getColumns(ResultSet selectResult) throws SQLException{
		ResultSetMetaData metaData = selectResult.getMetaData();
		String[] columns = new String[metaData.getColumnCount()];
		for(int i = 0; i < columns.length; i++){
			columns[i] = metaData.getColumnName(i+1);
		}
		return columns;
	}
	
	public abstract void copyBatch(AbstractDBSync dbSync);
	
	public abstract void clear();
	
	protected abstract void save(String[] columns, Object[] values, int size);
	
	protected interface DBOperation{
		public void doDB(Connection conn) throws SQLException;
	}
}
