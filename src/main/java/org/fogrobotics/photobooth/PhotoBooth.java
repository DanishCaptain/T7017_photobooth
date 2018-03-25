package org.fogrobotics.photobooth;

import org.fogrobotics.photobooth.common.PropertyBoothException;
import org.fogrobotics.photobooth.controller.BoothController;
import org.fogrobotics.photobooth.display.PhotoBoothDisplay;
import org.fogrobotics.photobooth.email.EmailManager;
import org.fogrobotics.photobooth.model.BoothModel;
import org.fogrobotics.photobooth.model.DatabaseBoothException;
import org.fogrobotics.photobooth.model.customers.CustomerManager;
import org.fogrobotics.photobooth.photo.PhotoManager;

public class PhotoBooth
{

  private BoothModel model;
  private BoothController controller;

  public PhotoBooth() throws DatabaseBoothException
  {
    try
    {
      model = new BoothModel();
      PhotoManager photoManager = new PhotoManager(model);
      CustomerManager customers = new CustomerManager(model);
      EmailManager email = new EmailManager(model);
      controller = new BoothController(model, photoManager, customers, email);
      PhotoBoothDisplay display = new PhotoBoothDisplay(customers, photoManager, controller);

    }
    catch (PropertyBoothException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {
    try
    {
      new PhotoBooth();
    }
    catch (DatabaseBoothException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
