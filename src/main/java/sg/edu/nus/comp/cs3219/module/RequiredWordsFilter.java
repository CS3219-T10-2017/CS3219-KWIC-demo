package sg.edu.nus.comp.cs3219.module;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.function.Predicate;

import sg.edu.nus.comp.cs3219.event.LineStorageChangeEvent;
import sg.edu.nus.comp.cs3219.model.Line;
import sg.edu.nus.comp.cs3219.model.LineStorage;

public class RequiredWordsFilter implements Observer {
	final private LineStorage resultStorage;
	private Set<String> requiredWords = new HashSet<>();

	public RequiredWordsFilter(LineStorage storage) {
		resultStorage = storage;
	}

	public void setRequiredWords(Set<String> requiredWordSet) {
		requiredWords = requiredWordSet;
	}

	@Override
	public void update(Observable o, Object arg) {
		LineStorage storage = (LineStorage) o;
		LineStorageChangeEvent event = (LineStorageChangeEvent) arg;
		switch (event.getEventType()) {
		case ADD:
			Line line = storage.get(event.getChangedLine());
			filterRequiredWords(line);
			break;
		default:
			break;
		}
	}

	
	private void filterRequiredWords(Line line) {
		if (requiredWords.isEmpty()) {
			resultStorage.addLine(line.toString());
			return;
		}

		// Add word if starting word is in required word
		if (isRequiredWords(line.getWord(0))) {
			System.out.println(line.toString());
			resultStorage.addLine(line.toString());
		}
	}

	private boolean isRequiredWords(String word) {
        // set up predicate for requireWords stream
        Predicate<String> equalsIgnoreCase = e -> e.equalsIgnoreCase(word);

        // if any of the requiredWords matches the word, return true; otherwise false
        return requiredWords.stream().anyMatch(equalsIgnoreCase);
	}
}
