package org.fogrobotics.photobooth.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fogrobotics.photobooth.common.PropertyBoothException;
import org.fogrobotics.photobooth.model.customers.Customer;
import org.fogrobotics.photobooth.model.customers.CustomerManager;
import org.fogrobotics.photobooth.model.customers.CustomerUpdatesListener;
import org.fogrobotics.photobooth.model.email.EmailManager;
import org.fogrobotics.photobooth.model.email.EmailMemoListener;
import org.fogrobotics.photobooth.model.photo.Photo;
import org.fogrobotics.photobooth.model.photo.PhotoManager;
import org.fogrobotics.photobooth.model.photo.PhotoMemoListener;
import org.fogrobotics.photobooth.model.photo.PhotoUpdatesListener;

public class BoothModel
{
  private static final Logger LOG = Logger.getLogger(BoothModel.class.getName());
  private static final String PROPERTIES_FILE_NAME = "booth.properties";
  private static final String TAG_PHOTO_LOCATION = "photo-archive-directory";
  private Properties p = new Properties();
  private File photoLocation;
  private DatabaseStore store;
  private PhotoManager photoManager;
  private CustomerManager customers;
  private EmailManager email;

  public BoothModel() throws PropertyBoothException, DatabaseBoothException {
    InputStream is = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
    try
    {
      p.load(is);
    }
    catch (IOException e)
    {
      throw new PropertyBoothException("properties file ("+PROPERTIES_FILE_NAME+") not found.");
    }
    photoLocation = new File(p.getProperty(TAG_PHOTO_LOCATION, "/opt/fogrobotics/booth"));
    if (!photoLocation.exists()) {
      photoLocation.mkdir();
    }
    LOG.log(Level.INFO, "using location: "+photoLocation.getAbsolutePath());
    store = new DerbyStore();
    
    photoManager = new PhotoManager(this);
    customers = new CustomerManager(this);
    email = new EmailManager(this);
    
  }

  public DatabaseStore getStore()
  {
    return store;
  }

  public void addCustomer(String name, String teamNumber, String emailAddress) throws DatabaseBoothException
  {
    customers.add(name, teamNumber, emailAddress);
  }

  public void updateCustomer(String oid, String name, String teamNumber, String emailAddress) throws DatabaseBoothException
  {
    customers.update(oid, name, teamNumber, emailAddress);
  }

  public void addCustomerUpdatesListener(CustomerUpdatesListener lis)
  {
    customers.addCustomerUpdatesListener(lis);    
  }

  public void takePhoto(Customer c)
  {
    photoManager.takePhoto(c);
  }

  public void addPhotoUpdatesListener(PhotoUpdatesListener lis)
  {
    photoManager.addPhotoUpdatesListener(lis);
  }

  public void addPhotoMemoListener(PhotoMemoListener lis)
  {
    photoManager.addPhotoMemoListener(lis);
  }

  public void sendEmail(Customer c, Photo p)
  {
    email.sendEmail(c, p);
  }

  public void addEmailMemoListener(EmailMemoListener lis)
  {
    email.addEmailMemoListener(lis);
  }

  public File getPhotoLocation()
  {
    return photoLocation;
  }

}
