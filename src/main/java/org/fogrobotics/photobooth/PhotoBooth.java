package org.fogrobotics.photobooth;

import org.fogrobotics.photobooth.common.PropertyBoothException;
import org.fogrobotics.photobooth.controller.BoothController;
import org.fogrobotics.photobooth.display.PhotoBoothDisplay;
import org.fogrobotics.photobooth.model.BoothModel;
import org.fogrobotics.photobooth.model.DatabaseBoothException;

public class PhotoBooth
{

  private BoothModel model;
  private BoothController controller;

  public PhotoBooth() throws DatabaseBoothException
  {
    try
    {
      model = new BoothModel();
      controller = new BoothController(model);
      new PhotoBoothDisplay(model, controller);

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
