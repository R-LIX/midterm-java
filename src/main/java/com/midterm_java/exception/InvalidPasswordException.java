package com.midterm_java.exception;

public class InvalidPasswordException extends Exception{
   public InvalidPasswordException(String message) {
       super(message);
   }
}
