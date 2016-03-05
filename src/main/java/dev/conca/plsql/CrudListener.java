package dev.conca.plsql;

import org.antlr.v4.runtime.tree.TerminalNode;

import dev.conca.plsql.antlr4.plsqlBaseListener;
import dev.conca.plsql.antlr4.plsqlParser.Column_nameContext;
import dev.conca.plsql.antlr4.plsqlParser.Delete_statementContext;
import dev.conca.plsql.antlr4.plsqlParser.Insert_statementContext;
import dev.conca.plsql.antlr4.plsqlParser.Select_statementContext;
import dev.conca.plsql.antlr4.plsqlParser.Selected_elementContext;
import dev.conca.plsql.antlr4.plsqlParser.Tableview_nameContext;
import dev.conca.plsql.antlr4.plsqlParser.Update_statementContext;

public class CrudListener extends plsqlBaseListener {
	
	private enum CrudContext {NONE, CREATE, READ, UPDATE, DELETE};

	private CrudContext crudContext = CrudContext.NONE;

	private boolean inTableViewContext;
	private boolean inSelectedElementContext;
	private boolean inColumnNameContext;
	
	private String tableName = "";
	private String columnName = "";
	
	private Crud crud = new Crud();
	
	public Crud getCrud() {
		return crud;
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
			crud.addCreateTable(new Table(tableName));
			break;
		case READ:
			crud.addReadTable(new Table(tableName));
			break;
		case UPDATE:
			crud.addUpdateTable(new Table(tableName));
			break;
		case DELETE:
			crud.addDeleteTable(new Table(tableName));
			break;
		default:
			break;
		}
		tableName = "";
	}
	
	@Override
	public void enterSelected_element(Selected_elementContext ctx) {
		inSelectedElementContext = true;
	}
	
	@Override
	public void exitSelected_element(Selected_elementContext ctx) {
		inSelectedElementContext = false;
		switch (crudContext) {
		case CREATE:
			crud.addCreateColumn(new Column(columnName));
			break;
		case READ:
			crud.addReadColumn(new Column(columnName));
			break;
		case UPDATE:
			crud.addUpdateColumn(new Column(columnName));
			break;
		default:
			break;
		}
		columnName = "";
	}
	
	@Override
	public void enterColumn_name(Column_nameContext ctx) {
		inColumnNameContext = true;
	}
	
	@Override
	public void exitColumn_name(Column_nameContext ctx) {
		inColumnNameContext = false;
		switch (crudContext) {
		case CREATE:
			crud.addCreateColumn(new Column(columnName));
			break;
		case READ:
			crud.addReadColumn(new Column(columnName));
			break;
		case UPDATE:
			crud.addUpdateColumn(new Column(columnName));
			break;
		default:
			break;
		}
		columnName = "";
	}
	
	@Override
	public void visitTerminal(TerminalNode node) {
		if (inTableViewContext)
			tableName += node.getText();
		if (inSelectedElementContext || inColumnNameContext)
			columnName = node.getText();
	}

}
