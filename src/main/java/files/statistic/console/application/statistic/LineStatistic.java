package files.statistic.console.application.statistic;

/**
 * Created by mac on 16.06.17.
 */
public class LineStatistic {
    private Long id;
    private String longestWord = "";
    private String shortestWord = "";
    private int lineLength;
    private int avgWordLength = 0;

    public String getLongestWord() {
        return longestWord;
    }

    public void setLongestWord(String longestWord) {
        this.longestWord = longestWord;
    }

    public String getShortestWord() {
        return shortestWord;
    }

    public void setShortestWord(String shortestWord) {
        this.shortestWord = shortestWord;
    }

    public int getLineLength() {
        return lineLength;
    }

    public void setLineLength(int lineLength) {
        this.lineLength = lineLength;
    }

    public int getAvgWordLength() {
        return avgWordLength;
    }

    public void setAvgWordLength(int avgWordLength) {
        this.avgWordLength = avgWordLength;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LineStatistic{" +
                "longestWord='" + longestWord + '\'' +
                ", shortestWord='" + shortestWord + '\'' +
                ", lineLength=" + lineLength +
                ", avgWordLength=" + avgWordLength +
                '}';
    }

    /**
     * This method computes statistic of a line
     * <p>
     * This statistic consist:
     * - longest word in a line
     * - shortest word in a line
     * - length of a line
     * - average word length in a line
     *
     * @return an LineStatistic object with statistic of a current line
     */
    public static LineStatistic computeLineStatistic(String line) {
        LineStatistic lineStatistic = new LineStatistic();
        int lineLength = line.length();

        /* Removing redundant space from a line and splitting it by spaces */
        String[] wordsArray = line.replaceAll(" +", " ")
                .trim()
                .split(" ");

        /* Count of words in a current line */
        int wordsLineCount = wordsArray.length;

        /* Index of longest and shortest words in the massive {@code wordsArray} */
        int indexMaxLenWord = 0;
        int indexMinLenWord = 0;

        /* Sum of letters of the all words in a line */
        int lengthOfAllLineWords = 0;

        /* String objects for storing longest and shortest words */
        String longestWord = "";
        String shortestWord = "";

        /* a valuable for storing average count of letters of each line */
        int avgWordLength = 0;

        if (wordsLineCount != 0) {
            int j = 0;

            /* Length of longest and shortest words */
            int MaxLenWordLetterCount = wordsArray[0].length();
            int MinLenWordLetterCount = wordsArray[0].length();

            /* Each word of a line processing */
            for (String word : wordsArray) {
                word = word.trim();

                /* Finding a longest word in a line */
                if (MaxLenWordLetterCount < word.length()) {
                    MaxLenWordLetterCount = word.length();
                    indexMaxLenWord = j;
                }

                /* Finding a shortest word in a line */
                if (MinLenWordLetterCount > word.length()) {
                    MinLenWordLetterCount = word.length();
                    indexMinLenWord = j;
                }
                lengthOfAllLineWords += word.length();
                j++;
            }
            longestWord = wordsArray[indexMaxLenWord];
            shortestWord = wordsArray[indexMinLenWord];
            avgWordLength = lengthOfAllLineWords / wordsLineCount;
        }

        /* Saving data of statistic of a current line
         * to a LineStatistic object */
        lineStatistic.setLongestWord(longestWord);
        lineStatistic.setShortestWord(shortestWord);
        lineStatistic.setAvgWordLength(avgWordLength);
        lineStatistic.setLineLength(lineLength);

        return lineStatistic;
    }
}