package utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public final class CommonUtils {
	public static boolean isEmptyOrNull(String str) {
		return str == null || str.isEmpty();
	}
	
	
	public static String getOrderWithSuffix(int order) {
		String orderSuffix = null;
		
		switch (order % 10) {
			case 1:
				orderSuffix = "st";
				break;
			case 2:
				orderSuffix = "nd";
				break;
			case 3:
				orderSuffix = "rd";
				break;
			default:
				orderSuffix = "th";
				break;
		}
		
		return Integer.toString(order) + orderSuffix;
	}
	
	
	public static char getRandomBoundedChar(int min, int max) {
		return (char) (new Random().nextInt(max - min) + min);
	}
	
	
	/**
	 * Randomizes each character, which by default is a lower case letter in the English alphabet but may also be 
	 * an upper case letter or a number if desired, in a string of a specified length
	 * @param length the required number of characters in the randomized string
	 * @param includeUpperCase {@code true} if the randomized string is permitted to include upper case letters; {@code false} otherwise
	 * @param includeNumbers {@code true} if the randomized string is permitted to include Arabic numbers; {@code false} otherwise
	 * @return A random alphanumeric string 
	 */
	public static String getRandomString(int length, boolean includeUpperCase, boolean includeNumbers) {
		/*
			Could use an enum which may be arguably better in this situation...
			0 : lower case letter (ASCII 97 and 122 inclusive) 
			1 : upper case letter (ASCII 65 and 90 inclusive)
			2 : number (ASCII 48 to 57 inclusive)
		*/
		String source = "";
		List<Integer> charTypes = new ArrayList<Integer>();
		charTypes.add(0);
		if (includeUpperCase)  charTypes.add(1);
		if (includeNumbers)	   charTypes.add(2);

		char next = ' ';
		for (int i = 0; i < length; i++) {
			/* With Java 12...
			 char next = switch (charTypes.get(new Random().nextInt(charTypes.size()))) {
		 		case 0 -> CommonUtils.getRandomBoundedChar(97, 122);
		 		case 1 -> CommonUtils.getRandomBoundedChar(65, 90);
		 		case 2 -> CommonUtils.getRandomBoundedChar(48, 57);
		 		default -> ' ';
			 }
			 */
			switch (charTypes.get(new Random().nextInt(charTypes.size()))) {
				case 0:
					next = CommonUtils.getRandomBoundedChar(97, 122);
					break;
				case 1:
					next = CommonUtils.getRandomBoundedChar(65, 90);
					break;
				case 2:
					next = CommonUtils.getRandomBoundedChar(48, 57);
					break;
				default:
					// does nothing
			}
			source += next;
		}
		
		return source;
	}
	
	
	/**
	 * Removes all the elements in a collection that evaluates to {@code true} against a specific predicate and
	 * returns those elements in a single list 
	 * <p>
	 * Use this method over the {@code removeIf(Predicate)} method in the {@code Collection}
	 * interface if you need the list of removed elements
	 * @param <T> the type of each element in the collection
	 * @param collection a collection of some object whose elements that meet the specified predicate
	 * are to be removed
	 * @param removalCondition the elements in the collection that satisfy this predicate are removed
	 * @return A list of all the removed elements from collection
	 */
	public static <T> List<T> removeTargetedElements(Collection<T> collection, Predicate<T> removalCondition) {
		List<T> removedElements = new ArrayList<>();
		for (Iterator<T> it = collection.iterator(); it.hasNext();) {
			T currElement = it.next();
			if (removalCondition.test(currElement)) {
				removedElements.add(currElement);
				it.remove();
			}
		}
		
		return removedElements;
	}
	
	
	/**
	 * I don't know what exactly to call this method, but I doubt that "englishize" is an actual word...
	 * @param str the {@code string} object to be "englishized"
	 * @return the {@code str} argument with its first character capitalized and its subsequent character converted to lower case
	 */
	public static String englishizeWord(String str) {
		String formattedWord = "";
		
		formattedWord += Character.toUpperCase(str.charAt(0));
		for (int i = 1; i < str.length(); i++) {
			formattedWord += Character.toLowerCase(str.charAt(i));
		}
		
		return formattedWord;
	}
}
