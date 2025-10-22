package com.assign.entities.accountNumGenerator;


import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/*
  * Custom logc to generate account numbers with prefix "ABC" followed by a 9-digit sequential number.
 */

public class AccountNumGenerator implements IdentifierGenerator {

  private static int counter = 1;

  @Override
  public Object generate( SharedSessionContractImplementor session, Object object ) {
    String prefix = "ABC";
    String suffix = String.format("%09d", counter++);
    return prefix + suffix;
  }

}
