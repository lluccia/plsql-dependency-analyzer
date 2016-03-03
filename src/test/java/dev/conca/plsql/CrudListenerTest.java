package dev.conca.plsql;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	private List<Table> listOf(String... tables) {
		List<Table> tableList = new ArrayList<Table>();
		for (String table : tables)
			tableList.add(new Table(table));
			
		return tableList;
	}

	@Test
	public void simpleSelect() throws IOException {
		ParseTree tree = parseFile("simple-select.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables(), is(listOf("TABELA1")));
		assertThat(crudListener.getCreateTables().isEmpty(), is(true));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}

	@Test
	public void simpleInsert() throws IOException {
		ParseTree tree = parseFile("simple-insert.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables().isEmpty(), is(true));
		assertThat(crudListener.getCreateTables(), is(listOf("TABELA1")));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}
	
	@Test
	public void simpleUpdate() throws IOException {
		ParseTree tree = parseFile("simple-update.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables().isEmpty(), is(true));
		assertThat(crudListener.getCreateTables().isEmpty(), is(true));
		assertThat(crudListener.getUpdateTables(), is(listOf("TABELA1")));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}
	
	@Test
	public void simpleDelete() throws IOException {
		ParseTree tree = parseFile("simple-delete.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables().isEmpty(), is(true));
		assertThat(crudListener.getCreateTables().isEmpty(), is(true));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables(), is(listOf("TABELA1")));
	}
	
	@Test
	public void insertOfSelect() throws IOException {
		ParseTree tree = parseFile("insert-of-select.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables(), is(listOf("TABELA1")));
		assertThat(crudListener.getCreateTables(), is(listOf("TABELA2")));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}

	@Test
	public void selectMultipleTables() throws IOException {
		ParseTree tree = parseFile("select-multiple-tables.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables(), is(listOf("TABELA1", "TABELA2", "TABELA3")));
		assertThat(crudListener.getCreateTables().isEmpty(), is(true));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}
	
	@Test
	public void selectTableWithSchema() throws IOException {
		ParseTree tree = parseFile("table-with-schema.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables(), is(listOf("SCHEMA1.TABELA1")));
		assertThat(crudListener.getCreateTables().isEmpty(), is(true));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}
	
}
