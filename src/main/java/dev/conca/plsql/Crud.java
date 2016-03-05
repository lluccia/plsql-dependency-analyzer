package dev.conca.plsql;

import java.util.HashSet;
import java.util.Set;

public class Crud {

	private Set<Table> createTables = new HashSet<Table>();
	private Set<Table> readTables = new HashSet<Table>();
	private Set<Table> updateTables = new HashSet<Table>();
	private Set<Table> deleteTables = new HashSet<Table>();

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

}
