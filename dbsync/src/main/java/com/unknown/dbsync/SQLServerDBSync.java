package com.unknown.dbsync;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLServerDBSync extends AbstractDBSync {

	public SQLServerDBSync(String target) {
		super(target);
	}

	protected String getQuery() {
		StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ");
		if (isEmpty(getTable())) {
			throw new IllegalArgumentException("Table miss");
		}
		sqlBuilder.append(getTable());
		if (!isEmpty(getExtern())) {
			sqlBuilder.append(" WHERE ").append(getExtern());
		}
		if (!isEmpty(getOrderColumn())) {
			sqlBuilder.append(" ORDER BY ").append(getOrderColumn());
		}
		return sqlBuilder.toString();
	}

	@Override
	public void copyBatch(final AbstractDBSync dbSync) {
		invoke(new DBOperation() {
			@Override
			public void doDB(Connection conn) throws SQLException {
				PreparedStatement query = conn.prepareStatement(getQuery(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet selectResult = query.executeQuery();
				selectResult.setFetchSize(getSyncSize());
				String[] columns = getColumns(selectResult);
				Object[] values = new Object[columns.length * getSyncSize()];
				int getCounts;
				do {
					getCounts = 0;
					if (selectResult.absolute(getStartIndex() + 1)) {
						do {
							for (int i = 0; i < columns.length; i++) {
								values[getCounts * columns.length + i] = selectResult.getObject(i + 1);
							}
							getCounts++;
						} while (selectResult.next() && getCounts < getSyncSize());
					}
					if (getCounts > 0) {
						dbSync.save(columns, values, getCounts);
						setStartIndex(getStartIndex() + getSyncSize());
					}
				} while (getCounts == getSyncSize());
				selectResult.close();
				query.close();
			}
		});
	}

	@Override
	public void clear() {
		invoke(new DBOperation() {
			@Override
			public void doDB(Connection conn) throws SQLException {

			}
		});
	}

	@Override
	protected void save(String[] columns, Object[] values, int size) {

	}

}
