package dev.conca.plsql;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.TerminalNode;

import dev.conca.plsql.antlr4.plsqlBaseListener;
import dev.conca.plsql.antlr4.plsqlParser.Delete_statementContext;
import dev.conca.plsql.antlr4.plsqlParser.Insert_statementContext;
import dev.conca.plsql.antlr4.plsqlParser.Select_statementContext;
import dev.conca.plsql.antlr4.plsqlParser.Tableview_nameContext;
import dev.conca.plsql.antlr4.plsqlParser.Update_statementContext;

public class CrudListener extends plsqlBaseListener {
	
	private enum CrudContext {NONE, CREATE, READ, UPDATE, DELETE};

	private CrudContext crudContext = CrudContext.NONE;

	private boolean inTableViewContext;
	
	private String tableName = "";
	
	private List<Table> createTables = new ArrayList<Table>();
	private List<Table> readTables = new ArrayList<Table>();
	private List<Table> updateTables = new ArrayList<Table>();
	private List<Table> deleteTables = new ArrayList<Table>();
	
	public List<Table> getCreateTables() {
		return createTables;
	}
	
	public List<Table> getReadTables() {
		return readTables;
	}

	public List<Table> getUpdateTables() {
		return updateTables;
	}

	public List<Table> getDeleteTables() {
		return deleteTables;
	}

	@Override
	public void enterSelect_statement(Select_statementContext ctx) {
		crudContext = CrudContext.READ;
	}
	
	@Override
	public void exitSelect_statement(Select_statementContext ctx) {
		crudContext = CrudContext.NONE;
	}

	@Override
	public void enterInsert_statement(Insert_statementContext ctx) {
		crudContext = CrudContext.CREATE;
	}
	@Override
	public void exitInsert_statement(Insert_statementContext ctx) {
		crudContext = CrudContext.NONE;
	}
	
	@Override
	public void enterUpdate_statement(Update_statementContext ctx) {
		crudContext = CrudContext.UPDATE;
	}
	@Override
	public void exitUpdate_statement(Update_statementContext ctx) {
		crudContext = CrudContext.NONE;
	}
	
	@Override
	public void enterDelete_statement(Delete_statementContext ctx) {
		crudContext = CrudContext.DELETE;
	}
	@Override
	public void exitDelete_statement(Delete_statementContext ctx) {
		crudContext = CrudContext.NONE;
	}
	
	@Override
	public void enterTableview_name(Tableview_nameContext ctx) {
		inTableViewContext = true;
	}
	
	@Override
	public void exitTableview_name(Tableview_nameContext ctx) {
		inTableViewContext = false;
		switch (crudContext) {
		case CREATE:
			createTables.add(new Table(tableName));
			break;
		case READ:
			readTables.add(new Table(tableName));
			break;
		case UPDATE:
			updateTables.add(new Table(tableName));
			break;
		case DELETE:
			deleteTables.add(new Table(tableName));
			break;
		default:
			break;
		}
		tableName = "";
	}
	
	@Override
	public void visitTerminal(TerminalNode node) {
		if (inTableViewContext)
			tableName += node.getText();
	}

}
