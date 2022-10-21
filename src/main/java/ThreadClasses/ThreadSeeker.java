package ThreadClasses;

import FileWorkers.FileCommunicator;
import TextHelpers.TextBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Settings.CONSTANTS.THREAD_READ_LINES_VALUE;
import static Settings.CONSTANTS.VALUE_OF_LINES_IN_BLOCK;

public class ThreadSeeker implements Runnable {

    private static int threadID = 0;
    private final int valueOfPrevAndNextLines;
    private final FileCommunicator fileCommunicator;
    private final int id;
    private final Thread thread;
    private final String keyWord;
    private int alreadyCheckedBlocks = 0;

    public ThreadSeeker(int valueOfPrevAndNextLines, FileCommunicator fileCommunicator, String keyWord) throws InterruptedException {
        this.keyWord = keyWord;
        this.valueOfPrevAndNextLines = valueOfPrevAndNextLines;
        this.fileCommunicator = fileCommunicator;
        id = threadID++;
        thread = new Thread("Hobbit" + Integer.toString(id));

    }

    @Override
    public void run() {
        // TODO: while() AND ADD CONCURRENCY
        try {

            workWithPartOfText();
            alreadyCheckedBlocks++;
            System.out.println("end");
        } catch (IOException e) {
            e.printStackTrace();
            //fileCommunicator.closeBuffers();
        }
    }

    private void createTextAroundKeyWordAndSend(int posOfKeyWord) throws IOException {
        int leftEdge = posOfKeyWord - valueOfPrevAndNextLines;
        int rightEdge = posOfKeyWord + valueOfPrevAndNextLines;

        List<String> outputText = new ArrayList<>();

        if (leftEdge < 0 && rightEdge > VALUE_OF_LINES_IN_BLOCK - 1) {
            outputText.addAll(getTextFromPrevBlock(posOfKeyWord));
            outputText.addAll(getTextFromCurrentBlock(posOfKeyWord));
            outputText.addAll(getTextFromNextBlock(posOfKeyWord));
        } else if (leftEdge < 0) {
            outputText.addAll(getTextFromPrevBlock(posOfKeyWord));
            outputText.addAll(getTextFromCurrentBlock(posOfKeyWord));

        } else if (rightEdge > VALUE_OF_LINES_IN_BLOCK - 1) {
            outputText.addAll(getTextFromCurrentBlock(posOfKeyWord));
            outputText.addAll(getTextFromNextBlock(posOfKeyWord));
        } else {
            outputText.addAll(getTextFromCurrentBlock(posOfKeyWord));
        }
        outputText.add(thread.getName());
        fileCommunicator.insertTextInOutputFile(outputText);

    }


    private List<String> getTextFromPrevBlock(int posOfKeyWord) {
        TextBlock prevBlock = fileCommunicator.getPrevBlock();
        if (prevBlock.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> prevBlockText = prevBlock.getTextLines();
        int leftEdgeIndex = prevBlockText.size() - (posOfKeyWord - valueOfPrevAndNextLines);

        if (leftEdgeIndex < 0) {
            return prevBlockText;
        }
        return new ArrayList<>(prevBlockText.subList(leftEdgeIndex, prevBlockText.size()));
    }

    private List<String> getTextFromCurrentBlock(int posOfKeyWord) {
        TextBlock currentBlock = fileCommunicator.getCurrentBlock();
        if (currentBlock.isEmpty()) {
            return new ArrayList<>(); //TODO THROW EXCEPTION WTF??
        }
        List<String> currBlockText = currentBlock.getTextLines();
        int leftEdgeIndex = Math.max(posOfKeyWord - valueOfPrevAndNextLines, 0);
        int rightEdgeIndex = Math.min(posOfKeyWord + valueOfPrevAndNextLines + 1, currBlockText.size()); // + 1 because of sublist exclusive last index

        return new ArrayList<>(currBlockText.subList(leftEdgeIndex, rightEdgeIndex));
    }

    private List<String> getTextFromNextBlock(int posOfKeyWord) {
        TextBlock nextBlock = fileCommunicator.getNextBlock();
        if (nextBlock.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> nextBlockText = nextBlock.getTextLines();
        int rightEdgeIndex = posOfKeyWord + valueOfPrevAndNextLines + 1 % THREAD_READ_LINES_VALUE;
        if (rightEdgeIndex > nextBlockText.size()) {
            return nextBlockText;
        }
        return new ArrayList<>(nextBlockText.subList(0, rightEdgeIndex));

    }


    private void workWithPartOfText() throws IOException {
        List<String> threadLines = fileCommunicator.getCurrentBlock().getThreadPartOfText(id);
        for (int i = 0; i < threadLines.size(); i++) {
            if (threadLines.get(i).contains(keyWord)) {
                createTextAroundKeyWordAndSend(i + id * THREAD_READ_LINES_VALUE);
            }
        }

    }
}
