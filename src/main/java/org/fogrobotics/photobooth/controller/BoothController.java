package org.fogrobotics.photobooth.controller;

import org.fogrobotics.photobooth.model.BoothModel;
import org.fogrobotics.photobooth.model.DatabaseBoothException;
import org.fogrobotics.photobooth.model.customers.Customer;
import org.fogrobotics.photobooth.model.photo.Photo;

public class BoothController
{
  private BoothModel model;

  public BoothController(BoothModel model)
  {
    this.model = model;
  }

  public void addNew(String name, String teamNumber, String emailAddress) throws DatabaseBoothException
  {
    model.addCustomer(name, teamNumber, emailAddress);
  }

  public void update(String oid, String name, String teamNumber, String emailAddress) throws DatabaseBoothException
  {
    model.updateCustomer(oid, name, teamNumber, emailAddress);
  }

  public void sendEmail(Customer c, Photo p)
  {
    model.sendEmail(c, p);
  }

  public void takePhoto(Customer c)
  {
    model.takePhoto(c);
  }

}
