package org.fogrobotics.photobooth.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.fogrobotics.photobooth.common.PropertyBoothException;

public class BoothModel
{
  private static final Logger LOG = Logger.getLogger(BoothModel.class.getName());
  private static final String PROPERTIES_FILE_NAME = "booth.properties";
  private static final String TAG_PHOTO_LOCATION = "photo-archive-directory";
  private Properties p = new Properties();
  private File photoLocation;

  public BoothModel() throws PropertyBoothException {
    InputStream is = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
    try
    {
      p.load(is);
    }
    catch (IOException e)
    {
      throw new PropertyBoothException("properties file ("+PROPERTIES_FILE_NAME+") not found.");
    }
    photoLocation = new File(p.getProperty(TAG_PHOTO_LOCATION, "/etc/fogrobotics/booth"));
    LOG.log(Level.INFO, "using location: "+photoLocation.getAbsolutePath());
  }

  
  
}
