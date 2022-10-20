import FileWorkers.FileCommunicator;
import ThreadClasses.ThreadSeeker;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Settings.CONSTANTS.VALUE_OF_THREADS;

public class Application {
    public static final String PATH = "D:\\Paul\\Programming\\Java\\RPKS\\Labs\\MultithreadingSubstr\\src\\main\\resources";
    private static final String inFile = PATH + "\\LOTR.txt";
    private static final String outFile = PATH + "\\out.txt";

    public static void main(String[] args) {
        final int epsilonTextGet = 3;
        try {
            FileCommunicator fileCommunicator = new FileCommunicator(inFile, outFile);
            ExecutorService threadingFixedPool = Executors.newFixedThreadPool(VALUE_OF_THREADS);
            ThreadSeeker threadSeeker1 = new ThreadSeeker(epsilonTextGet, fileCommunicator);
            ThreadSeeker threadSeeker2 = new ThreadSeeker(epsilonTextGet, fileCommunicator);
            ThreadSeeker threadSeeker3 = new ThreadSeeker(epsilonTextGet, fileCommunicator);
            ThreadSeeker threadSeeker4 = new ThreadSeeker(epsilonTextGet, fileCommunicator);

            threadingFixedPool.submit(threadSeeker1);
            threadingFixedPool.submit(threadSeeker2);
            threadingFixedPool.submit(threadSeeker3);
            threadingFixedPool.submit(threadSeeker4);


            while (!fileCommunicator.isOver()) {
                //TODO add checker thread
            }

            threadingFixedPool.shutdown();
            fileCommunicator.closeBuffers();

        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());

        }


    }
}
