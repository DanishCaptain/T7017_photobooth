package org.fogrobotics.photobooth.model.customers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.fogrobotics.photobooth.model.BoothModel;
import org.fogrobotics.photobooth.model.DatabaseBoothException;

@SuppressWarnings("unused")
public class CustomerManager
{
  private ArrayList<CustomerUpdatesListener> cListeners = new ArrayList<CustomerUpdatesListener>();
  private ArrayList<Customer> cList = new ArrayList<Customer>();
  private HashMap<String, Customer> cMap = new HashMap<String, Customer>();
  private BoothModel model;

  public CustomerManager(BoothModel model) throws DatabaseBoothException
  {
    this.model = model;
    initData();
  }

  private void initData() throws DatabaseBoothException
  {
    List<Customer> initList = model.getStore().getCustomers();
    for (Customer c : initList)
    {
      cList.add(c);
      cMap.put(c.getOid().toString(), c);
      notifyAdded(c);
    }
  }

  public void add(String name, String teamNumber, String emailAddress) throws DatabaseBoothException
  {
    Customer c = new Customer();
    c.setOid(UUID.randomUUID());
    c.setName(name);
    c.setTeamNumber(teamNumber);
    c.setEmail(emailAddress);
    cList.add(c);
    cMap.put(c.getOid().toString(), c);
    model.getStore().addNew(c);
    notifyAdded(c);
  }

  public void update(String oid, String name, String teamNumber, String emailAddress) throws DatabaseBoothException
  {
    Customer c = cMap.get(oid);
    c.setName(name);
    c.setTeamNumber(teamNumber);
    c.setEmail(emailAddress);
    model.getStore().update(c);
    notifyUpdated(c);
  }

  public void addCustomerUpdatesListener(CustomerUpdatesListener lis)
  {
    synchronized (cListeners)
    {
      for (Customer c : cList)
      {
        lis.addNew(c);
      }
      cListeners.add(lis);
    }
  }

  public void removeCustomerUpdatesListener(CustomerUpdatesListener lis)
  {
    synchronized (cListeners)
    {
      cListeners.remove(lis);
    }
  }

  private void notifyAdded(Customer c)
  {
    synchronized (cListeners)
    {
      for (CustomerUpdatesListener lis : cListeners)
      {
        lis.addNew(c);
      }
    }
  }

  private void notifyUpdated(Customer c)
  {
    synchronized (cListeners)
    {
      for (CustomerUpdatesListener lis : cListeners)
      {
        lis.update(c);
      }
    }
  }

}
