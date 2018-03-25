package org.fogrobotics.photobooth.controller;

import org.fogrobotics.photobooth.email.EmailManager;
import org.fogrobotics.photobooth.model.BoothModel;
import org.fogrobotics.photobooth.model.DatabaseBoothException;
import org.fogrobotics.photobooth.model.customers.Customer;
import org.fogrobotics.photobooth.model.customers.CustomerManager;
import org.fogrobotics.photobooth.photo.Photo;
import org.fogrobotics.photobooth.photo.PhotoManager;

public class BoothController
{

  private BoothModel model;
  private PhotoManager photoManager;
  private CustomerManager customers;
  private EmailManager email;

  public BoothController(BoothModel model, PhotoManager photoManager, CustomerManager customers, EmailManager email)
  {
    this.model = model;
    this.photoManager = photoManager;
    this.customers = customers;
    this.email=email;
  }

  public void addNew(String name, String teamNumber, String emailAddress) throws DatabaseBoothException
  {
    customers.add(name, teamNumber, emailAddress);
  }

  public void update(String oid, String name, String teamNumber, String emailAddress) throws DatabaseBoothException
  {
    customers.update(oid, name, teamNumber, emailAddress);
  }

  public void sendEmail(Customer c, Photo p)
  {
    email.sendEmail(c, p);
  }

  public void takePhoto(Customer c)
  {
    photoManager.takePhoto(c);
  }

}
