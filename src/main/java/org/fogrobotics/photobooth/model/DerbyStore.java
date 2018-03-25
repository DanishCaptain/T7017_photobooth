package org.fogrobotics.photobooth.model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.derby.jdbc.EmbeddedDriver;

public class DerbyStore extends DatabaseStore
{
  private File dataDir;

  public DerbyStore() throws DatabaseBoothException

  {
    try
    {
      DriverManager.registerDriver(new EmbeddedDriver());
    }
    catch (SQLException e)
    {
      throw new DatabaseBoothException(e);
    }
    dataDir = new File(".");
  }

  @Override
  protected Connection createConnection() throws DatabaseBoothException
  {
    File databaseFile = new File(dataDir, "photoBoothDB");
    if (!databaseFile.exists())
    {
      try
      {
        return DriverManager.getConnection("jdbc:derby:" + databaseFile.getAbsolutePath() + ";create=true");
      }
      catch (SQLException e)
      {
        throw new DatabaseBoothException(e);
      }
    }
    else
    {
      try
      {
        return DriverManager.getConnection("jdbc:derby:" + databaseFile.getAbsolutePath());
      }
      catch (SQLException e)
      {
        throw new DatabaseBoothException(e);
      }
    }
  }

  @Override
  protected int translateError(SQLException e)
  {
    switch (e.getErrorCode()) {
      case 30000:
        if (e.getMessage().contains("Syntax error")) {
          return SQL_SYNTAX_ERROR;
          
        } else {
          return TABLE_DOES_NOT_EXIST;
        }
      default:
        System.out.println("unknown error: "+e.getErrorCode()+":"+e.getMessage());
    }
    return Integer.MIN_VALUE;
  }

}
