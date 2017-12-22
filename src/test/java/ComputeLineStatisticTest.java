import files.statistic.console.application.statistic.LineStatistic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static files.statistic.console.application.statistic.LineStatistic.computeLineStatistic;

public class ComputeLineStatisticTest {
    @Test
    public void testRegularLines() throws Exception {
        String test = "JavaScript is a multi-paradigm, dynamic language with types and operators, standard built-in objects, and methods.";
        assertEquals(computeLineStatistic(test), new LineStatistic("multi-paradigm,", "a", 114, 6));
    }

    @Test
    public void testLinesWithSeveralSpacesGoOneByOne() throws Exception {
        String test = " A            re-introduction  to   JavaScript    (JS tutorial)";
        assertEquals(computeLineStatistic(test), new LineStatistic("re-introduction", "A", 63, 6));
    }

    @Test
    public void testEmptyLines() throws Exception {
        assertEquals(computeLineStatistic(""), new LineStatistic("", "", 0, 0));

    }
}