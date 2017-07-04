package luxoft.console.application;

import luxoft.console.application.db.DBConnection;
import luxoft.console.application.db.update.InsertStatistic;
import luxoft.console.application.statistic.TextFile;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Scanner;

/**
 * Created by mac on 16.06.17.
 */
public class Main {
    public static void main(String[] args) {
        String fileOrDir;
        boolean valid = true;
        do {
            System.out.println("Operations: ");
            System.out.println("0 - file processing");
            System.out.println("1 - directory processing");
            System.out.println("\nChoice a number of operation, ");
            System.out.println("(or enter \'e\' to exit):");
            Scanner choice = new Scanner(System.in);
            fileOrDir = choice.next();

            switch (fileOrDir) {
                case "0":
                    fileEnter();
                    break;
                case "1":
                    dirFilesEnter();
                    break;
                case "e":
                    fileOrDir = "e";
                    break;
                default: {
                    System.out.println("You entered invalid value.");
                    System.out.println("Try one more.");
                    valid = false;
                }
            }
        }
        while (!valid && !fileOrDir.equals("e"));
    }

    private static void fileEnter() {
        /* Entering a path-file */
        String filePath;
        System.out.println("Enter a path of a file: ");
        Scanner sc = new Scanner(System.in);
        filePath = sc.next();

        /* Read text file and compute statistic for each
         * line in the one */
        TextFile file = TextFile.readFileAndProcess(new File(filePath));

        /* Open connection to the db */
        DBConnection dbConnection = new DBConnection();
        Connection conn = dbConnection.getConnection();

        /* Add file statistic to the db */
        new InsertStatistic(conn, file).insertFileStatistic();

        /* Closing connection to the db */
        dbConnection.closeConnection(conn);

        System.out.println("Statistic data have been successfully saved to the db!");
    }

    private static void dirFilesEnter() {
        /* Entering a path-directory */
        String dirPath;
        System.out.println("Enter a path of a directory: ");
        Scanner sc = new Scanner(System.in);
        dirPath = sc.next();

        if (new File(dirPath).isDirectory()) {
            /* Read files and compute statistic for each one */
            TextFile[] files = TextFile.folderFilesReadAndProcess(Paths.get(dirPath));

            /* Open connection to the db */
            DBConnection dbConnection = new DBConnection();
            Connection conn = dbConnection.getConnection();

            /* Add files statistic to the db */
            for (TextFile file : files)
                new InsertStatistic(conn, file).insertFileStatistic();

            /* Closing connection to the db */
            dbConnection.closeConnection(conn);

            System.out.println("Statistic data have been successfully saved to the db!");
        } else {
            System.err.println("You entered invalid path, it is not directory!");
        }
    }
}
