package sg.edu.nus.comp.cs3219.ui;

import java.io.*;
import java.util.List;
import java.util.Set;

import javax.swing.JTextArea;

import sg.edu.nus.comp.cs3219.control.MasterControl;

public class UiController {
	public interface KwicUi {
		/**
		 * Get the input String lists
		 */
		List<String> getInput();

		/**
		 * Get the input strings as an String array
		 */
		String[] getInputArray();

		/**
		 * Set the UI for input words
		 */
		void setInputWords(String inputWords);

		/**
		 * Get the output text area in the UI
		 */
		JTextArea getOutputTextArea();

		/**
		 * Get the "words to ignore" set
		 */
		Set<String> getIgnoredWords();

		/**
		 * Set the UI for "words to ignore"
		 */
		void setIgnoredWords(String ignoreWords);

		/**
		 * Get the "required words" set
		 */
		Set<String> getRequiredWords();

		/**
		 * Set the UI for "required words"
		 */
		void setRequiredWords(String requiredWords);

		/**
		 * Set the generated KWIC results in the UI
		 */
		void setResults(List<String> results);

		/**
		 * Set the UI Controller
		 */
		void setController(UiController controller);
	}

	final private KwicUi view;

	private MasterControl controller;

	public UiController(KwicUi view) {
		this.view = view;
		controller = new MasterControl();
	}

	public void readFile(String fileName) {
		StringBuilder inputWords = new StringBuilder();
		StringBuilder ignoreWords = new StringBuilder();
		StringBuilder requiredWords = new StringBuilder();

		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String[] ignoreWordsArr = br.readLine().split(",(\\s+)?");
			String[] requiredWordsArr = br.readLine().split(",(\\s+)?");
			String line = br.readLine();

			for (String ignoreWord: ignoreWordsArr) {
				ignoreWords.append(ignoreWord)
							.append("\n");
			}
			for (String requiredWord: requiredWordsArr) {
				requiredWords.append(requiredWord)
						.append("\n");
			}

			while (line != null) {
				inputWords.append(line);
				inputWords.append("\n");
				line = br.readLine();
			}

			view.setIgnoredWords(ignoreWords.toString());
			view.setRequiredWords(requiredWords.toString());
			view.setInputWords(inputWords.toString());
			generateResult();

		} catch (FileNotFoundException e) {
			System.out.format("file:%s not found",fileName);
			return;
		} catch (IOException e) {
			System.out.format("file:%s not readable",fileName);
			return;
		}
	}

	public void generateResult() {
		// Get entered ignored words from GUI
		Set<String> ignoredWordsSet = view.getIgnoredWords();
		// Get entered required words from GUI
		Set<String> requiredWordsSet = view.getRequiredWords();
		// Run the application
		List<String> result = controller.run(view.getInput(), ignoredWordsSet, requiredWordsSet);
		// Display result
		view.setResults(result);
	}

	public void exportResultToFile(String data) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("./output.txt"));
			view.getOutputTextArea().write(writer);
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}
	}
}
