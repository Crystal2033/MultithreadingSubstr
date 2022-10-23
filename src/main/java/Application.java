import FileWorkers.FileCommunicator;
import FileWorkers.FileDataWriter;
import ThreadClasses.TextBlockChanger;
import ThreadClasses.ThreadSeeker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static Settings.CONSTANTS.VALUE_OF_THREADS;

public class Application {
    public static final String PATH = "D:\\Paul\\Programming\\Java\\RPKS\\Labs\\MultithreadingSubstr\\src\\main\\resources";
    private static final String inFile = PATH + "\\LOTR.txt";//"\\out.txt";
    private static final String outFile = PATH + "\\out2.txt";


    public static void main(String[] args) throws IOException {
        final int epsilonTextGet = 0;
        final String keyWord = "Tolkien";


        //todo: checking that 0 < epsilonTextGet <= 3*VALUE_OF_LINES_IN_BLOCK
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

        } catch (IOException | InterruptedException ioException) {
            System.out.println(ioException.getMessage());
        }


//        FileDataWriter fileDataWriter = new FileDataWriter(outFile);
//
//        int counter = 0;
//        while(true){
//            String outputString = "";
//            for(int i = 0; i < 20; i++){
//                outputString = outputString + " " + Integer.toString(counter++);
//            }
//            fileDataWriter.writeLine(outputString);
//        }
    }
}
