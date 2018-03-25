package org.fogrobotics.photobooth.model.customers;

public interface CustomerUpdatesListener
{

  void addNew(Customer c);

  void update(Customer c);

}
