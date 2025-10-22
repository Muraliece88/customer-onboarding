package com.assign.exceptions;


public class URINotFoundException extends  RuntimeException{
  public URINotFoundException(String message){
    super(message);
  }
}