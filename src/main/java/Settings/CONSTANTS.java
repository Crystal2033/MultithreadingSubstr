package Settings;

public class CONSTANTS {
    public static final int VALUE_OF_THREADS = 2;
    public static final int VALUE_OF_LINES_IN_BLOCK = 10; //this value has to be more than VALUE_OF_THREADS*THREAD_READ_LINES_VALUE
    public static final int THREAD_READ_LINES_VALUE = 4; //shouldn`t be more than 3 * VALUE_OF_LINES_IN_BLOCK

}
