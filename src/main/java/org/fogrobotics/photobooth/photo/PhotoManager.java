package org.fogrobotics.photobooth.photo;

import java.io.File;
import java.util.ArrayList;

import org.fogrobotics.photobooth.controller.BoothController;
import org.fogrobotics.photobooth.model.BoothModel;
import org.fogrobotics.photobooth.model.customers.Customer;
import org.fogrobotics.photobooth.model.customers.CustomerUpdatesListener;

public class PhotoManager
{
  private ArrayList<PhotoUpdatesListener> pListeners = new ArrayList<PhotoUpdatesListener>();
  private BoothModel model;

  public PhotoManager(BoothModel model)
  {
    this.model = model;
  }

  public void takePhoto(Customer c)
  {
    Photo p = new Photo();
    p.setCustomer(c);
    p.setArtifact(new File("FruitSalad.jpg"));
    System.out.println(p.getArtiface().exists());
    notifyUpdated(p);
  }

  private void notifyUpdated(Photo p)
  {
    synchronized (pListeners) {
      for (PhotoUpdatesListener lis : pListeners) {
        lis.update(p);
      }
    }
  }
  
  public void addPhotoUpdatesListener(PhotoUpdatesListener lis)
  {
    synchronized (pListeners) {
      pListeners.add(lis);
    }
  }
  
  public void removePhotoUpdatesListener(PhotoUpdatesListener lis)
  {
    synchronized (pListeners) {
      pListeners.remove(lis);
    }
  }

}
