package dev.conca.plsql;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.Test;

import dev.conca.plsql.antlr4.plsqlLexer;
import dev.conca.plsql.antlr4.plsqlParser;

public class CrudListenerTest {
	
	private ParseTree parseFile(String fileName) throws IOException {
		ANTLRInputStream input = new ANTLRInputStream(this.getClass()
				.getClassLoader().getResourceAsStream(fileName));
		plsqlLexer lexer = new plsqlLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		plsqlParser parser = new plsqlParser(tokens);
		ParseTree tree = parser.sql_script();
		return tree;
	}

	private CrudListener walkTreeWithCrudListener(ParseTree tree) {
		CrudListener crudListener = new CrudListener();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(crudListener, tree);
		return crudListener;
	}

	private Set<Table> tableSet(String... tableIds) {
		Set<Table> tables = new HashSet<Table>();
		for (String table : tableIds)
			tables.add(new Table(table));
			
		return tables;
	}

	private Set<Column> columnSet(String... columnNames) {
		Set<Column> columns = new HashSet<Column>();
		for (String column : columnNames)
			columns.add(new Column(column));
			
		return columns;
	}

	@Test
	public void simpleSelect() throws IOException {
		ParseTree tree = parseFile("simple-select.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		Crud crud = crudListener.getCrud();
		
		assertThat(crud.getReadTables(), is(tableSet("TABELA1")));
		assertThat(crud.getCreateTables().isEmpty(), is(true));
		assertThat(crud.getUpdateTables().isEmpty(), is(true));
		assertThat(crud.getDeleteTables().isEmpty(), is(true));
	}

	@Test
	public void simpleInsert() throws IOException {
		ParseTree tree = parseFile("simple-insert.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		Crud crud = crudListener.getCrud();
		
		assertThat(crud.getReadTables().isEmpty(), is(true));
		assertThat(crud.getCreateTables(), is(tableSet("TABELA1")));
		assertThat(crud.getUpdateTables().isEmpty(), is(true));
		assertThat(crud.getDeleteTables().isEmpty(), is(true));
	}
	
	@Test
	public void simpleUpdate() throws IOException {
		ParseTree tree = parseFile("simple-update.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		Crud crud = crudListener.getCrud();
		
		assertThat(crud.getReadTables().isEmpty(), is(true));
		assertThat(crud.getCreateTables().isEmpty(), is(true));
		assertThat(crud.getUpdateTables(), is(tableSet("TABELA1")));
		assertThat(crud.getDeleteTables().isEmpty(), is(true));
	}
	
	@Test
	public void simpleDelete() throws IOException {
		ParseTree tree = parseFile("simple-delete.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		Crud crud = crudListener.getCrud();
		
		assertThat(crud.getReadTables().isEmpty(), is(true));
		assertThat(crud.getCreateTables().isEmpty(), is(true));
		assertThat(crud.getUpdateTables().isEmpty(), is(true));
		assertThat(crud.getDeleteTables(), is(tableSet("TABELA1")));
	}
	
	@Test
	public void insertOfSelect() throws IOException {
		ParseTree tree = parseFile("insert-of-select.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		Crud crud = crudListener.getCrud();
		
		assertThat(crud.getReadTables(), is(tableSet("TABELA1")));
		assertThat(crud.getCreateTables(), is(tableSet("TABELA2")));
		assertThat(crud.getUpdateTables().isEmpty(), is(true));
		assertThat(crud.getDeleteTables().isEmpty(), is(true));
	}

	@Test
	public void selectMultipleTables() throws IOException {
		ParseTree tree = parseFile("select-multiple-tables.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		Crud crud = crudListener.getCrud();
		
		assertThat(crud.getReadTables(), is(tableSet("TABELA1", "TABELA2", "TABELA3")));
		assertThat(crud.getCreateTables().isEmpty(), is(true));
		assertThat(crud.getUpdateTables().isEmpty(), is(true));
		assertThat(crud.getDeleteTables().isEmpty(), is(true));
	}
	
	@Test
	public void selectTableWithSchema() throws IOException {
		ParseTree tree = parseFile("table-with-schema.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		Crud crud = crudListener.getCrud();
		
		assertThat(crud.getReadTables(), is(tableSet("SCHEMA1.TABELA1")));
		assertThat(crud.getCreateTables().isEmpty(), is(true));
		assertThat(crud.getUpdateTables().isEmpty(), is(true));
		assertThat(crud.getDeleteTables().isEmpty(), is(true));
	}

	@Test
	public void simpleSelectColumns() throws IOException {
		ParseTree tree = parseFile("simple-select.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		Crud crud = crudListener.getCrud();
		
		assertThat(crud.getReadColumns(), is(columnSet("COLUNA1", "COLUNA2", "COLUNA3")));
		assertThat(crud.getCreateColumns().isEmpty(), is(true));
		assertThat(crud.getUpdateColumns().isEmpty(), is(true));
	}
	
	@Test
	public void simpleInsertColumns() throws IOException {
		ParseTree tree = parseFile("simple-insert.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		Crud crud = crudListener.getCrud();
		
		assertThat(crud.getReadColumns().isEmpty(), is(true));
		assertThat(crud.getCreateColumns(), is(columnSet("COLUNA1", "COLUNA2", "COLUNA3")));
		assertThat(crud.getUpdateColumns().isEmpty(), is(true));
	}
	
	@Test
	public void simpleUpdateColumns() throws IOException {
		ParseTree tree = parseFile("simple-update.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		Crud crud = crudListener.getCrud();
		
		assertThat(crud.getReadColumns().isEmpty(), is(true));
		assertThat(crud.getCreateColumns().isEmpty(), is(true));
		assertThat(crud.getUpdateColumns(), is(columnSet("COLUNA2", "COLUNA3")));
	}
}
