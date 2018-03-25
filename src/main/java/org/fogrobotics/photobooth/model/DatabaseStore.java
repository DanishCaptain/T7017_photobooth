package org.fogrobotics.photobooth.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.fogrobotics.photobooth.model.customers.Customer;

import com.google.gson.Gson;

public abstract class DatabaseStore
{
  protected static final int TABLE_DOES_NOT_EXIST = 1;
  protected static final int SQL_SYNTAX_ERROR = 2;

  
  public List<Customer> getCustomers() throws DatabaseBoothException
  {
    ArrayList<Customer> list = new ArrayList<Customer>();
    Connection cc = createConnection();
    try
    {
      String sql = "select oid, text from customer";
      Statement stmt = cc.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      Gson gson = new Gson();
      while(rs.next()) {
        Customer c = gson.fromJson(rs.getString("text"), Customer.class);
        list.add(c);
      }
    }
    catch (SQLException e)
    {
      if (TABLE_DOES_NOT_EXIST == translateError(e))
      {
        createTable(cc);
        return getCustomers();
      }
      else
      {
        e.printStackTrace();
        throw new DatabaseBoothException(e);
      }
    }
    
    return list;
  }

  
  public final void addNew(Customer c) throws DatabaseBoothException
  {
    Connection cc = createConnection();
    String value = new Gson().toJson(c);
    String sql = "insert into customer (oid, text) values ('" + c.getOid().toString() + "', '" + value + "')";
    try
    {
      Statement stmt = cc.createStatement();
      int rs = stmt.executeUpdate(sql);
      System.out.println(rs);
    }
    catch (SQLException e)
    {
      if (TABLE_DOES_NOT_EXIST == translateError(e))
      {
        createTable(cc);
        addNew(c);
      }
      else
      {
        e.printStackTrace();
        throw new DatabaseBoothException(e);
      }
    }
  }

  private void createTable(Connection cc) throws DatabaseBoothException
  {
    String sql = "create table customer ("+
  "oid varchar(40) not null,"+
  "text varchar(1000) not null,"+
  "primary key (oid))";
    try
    {
      Statement stmt = cc.createStatement();
      int rs = stmt.executeUpdate(sql);
      System.out.println(rs);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      throw new DatabaseBoothException(e);
    }

  }


  protected abstract int translateError(SQLException e);


  public final void update(Customer c) throws DatabaseBoothException
  {
    Connection cc = createConnection();
    String value = new Gson().toJson(c);
    String sql = "update customer set text='"+value+"' where oid='"+c.getOid()+"'";
    try
    {
      Statement stmt = cc.createStatement();
      int rs = stmt.executeUpdate(sql);
      System.out.println(rs);
    }
    catch (SQLException e)
    {
      System.out.println("ec "+e.getErrorCode());
      if (TABLE_DOES_NOT_EXIST == translateError(e))
      {
        createTable(cc);
        addNew(c);
      }
      else
      {
        e.printStackTrace();
        throw new DatabaseBoothException(e);
      }
    }

  }

  protected abstract Connection createConnection() throws DatabaseBoothException;


}
