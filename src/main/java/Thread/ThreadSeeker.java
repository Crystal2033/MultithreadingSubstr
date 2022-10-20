package Thread;

import FileWorkers.FileCommunicator;

public class ThreadSeeker implements Runnable{
    private int alreadyCheckedBlocks = 0;
    private final int valueOfPrevAndNextLines;
    private final FileCommunicator fileCommunicator;

    ThreadSeeker(int valueOfPrevAndNextLines, FileCommunicator fileCommunicator){
        this.valueOfPrevAndNextLines = valueOfPrevAndNextLines;
        this.fileCommunicator = fileCommunicator;
    }

    @Override
    public void run() {
        //TODO!
    }
}
