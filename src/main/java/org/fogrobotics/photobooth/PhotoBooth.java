package org.fogrobotics.photobooth;

import org.fogrobotics.photobooth.common.PropertyBoothException;
import org.fogrobotics.photobooth.model.BoothModel;

public class PhotoBooth {

	private BoothModel model;

  public PhotoBooth() {
	  try
    {
      model = new BoothModel();
    }
    catch (PropertyBoothException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
	}
	
	public static void main(String[] args) {
		new PhotoBooth();
		
	}

}
