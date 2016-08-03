
package co.timecrypt.utils;

/**
 * Some quick helper methods for Strings.
 */
public class TextUtils {

    /**
     * Checks whether the given String is empty. Spaces prefixing and suffixing the text will be removed prior to check.
     * 
     * @param text String to test
     * @return {@code True} if text is empty or {@code null}, {@code false} otherwise
     */
    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    /**
     * Tests if <b>all</b> of the given Strings are empty using {@link #isEmpty(String)}.
     * 
     * @param texts Strings to test
     * @return {@code True} if <b>all</b> texts are empty or {@code null}, {@code false} if any is non-empty
     */
    public static boolean areEmpty(String... texts) {
        // noinspection ForLoopReplaceableByForEach - mine is quicker
        for (int i = 0; i < texts.length; i++) {
            if (!isEmpty(texts[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests if <b>any</b> of the given Strings are empty using {@link #isEmpty(String)}.
     *
     * @param texts Strings to test
     * @return {@code True} if <b>any</b> text is empty or {@code null}, {@code false} if all are non-empty
     */
    public static boolean isAnyEmpty(String... texts) {
        // noinspection ForLoopReplaceableByForEach - mine is quicker
        for (int i = 0; i < texts.length; i++) {
            if (isEmpty(texts[i])) {
                return true;
            }
        }
        return false;
    }

}
