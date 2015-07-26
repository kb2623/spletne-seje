package org.oosql;

import java.io.File;
import java.sql.*;
import java.util.Properties;

public class SqlDriver implements AutoCloseable {

   private Connection con;

   public SqlDriver(String protocol, String path, Properties props) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
      this(protocol, new File(path), props);
   }

   public SqlDriver(String protocol, File file, Properties props) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
      if (props != null) con = DriverManager.getConnection(protocol + ":" + file.getAbsolutePath(), props);
      else con = DriverManager.getConnection(protocol + ":" + file.getAbsolutePath());
      con.setAutoCommit(false);
      System.out.println("Driver " + con.getMetaData().getDriverName() + " loaded!!");
   }

   public SqlStatement getStatement() throws SQLException {
      return new SqlStatement(con);
   }

	public ResultSet getTables(String name) throws SQLException {
		return con.getMetaData().getTables(null, null, name, null);
	}

	public ResultSet getColumns(String tableName) throws SQLException {
		return con.getMetaData().getColumns(null, null, tableName, null);
	}

   public boolean execUpdate(String... args) throws SQLException {
      try {
         Statement stat = con.createStatement();
         for (String query : args) stat.executeUpdate(query);
         stat.close();
         con.commit();
         return true;
      } catch (SQLException e) {
         con.rollback();
         return false;
      }
   }

   public void commit() throws SQLException {
      con.commit();
   }

   public void rollback() throws SQLException {
      con.rollback();
   }

   @Override
   public void close() throws SQLException {
      if (con != null) con.close();
   }
}
