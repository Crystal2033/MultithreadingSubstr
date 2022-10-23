import FileWorkers.FileCommunicator;
import ThreadClasses.TextBlockChanger;
import ThreadClasses.ThreadSeeker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static Settings.CONSTANTS.VALUE_OF_LINES_IN_BLOCK;
import static Settings.CONSTANTS.VALUE_OF_THREADS;

public class Application {
    public static final String PATH = "D:\\Paul\\Programming\\Java\\RPKS\\Labs\\MultithreadingSubstr\\src\\main\\resources";
    private static final String inFile = PATH + "\\LOTR.txt";//"\\out.txt";
    private static final String outFile = PATH + "\\out2.txt";


    public static void main(String[] args){
        final String epsilonTextGetStr = "2";
        final String keyWord = "Tolkien";

        final int epsilonTextGet = Integer.parseInt(epsilonTextGetStr);
        if(epsilonTextGet < 0 || epsilonTextGet >VALUE_OF_LINES_IN_BLOCK){
            System.out.println("Your epsilon value for next and previous value of lines should be more than 0 and " +
                    "less than " + VALUE_OF_LINES_IN_BLOCK + ".");
            return;
        }

        try {
            FileCommunicator fileCommunicator = new FileCommunicator(inFile, outFile);
            ExecutorService threadingFixedPool = Executors.newFixedThreadPool(VALUE_OF_THREADS);
            CyclicBarrier cyclicBarrier = new CyclicBarrier(VALUE_OF_THREADS, new TextBlockChanger(fileCommunicator));

            List<ThreadSeeker> listOfThreads = new ArrayList<>();
            for (int i = 0; i < VALUE_OF_THREADS; i++) {
                ThreadSeeker threadSeeker = new ThreadSeeker(epsilonTextGet, fileCommunicator, keyWord, cyclicBarrier);
                listOfThreads.add(threadSeeker);
            }

            long start = System.nanoTime();
            List<Future<?>> listOfFuture = new ArrayList<>();
            for (int i = 0; i < VALUE_OF_THREADS; i++) {
                Future<?> future = threadingFixedPool.submit(listOfThreads.get(i));
                listOfFuture.add(future);
            }

            boolean allIsDone = false;
            while (!allIsDone) {
                allIsDone = listOfFuture.stream().allMatch(Future::isDone);
            }
            long end = System.nanoTime();
            long time = end - start;
            System.out.println(time + " ns");
            threadingFixedPool.shutdown();
            fileCommunicator.closeBuffers();

        } catch (IOException | InterruptedException | IllegalArgumentException ioException) {
            System.out.println(ioException.getMessage());
            ioException.printStackTrace();
        }
    }
}
