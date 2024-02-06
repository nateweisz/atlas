package me.nateweisz.terminal;

import java.util.Scanner;
import java.io.InputStream;
import java.io.PrintStream;

public class Terminal {
	private final String prompt;
	private final InputStream inputStream;
    private final PrintStream printStream; // you can create a custom PrintStream to go somewhere else

	/**
	 * @param prompt The prompt that will be displayed on each new line. (Ex. "server>")
	 */
	public Terminal(String prompt) {
		this.prompt = prompt;
		this.inputStream = System.in;
        this.printStream = System.out;
	}

	public Terminal(String prompt, InputStream inputStream) {
		this.prompt = prompt;
		this.inputStream = inputStream;
        this.printStream = System.out;
	}

    public Terminal(String prompt, PrintStream printStream) {
        this.prompt = prompt;
        this.inputStream = System.in;
        this.printStream = printStream;
    }

    public Terminal(String prompt, InputStream inputStream, PrintStream printStream) {
        this.prompt = prompt;
        this.inputStream = inputStream;
        this.printStream = printStream;
    }

	/**
	 * Calling this will block the current thread so make sure you are polling from a seperate thread.
	 */
	public void poll() {
		// start a new thread and poll for a string input.
		Scanner input = new Scanner(this.inputStream);
		printStream.println(); // TODO: make a cool ascii header and throw it here.
		
		while (true) {
			printStream.print(this.prompt + " ");
			String command = input.nextLine(); // blocks the thread until an input is entered.

            // ignore empty inputs
            if (command.isEmpty()) continue;

            try {
                if (command.equals("test-exception")) {
                    throw new RuntimeException("Testing exceptions!");
                }
            } catch (Exception e) {
                // this works as of 2/6/2024 but could be cleaned up more
                printStream.println("|  Error:");
                printStream.println("|");
                printStream.println("|  " + e);

                for (StackTraceElement element : e.getStackTrace()) {
                    printStream.println("|  at " + element);
                }
            }
		}
	}
}