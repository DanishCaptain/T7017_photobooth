package org.fogrobotics.photobooth.photo;

import java.io.File;

import org.fogrobotics.photobooth.model.customers.Customer;

public class Photo
{
  private Customer c;
  private File file;

  public void setCustomer(Customer c)
  {
    this.c = c;
  }

  public void setArtifact(File file)
  {
    this.file = file;
  }

  public File getArtiface()
  {
    return file;
  }

}
