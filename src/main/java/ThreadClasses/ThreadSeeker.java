package ThreadClasses;

import FileWorkers.FileCommunicator;
import TextHelpers.TextBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static Settings.CONSTANTS.*;


public class ThreadSeeker implements Runnable {

    private static int threadID = 0;
    private final int valueOfPrevAndNextLines;
    private final FileCommunicator fileCommunicator;
    private final int id;
    private final Thread thread;
    private final String keyWord;
    private final CyclicBarrier cyclicBarrier;
    private int alreadyCheckedBlocks = 0;
    private int currentStartPosInBlock = 0;

    public ThreadSeeker(int valueOfPrevAndNextLines, FileCommunicator fileCommunicator, String keyWord, CyclicBarrier cyclicBarrier) throws InterruptedException {
        this.keyWord = keyWord;
        this.valueOfPrevAndNextLines = valueOfPrevAndNextLines;
        this.fileCommunicator = fileCommunicator;
        id = threadID++;
        thread = new Thread(this,"Hobbit" + id);
        recountCurrentStartPosInBlock();
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        try {
            while (!fileCommunicator.getCurrentBlock().isEmpty()) {
                while (currentStartPosInBlock < VALUE_OF_LINES_IN_BLOCK) {
                    workWithPartOfText();
                    updateThreadReadingInfo(alreadyCheckedBlocks + 1);
                }
                updateThreadReadingInfo(0);
                cyclicBarrier.await();

            }
        } catch (IOException | BrokenBarrierException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void recountCurrentStartPosInBlock() {
        currentStartPosInBlock = (id + VALUE_OF_THREADS * alreadyCheckedBlocks) * THREAD_READ_LINES_VALUE;
    }

    private void updateThreadReadingInfo(int newCheckedBlocksValue) {
        alreadyCheckedBlocks = newCheckedBlocksValue;
        recountCurrentStartPosInBlock();
    }

    private void createTextAroundKeyWordAndSend(int posOfKeyWord){
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
        fileCommunicator.insertTextInQueueForOutput(outputText, posOfKeyWord);
    }


    private List<String> getTextFromPrevBlock(int posOfKeyWord) {
        TextBlock prevBlock = fileCommunicator.getPrevBlock();
        if (prevBlock.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> prevBlockText = prevBlock.getTextLines();

        int leftEdgeIndex = prevBlockText.size() - (valueOfPrevAndNextLines - posOfKeyWord);

        if (leftEdgeIndex < 0) {
            return prevBlockText;
        }
        return new ArrayList<>(prevBlockText.subList(leftEdgeIndex, prevBlockText.size()));
    }

    private List<String> getTextFromCurrentBlock(int posOfKeyWord) {
        TextBlock currentBlock = fileCommunicator.getCurrentBlock();

        List<String> currBlockText = currentBlock.getTextLines();
        int leftEdgeIndex = Math.max(posOfKeyWord - valueOfPrevAndNextLines, 0);
        int rightEdgeIndex = Math.min(posOfKeyWord + valueOfPrevAndNextLines + 1, currBlockText.size()); // + 1 because of sublist Exclusive last index

        return new ArrayList<>(currBlockText.subList(leftEdgeIndex, rightEdgeIndex));
    }

    private List<String> getTextFromNextBlock(int posOfKeyWord) {
        TextBlock nextBlock = fileCommunicator.getNextBlock();
        if (nextBlock.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> nextBlockText = nextBlock.getTextLines();
        int rightEdgeIndex = (posOfKeyWord + valueOfPrevAndNextLines + 1) % VALUE_OF_LINES_IN_BLOCK;
        if (rightEdgeIndex > nextBlockText.size()) {
            return nextBlockText;
        }
        return new ArrayList<>(nextBlockText.subList(0, rightEdgeIndex));

    }

    private void workWithPartOfText() throws IOException {
        List<String> threadLines = fileCommunicator.getCurrentBlock().getThreadPartOfText(id + VALUE_OF_THREADS * alreadyCheckedBlocks);
        for (int i = 0; i < threadLines.size(); i++) {
            if (threadLines.get(i).contains(keyWord)) {
                createTextAroundKeyWordAndSend(i + currentStartPosInBlock);
            }
        }

    }
}
