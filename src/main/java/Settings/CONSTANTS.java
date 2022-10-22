package Settings;

public class CONSTANTS {
    public static final int VALUE_OF_THREADS = 4;
    public static final int THREAD_READ_LINES_VALUE = 40; //shouldn`t be more than 3 * VALUE_OF_LINES_IN_BLOCK
    public static final int VALUE_OF_LINES_IN_BLOCK = 100; //this value has to be more than VALUE_OF_THREADS*THREAD_READ_LINES_VALUE
}
