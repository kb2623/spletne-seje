package org.oosqljet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlStatement implements AutoCloseable {

   private Statement stat;

   public SqlStatement(Connection connection) throws SQLException {
      stat = connection.createStatement();
   }

   public int execUpdate(String query) throws SQLException {
      return stat.executeUpdate(query);
   }

   public ResultSet execQuery(String query) throws SQLException {
      return stat.executeQuery(query);
   }

   @Override
   public void close() throws SQLException {
      if (stat != null) stat.close();
   }
}
