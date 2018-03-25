package org.fogrobotics.photobooth.model.photo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.fogrobotics.photobooth.model.BoothModel;
import org.fogrobotics.photobooth.model.customers.Customer;

public class PhotoManager
{
  private ArrayList<PhotoUpdatesListener> pListeners = new ArrayList<PhotoUpdatesListener>();
  private ArrayList<PhotoMemoListener> memoListeners = new ArrayList<PhotoMemoListener>();
  private BoothModel model;
  private File dir;

  public PhotoManager(BoothModel model)
  {
    this.model = model;
    dir = model.getPhotoLocation();
  }

  public void takePhoto(Customer c)
  {
    File file = new File(dir, UUID.randomUUID()+".jpg");
    Photo p = new Photo();
    p.setCustomer(c);
    p.setArtifact(file);
    boolean status = activateCamera(file);
    if (status) {
      notifyUpdated(p);
      setDisplayMemo("photo taken - ready to email");
      
    } else {
      notifyUpdated(null);
      setDisplayMemo("photo failed");
    }
  }

  private boolean activateCamera(File file)
  {
    String command = "fswebcam "+file.getPath();

    // Running the above command
    Runtime run  = Runtime.getRuntime();
    try
    {
      Process proc = run.exec(command);
      proc.waitFor();
      return true;
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return false;
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
      return false;
    }
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

  public void setDisplayMemo(String memo)
  {
    for (PhotoMemoListener lis : memoListeners) {
      lis.photoMemoChange(memo);
    }
  }

  public void addPhotoMemoListener(PhotoMemoListener lis)
  {
    memoListeners.add(lis);
  }

}
