package sg.edu.nus.comp.cs3219.module;

import org.junit.Before;
import org.junit.Test;
import sg.edu.nus.comp.cs3219.model.LineStorage;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class RequiredWordsFilterTest {
	LineStorage inputLineStorage;
	LineStorage afterFilterStorage;
	RequiredWordsFilter requiredWordsFilter;

	@Before
	public void setUp() {
		inputLineStorage = new LineStorage();
		afterFilterStorage = new LineStorage();
		requiredWordsFilter = new RequiredWordsFilter(afterFilterStorage);
		Set<String> words = new HashSet<>();
		words.add("Space");
		words.add("Star");
		requiredWordsFilter.setRequiredWords(words);
		inputLineStorage.addObserver(requiredWordsFilter);
	}

	@Test
	public void test() {
		inputLineStorage.addLine("The Day after Tomorrow");
		inputLineStorage.addLine("Star Trek");
		inputLineStorage.addLine("Fast and Furious");
		inputLineStorage.addLine("2001: a Space Odyssey");
		inputLineStorage.addLine("Man of Steel");

		assertEquals(2, afterFilterStorage.size());

		assertEquals("Space Odyssey 2001: a", afterFilterStorage.get(0).toString());
		assertEquals("Star Trek", afterFilterStorage.get(1).toString());
	}
}
