package TextHelpers;

import java.util.ArrayList;
import java.util.List;

import static Settings.CONSTANTS.THREAD_READ_LINES_VALUE;

public class TextBlock {
    private List<String> textLines;
    private int valueOfBlocks;

    public List<String> getThreadPartOfText(int partValue) {
        //TODO: check if end array position is less than we want to take
        return new ArrayList<>(textLines.subList(partValue * THREAD_READ_LINES_VALUE, (partValue + 1) * THREAD_READ_LINES_VALUE));
    }

    public void setTextLines(List<String> textLines) {
        this.textLines = textLines;
    }

}
