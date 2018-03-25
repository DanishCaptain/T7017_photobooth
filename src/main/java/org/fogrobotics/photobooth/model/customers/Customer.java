package org.fogrobotics.photobooth.model.customers;

import java.util.UUID;

public class Customer
{
  private UUID uuid;
  private String name;
  private String teamNumber;
  private String email;

  public UUID getOid()
  {
    return uuid;
  }

  public void setOid(UUID uuid)
  {
    this.uuid = uuid;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getTeamNumber()
  {
    return teamNumber;
  }

  public void setTeamNumber(String teamNumber)
  {
    this.teamNumber = teamNumber;
  }

  public String getEmailAddress()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  @Override
  public String toString()
  {
    return teamNumber + " " + name + " :: " + email;
  }

}
