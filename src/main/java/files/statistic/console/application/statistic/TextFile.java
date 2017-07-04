package files.statistic.console.application.statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mac on 16.06.17.
 */
public class TextFile {
    private Long id;
    private String name;
    private Date dateOfStatisticComputation;
    private List<LineStatistic> lines;

    public TextFile() {
        /* Getting current date in time zone: "Africa/Cairo" */
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Africa/Cairo"));
        Calendar cal = df.getCalendar();
        cal.add(Calendar.HOUR, +3);
        cal.add(Calendar.YEAR, +80);
        this.dateOfStatisticComputation = cal.getTime();

        lines = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOfStatisticComputation() {
        return dateOfStatisticComputation;
    }

    public List<LineStatistic> getLines() {
        return lines;
    }

    @Override
    public String toString() {
        return "TextFile{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfStatisticComputation=" + dateOfStatisticComputation +
                '}';
    }

    /**
     * Firstly, this method reads a text file.
     * Secondly, it computes statistic of each line of a file
     * Thirdly, return a TextFile object with filled fields
     *
     * @return a TextFile object
     */
    public static TextFile readFileAndProcess(File filePath) {
        fileChecking(filePath); // file validation

        TextFile file = new TextFile();
        file.setName(filePath.getName());

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                file.lines.add(
                        LineStatistic.computeLineStatistic(sCurrentLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Reading and statistic calculation of each text file in directory and sub-directory
     *
     * @param directory a path to a directory
     * @return array of TextFile objects with lines statistic
     */
    public static TextFile[] folderFilesReadAndProcess(Path directory) {
        TextFile[] outFiles = null;
        int maxDepth = 5;
        try (Stream<Path> stream = Files.walk(directory, maxDepth)) {
            List<String> paths = stream
                    .map(String::valueOf)
                    .filter(path -> path.endsWith(".txt"))
                    .sorted()
                    .collect(Collectors.toList());

            System.out.println("Paths of text files:");
            for (String p : paths)
                System.out.println(p);

            /* Knew how many threads this system can support */
            int systemThreadsCount = Runtime.getRuntime().availableProcessors();
            System.out.println("\nThreads count in this system: " + systemThreadsCount);

            TextFile[] files = new TextFile[paths.size()];

            /* Each thread take some count of files which it will process
             * depends of count of threads that current system can support */
            int threadFiles = files.length / systemThreadsCount;
            int runningThreadsCount = (threadFiles < systemThreadsCount)? threadFiles : systemThreadsCount;
            int filesPerRunningThread = files.length / runningThreadsCount;
            int restThreads = files.length % runningThreadsCount;
            for (int j = 0; j < runningThreadsCount; j++) {
                int block = j;
                new Thread(() -> {
                    long start = System.currentTimeMillis();
                    for (int i = block * filesPerRunningThread;
                         (i < i + ((block == 0)? filesPerRunningThread + restThreads : filesPerRunningThread)) && (i < files.length); i++) {
                        files[i] = readFileAndProcess(new File(paths.get(i)));
                    }
                    System.out.println("Thread " + Thread.currentThread().getId()  + " run time: " + (System.currentTimeMillis() - start) + " ms");
                }).start();
            }
            outFiles = files;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outFiles;
    }

    /**
     * Checking a file
     *
     * @param file path to a text file
     */
    private static void fileChecking(File file) {
        String absPath = file.getAbsolutePath();
        if (!file.exists()) {
            System.err.println(absPath + " [The file is not exist!]");
        } else if (!file.canRead()) {
            System.err.println(absPath + "[Can't read the file!]");
        }
    }
}