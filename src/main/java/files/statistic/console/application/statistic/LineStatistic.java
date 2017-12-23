package files.statistic.console.application.statistic;

/**
 * Created by mac on 16.06.17.
 */
public class LineStatistic {
    private Long id;
    private String longestWord = "";
    private String shortestWord = "";
    private int lineLength;
    private int avgWordLength;

    public LineStatistic() {
    }

    public LineStatistic(String longestWord, String shortestWord, int lineLength, int avgWordLength) {
        this.longestWord = longestWord;
        this.shortestWord = shortestWord;
        this.lineLength = lineLength;
        this.avgWordLength = avgWordLength;
    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LineStatistic that = (LineStatistic) o;

        if (lineLength != that.lineLength) return false;
        if (avgWordLength != that.avgWordLength) return false;
        if (longestWord != null ? !longestWord.equals(that.longestWord) : that.longestWord != null) return false;
        return shortestWord != null ? shortestWord.equals(that.shortestWord) : that.shortestWord == null;
    }

    @Override
    public int hashCode() {
        int result = longestWord != null ? longestWord.hashCode() : 0;
        result = 31 * result + (shortestWord != null ? shortestWord.hashCode() : 0);
        result = 31 * result + lineLength;
        result = 31 * result + avgWordLength;
        return result;
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
        int lineLength = line.length();

        /* Splitting a line by one or more spaces */
        String[] wordsArr = line.trim().split(" +");

        int wordsInLineCount = wordsArr.length;

        /* Index of longest and shortest words in the massive {@code wordsArray} */
        int indexMaxWordLen = 0;
        int indexMinWordLen = 0;

        int lenAllWords = 0;
        if (wordsInLineCount == 0) {
            return new LineStatistic();
        }

        /* Length of longest and shortest words */
        int maxWordLen = wordsArr[0].length();
        int minWordLen = wordsArr[0].length();

        int j = 0;
        for (String word : wordsArr) {
            if (maxWordLen < word.length()) {
                maxWordLen = word.length();
                indexMaxWordLen = j;
            }
            if (minWordLen > word.length()) {
                minWordLen = word.length();
                indexMinWordLen = j;
            }
            lenAllWords += word.length();
            j++;
        }

        String longestWord = wordsArr[indexMaxWordLen];
        String shortestWord = wordsArr[indexMinWordLen];
        int avgWordLen = lenAllWords / wordsInLineCount;
        return new LineStatistic(longestWord, shortestWord, lineLength, avgWordLen);
    }
}
