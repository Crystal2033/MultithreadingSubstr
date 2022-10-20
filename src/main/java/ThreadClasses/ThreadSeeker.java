package ThreadClasses;

import FileWorkers.FileCommunicator;

public class ThreadSeeker implements Runnable {
    private static int threadID = 1;
    private final int valueOfPrevAndNextLines;
    private final FileCommunicator fileCommunicator;
    private final int id;
    private int alreadyCheckedBlocks = 0;

    public ThreadSeeker(int valueOfPrevAndNextLines, FileCommunicator fileCommunicator) {
        this.valueOfPrevAndNextLines = valueOfPrevAndNextLines;
        this.fileCommunicator = fileCommunicator;
        id = threadID++;
    }

    @Override
    public void run() {
        //TODO!
    }
}
