package com.unknown.dbsync;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDBSync extends AbstractDBSync {

	
	public MySQLDBSync(String target) {
		super(target);
	}

	protected String getQuery(){
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ");
		if(isEmpty(getTable())){
			throw new IllegalArgumentException("Table miss");
		}
		sqlBuilder.append(getTable());
		if(!isEmpty(getExtern())){
			sqlBuilder.append(" WHERE ").append(getExtern());
		}
		if(!isEmpty(getOrderColumn())){
			sqlBuilder.append(" ORDER BY ").append(getOrderColumn());
		}
		sqlBuilder.append(String.format(" LIMIT %d, %d", getStartIndex(), getSyncSize()));
		return sqlBuilder.toString();
	}
	
	@Override
	protected void save(final String[] columns,final Object[] values, final int size) {
		if(size < 1){
			return ;
		}
		final StringBuilder insert = new StringBuilder("INSERT INTO ").append(getTable());
		insert.append("(");
		for(int i = 0; i < columns.length ; i++){
			insert.append('`').append(columns[i]).append("`,");
		}
		insert.delete(insert.length() -1, insert.length()).append(") VALUES");
		for(int i = 0; i < size; i++){
			insert.append("(");
			for(int j = 0; j < columns.length; j++){
				insert.append("?,");
			}
			insert.delete(insert.length() -1, insert.length()).append("),");
		}
		insert.delete(insert.length() -1, insert.length());
		invoke(new DBOperation() {
			@Override
			public void doDB(Connection conn) throws SQLException {
				PreparedStatement prepInsert = conn.prepareStatement(insert.toString());
				int total = size * columns.length;
				for(int i = 0; i < total; i++){
					prepInsert.setObject(i + 1, values[i]);
				}
				prepInsert.executeUpdate();
			}
		});
	}

	@Override
	public void copyBatch(final AbstractDBSync dbSync) {
		invoke(new DBOperation() {
			@Override
			public void doDB(Connection conn) throws SQLException {
				String[] columns = null;
				Object[] values = null;
				int getCounts = 0;
				do{
					getCounts = 0;
					ResultSet selectResult = conn.createStatement().executeQuery(getQuery());
					if(columns == null){
						columns = getColumns(selectResult);
						values = new Object[columns.length * getSyncSize()];
					}
					while(selectResult.next()){
						for(int i = 0; i < columns.length; i++){
							values[getCounts * columns.length + i] = selectResult.getObject(i + 1);
						}
						getCounts++;
					}
					if(getCounts > 0){
						dbSync.save(columns, values, getCounts);
						setStartIndex(getStartIndex() + getSyncSize());
					}
					selectResult.close();
				}while(getCounts == getSyncSize());
			}
		});
	}

	@Override
	public void clear(){
		invoke(new DBOperation() {
			@Override
			public void doDB(Connection conn) throws SQLException {
				int deleteSize = 1000;
				String delete = String.format("DELETE FROM %s limit %d", getTable(), deleteSize);
				Statement stament = conn.createStatement();
				while(stament.executeUpdate(delete) > 0){}
			}
		});
	}
}
