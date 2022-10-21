import FileWorkers.FileCommunicator;
import ThreadClasses.ThreadSeeker;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static Settings.CONSTANTS.VALUE_OF_THREADS;

public class Application {
    public static final String PATH = "D:\\Paul\\Programming\\Java\\RPKS\\Labs\\MultithreadingSubstr\\src\\main\\resources";
    private static final String inFile = PATH + "\\test.txt";//"\\LOTR.txt";
    private static final String outFile = PATH + "\\out.txt";
    private static String keyWord = "and";

    public static void main(String[] args) throws IOException {
        final int epsilonTextGet = 1;
        //todo: checking that 0 < epsilonTextGet <= 3*VALUE_OF_LINES_IN_BLOCK
        try {
            FileCommunicator fileCommunicator = new FileCommunicator(inFile, outFile);
            ExecutorService threadingFixedPool = Executors.newFixedThreadPool(VALUE_OF_THREADS);
            ThreadSeeker threadSeeker1 = new ThreadSeeker(epsilonTextGet, fileCommunicator, keyWord);
            ThreadSeeker threadSeeker2 = new ThreadSeeker(epsilonTextGet, fileCommunicator, keyWord);
            ThreadSeeker threadSeeker3 = new ThreadSeeker(epsilonTextGet, fileCommunicator, keyWord);
            ThreadSeeker threadSeeker4 = new ThreadSeeker(epsilonTextGet, fileCommunicator, keyWord);

            Future<?> future1 = threadingFixedPool.submit(threadSeeker1);
            Future<?> future2 = threadingFixedPool.submit(threadSeeker2);
            Future<?> future3 = threadingFixedPool.submit(threadSeeker3);
            Future<?> future4 = threadingFixedPool.submit(threadSeeker4);


            while (!future1.isDone() || !future2.isDone() || !future3.isDone() || !future4.isDone()) {
                System.out.println("heeeey");
            }
            System.out.println("Nice. DONE!");
            threadingFixedPool.shutdown();
            fileCommunicator.closeBuffers();

        } catch (IOException | InterruptedException ioException) {
            System.out.println(ioException.getMessage());

        }


    }
}
