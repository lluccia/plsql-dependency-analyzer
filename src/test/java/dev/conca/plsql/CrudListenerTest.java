package dev.conca.plsql;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
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

	private List<String> listOf(String... string) {
		return Arrays.asList(string);
	}

	@Test
	public void simpleSelect() throws IOException {
		ParseTree tree = parseFile("simple-select.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables(), is(listOf("tabela1")));
		assertThat(crudListener.getCreateTables().isEmpty(), is(true));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}

	@Test
	public void simpleInsert() throws IOException {
		ParseTree tree = parseFile("simple-insert.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables().isEmpty(), is(true));
		assertThat(crudListener.getCreateTables(), is(listOf("tabela1")));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}
	
	@Test
	public void simpleUpdate() throws IOException {
		ParseTree tree = parseFile("simple-update.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables().isEmpty(), is(true));
		assertThat(crudListener.getCreateTables().isEmpty(), is(true));
		assertThat(crudListener.getUpdateTables(), is(listOf("tabela1")));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}
	
	@Test
	public void simpleDelete() throws IOException {
		ParseTree tree = parseFile("simple-delete.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables().isEmpty(), is(true));
		assertThat(crudListener.getCreateTables().isEmpty(), is(true));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables(), is(listOf("tabela1")));
	}
	
	@Test
	public void insertOfSelect() throws IOException {
		ParseTree tree = parseFile("insert-of-select.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables(), is(listOf("tabela1")));
		assertThat(crudListener.getCreateTables(), is(listOf("tabela2")));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}

	@Test
	public void selectMultipleTables() throws IOException {
		ParseTree tree = parseFile("select-multiple-tables.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables(), is(listOf("tabela1", "tabela2", "tabela3")));
		assertThat(crudListener.getCreateTables().isEmpty(), is(true));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}
	
	@Test
	public void selectTableWithSchema() throws IOException {
		ParseTree tree = parseFile("table-with-schema.sql");
		
		CrudListener crudListener = walkTreeWithCrudListener(tree);
		
		assertThat(crudListener.getReadTables(), is(listOf("schema1.tabela1")));
		assertThat(crudListener.getCreateTables().isEmpty(), is(true));
		assertThat(crudListener.getUpdateTables().isEmpty(), is(true));
		assertThat(crudListener.getDeleteTables().isEmpty(), is(true));
	}


}
