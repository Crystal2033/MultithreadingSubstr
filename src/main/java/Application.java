import COLORS.ConsoleColors;
import Exceptions.BadArgsException;
import FileWorkers.FileCommunicator;
import TextHelpers.ArgsConverter;
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

/**
 * @project SubstrFinder
 * ©Crystal2033
 * @date 21/10/2022
 */
public class Application {

    public static void main(String[] args) {

        final String inFile;
        final String outFile;
        final String keyWord;
        final int linesBeforeAndAfterKeyword;
        try {
            ArgsConverter argsConverter = new ArgsConverter(args);
            inFile = argsConverter.getInputFileName();
            outFile = argsConverter.getOutputFileName();
            keyWord = argsConverter.getKeyWord();
            linesBeforeAndAfterKeyword = argsConverter.getPrevAndNextTextLines();
        } catch (BadArgsException exception) {
            System.out.println(exception.getMessage());
            return;
        }


        try {
            FileCommunicator fileCommunicator = new FileCommunicator(inFile, outFile);
            ExecutorService threadingFixedPool = Executors.newFixedThreadPool(VALUE_OF_THREADS);
            CyclicBarrier cyclicBarrier = new CyclicBarrier(VALUE_OF_THREADS, new TextBlockChanger(fileCommunicator));

            List<ThreadSeeker> listOfThreads = new ArrayList<>();
            for (int i = 0; i < VALUE_OF_THREADS; i++) {
                ThreadSeeker threadSeeker = new ThreadSeeker(linesBeforeAndAfterKeyword, fileCommunicator, keyWord, cyclicBarrier);
                listOfThreads.add(threadSeeker);
            }

            List<Future<?>> listOfFuture = new ArrayList<>();
            for (int i = 0; i < VALUE_OF_THREADS; i++) {
                Future<?> future = threadingFixedPool.submit(listOfThreads.get(i));
                listOfFuture.add(future);
            }

            boolean allIsDone = false;
            while (!allIsDone) {
                allIsDone = listOfFuture.stream().allMatch(Future::isDone);
            }

            threadingFixedPool.shutdown();
            System.out.println(ConsoleColors.GREEN_BRIGHT + "Work is done." + ConsoleColors.RESET + " Check " + outFile +
                    " file. Was found " + ConsoleColors.CYAN_BRIGHT + fileCommunicator.getFoundKeyWords()
                    + ConsoleColors.RESET + " matches.");
            fileCommunicator.closeBuffers();

        } catch (IOException | InterruptedException | IllegalArgumentException ioException) {
            System.out.println(ioException.getMessage());
        }
    }
}
