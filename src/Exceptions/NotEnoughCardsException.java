package Exceptions;

public class NotEnoughCardsException extends Exception {
    public NotEnoughCardsException(String errormessage){
        super(errormessage);
    }
}
