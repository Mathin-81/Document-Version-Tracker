package com.dvt.version_tracker.util;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Scanner;

public class ConsoleUI {
    // ANSI escape codes (works on most modern consoles)
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String CYAN = "\u001B[36m";

    private final Scanner scanner = new Scanner(System.in);

    public void printHeader() {
        System.out.println(CYAN + "========================================" + RESET);
        System.out.println(CYAN + "      DOCUMENT VERSION TRACKER (CLI)    " + RESET);
        System.out.println(CYAN + "========================================" + RESET);
    }

    public void printMenu() {
        System.out.println();
        System.out.println(BOLD + "1." + RESET + " Create new document");
        System.out.println(BOLD + "2." + RESET + " Edit document (create version)");
        System.out.println(BOLD + "3." + RESET + " View version history");
        System.out.println(BOLD + "4." + RESET + " Compare two versions");
        System.out.println(BOLD + "5." + RESET + " Revert to version");
        System.out.println(BOLD + "6." + RESET + " Search documents");
        System.out.println(BOLD + "7." + RESET + " Export version history to text");
        System.out.println(BOLD + "8." + RESET + " Delete document");
        System.out.println(BOLD + "9." + RESET + " Exit");
        System.out.print("\nChoose an option: ");
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ex) {
                System.out.println(RED + "Invalid number, try again." + RESET);
            }
        }
    }

    public void printSuccess(String msg) {
        System.out.println(GREEN + "✅ " + msg + RESET);
    }

    public void printError(String msg) {
        System.out.println(RED + "✖ " + msg + RESET);
    }

    public void printInfo(String msg) {
        System.out.println(YELLOW + msg + RESET);
    }

    public void printTimestamp() {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
