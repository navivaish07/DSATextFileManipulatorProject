import java.io.*;
import java.util.*;
import java.util.LinkedList;

class TextFileManipulator {
    static Scanner sc = new Scanner(System.in);
    static File file;
    static LinkedList<String> contentList = new LinkedList<>();
    static Stack<String> clipboard = new Stack<>();

    public static void main(String[] args) throws IOException {
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                 ************************  T E X T   F I L E   M A N I P U L A T O R  ************************                                                 ");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");

        System.out.println("Enter File Name (without extension) to manipulate: ");
        String fileName = sc.next();
        file = new File(fileName + ".txt");

        if (!file.exists()) {
            System.out.println("Error: File does not exist! Please ensure the file is in the correct location.");
            return;
        }

        
        
        try {
            loadFileContent();
        } catch (IOException e) {
            System.out.println("Error: Could not read from the file. Please check the file permissions.");
            return;
        }

        int option;
        do {
            displayMenu();
            option = sc.nextInt();
            switch (option) {
                case 1:
                    eliminateRepeatedLines();
                    break;
                case 2:
                    reverseContent();
                    break;
                case 3:
                    addNewLine();
                    break;
                case 4:
                    copyText();
                    break;
                case 5:
                    pasteText();
                    break;
                case 6:
                    cutText();
                    break;
                case 7:
                    sortContent();
                    break;
                case 8:
                    mergeContent();
                    break;
                case 9:
                    System.out.println("Exiting program. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option! Please choose again.");
                    break;
            }
        } while (option != 9);
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Eliminate repeated lines");
        System.out.println("2. Reverse content");
        System.out.println("3. Add new line");
        System.out.println("4. Copy text");
        System.out.println("5. Paste text");
        System.out.println("6. Cut text");
        System.out.println("7. Sort content");
        System.out.println("8. Merge content with another file");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void loadFileContent() throws IOException {
        contentList.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentList.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + file.getAbsolutePath());
            throw new IOException("File not found", e);
        } catch (IOException e) {
            System.out.println("Error reading the file: " + file.getAbsolutePath());
            throw e;
        }
    }

    private static void saveToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : contentList) {
                writer.write(line);
                writer.newLine();
            }
        }
        System.out.println("File saved successfully.");
    }

    private static void eliminateRepeatedLines() throws IOException {
        Set<String> uniqueLines = new LinkedHashSet<>(contentList);
        contentList.clear();
        contentList.addAll(uniqueLines);
        saveToFile();
    }

    private static void reverseContent() throws IOException {
        Collections.reverse(contentList);
        saveToFile();
    }

    private static void addNewLine() throws IOException {
        System.out.println("Enter the line to add:");
        sc.nextLine();  // Consume newline left-over
        String newLine = sc.nextLine();
        System.out.println("Enter the line number to insert the new line after:");
        int lineNumber = sc.nextInt();

        if (lineNumber > 0 && lineNumber <= contentList.size()) {
            contentList.add(lineNumber, newLine);
            saveToFile();
        } else {
            System.out.println("Invalid line number.");
        }
    }

    private static void copyText() {
        System.out.println("Enter the line number to copy:");
        int lineNumber = sc.nextInt();

        if (lineNumber > 0 && lineNumber <= contentList.size()) {
            clipboard.push(contentList.get(lineNumber - 1));
            System.out.println("Text copied to clipboard.");
        } else {
            System.out.println("Invalid line number.");
        }
    }

    private static void pasteText() throws IOException {
        if (clipboard.isEmpty()) {
            System.out.println("Clipboard is empty!");
            return;
        }

        System.out.println("Enter the line number to paste after:");
        int lineNumber = sc.nextInt();

        if (lineNumber > 0 && lineNumber <= contentList.size()) {
            contentList.add(lineNumber, clipboard.peek());
            saveToFile();
        } else {
            System.out.println("Invalid line number.");
        }
    }

    private static void cutText() throws IOException {
        System.out.println("Enter the line number to cut:");
        int lineNumber = sc.nextInt();

        if (lineNumber > 0 && lineNumber <= contentList.size()) {
            clipboard.push(contentList.get(lineNumber - 1));
            contentList.remove(lineNumber - 1);
            saveToFile();
            System.out.println("Text cut and saved to clipboard.");
        } else {
            System.out.println("Invalid line number.");
        }
    }

    private static void sortContent() throws IOException {
        Collections.sort(contentList);
        saveToFile();
    }

    private static void mergeContent() throws IOException {
        System.out.println("Enter the name of the file to merge with (without extension):");
        String mergeFileName = sc.next();
        File mergeFile = new File(mergeFileName + ".txt");

        if (!mergeFile.exists()) {
            System.out.println("The specified file does not exist.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(mergeFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentList.add(line);
            }
        }

        saveToFile();
        System.out.println("Files merged successfully.");
    }
}
