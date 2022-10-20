package ThreadClasses;

import FileWorkers.FileCommunicator;

public class ThreadSeeker implements Runnable {
    private static int threadID = 1;
    private final int valueOfPrevAndNextLines;
    private final FileCommunicator fileCommunicator;
    private final int id;
    private int alreadyCheckedBlocks = 0;
    private final Thread thread;

    public ThreadSeeker(int valueOfPrevAndNextLines, FileCommunicator fileCommunicator) {
        this.valueOfPrevAndNextLines = valueOfPrevAndNextLines;
        this.fileCommunicator = fileCommunicator;
        id = threadID++;
        thread = new Thread("Hobbit" + Integer.toString(id));
    }

    @Override
    public void run() {
        System.out.println("Heeeeey, my name is: " + thread.getName());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
