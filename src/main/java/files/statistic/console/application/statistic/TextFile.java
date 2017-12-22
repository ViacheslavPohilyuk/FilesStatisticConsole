package files.statistic.console.application.statistic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mac on 16.06.17.
 */
public class TextFile {
    private Long id;
    private String name;
    private String dateOfStatisticComputation;
    private List<LineStatistic> lines = new ArrayList<>();

    public TextFile() {
        String currentDateTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.mm.yyyy kk:mm:ss"));
        setDateOfStatisticComputation(currentDateTime);
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

    public String getDateOfStatisticComputation() {
        return dateOfStatisticComputation;
    }

    public void setDateOfStatisticComputation(String dateOfStatisticComputation) {
        this.dateOfStatisticComputation = dateOfStatisticComputation;
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

            TextFile[] files = new TextFile[paths.size()];

            /* Knew how many threads this system can support */
            int systemThreadsCount = Runtime.getRuntime().availableProcessors();

            /* Each thread take some count of files which it will process
             * depends of count of threads that current system can support */
            int threadFiles = files.length / systemThreadsCount;

            /* We create additional thread if we can't distribute files between available threads evenly */
            int additionalThread = ((files.length % systemThreadsCount) > 0) ? 1 : 0;

            for (int j = 0; j < systemThreadsCount + additionalThread; j++) {
                int block = j;
                new Thread(() -> {
                    for (int i = block * threadFiles; (i < i + threadFiles) && (i < files.length); i++) {
                        files[i] = readFileAndProcess(new File(paths.get(i)));
                        System.out.println("File" + i + ": " + files[i].dateOfStatisticComputation);
                    }
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