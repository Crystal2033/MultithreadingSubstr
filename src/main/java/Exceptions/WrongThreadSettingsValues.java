package Exceptions;

public class WrongThreadSettingsValues extends Exception {
    private final String errorMessage;
    public WrongThreadSettingsValues(String s) {
        errorMessage = s;
    }

    @Override
    public String getMessage(){
        return  errorMessage;
    }
}
