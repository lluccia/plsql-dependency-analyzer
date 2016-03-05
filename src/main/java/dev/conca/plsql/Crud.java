package dev.conca.plsql;

import java.util.HashSet;
import java.util.Set;

public class Crud {

	private Set<Table> createTables = new HashSet<Table>();
	private Set<Table> readTables = new HashSet<Table>();
	private Set<Table> updateTables = new HashSet<Table>();
	private Set<Table> deleteTables = new HashSet<Table>();

	private Set<Column> createColumns = new HashSet<Column>();
	private Set<Column> readColumns = new HashSet<Column>();
	private Set<Column> updateColumns = new HashSet<Column>();

	
	public Set<Table> getCreateTables() {
		return createTables;
	}

	public Set<Table> getReadTables() {
		return readTables;
	}

	public Set<Table> getUpdateTables() {
		return updateTables;
	}

	public Set<Table> getDeleteTables() {
		return deleteTables;
	}
	
	
	public Set<Column> getCreateColumns() {
		return createColumns;
	}

	public Set<Column> getReadColumns() {
		return readColumns;
	}

	public Set<Column> getUpdateColumns() {
		return updateColumns;
	}

	public void addCreateTable(Table table) {
		createTables.add(table);
	}
	public void addReadTable(Table table) {
		readTables.add(table);
	}
	public void addUpdateTable(Table table) {
		updateTables.add(table);
	}
	public void addDeleteTable(Table table) {
		deleteTables.add(table);
	}

	public void addCreateColumn(Column column) {
		createColumns.add(column);
	}
	public void addReadColumn(Column column) {
		readColumns.add(column);
	}
	public void addUpdateColumn(Column column) {
		updateColumns.add(column);
	}
}
