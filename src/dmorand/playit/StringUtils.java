package dmorand.playit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;

public final class StringUtils {
    private static final Pattern WORD_SEPARATOR_PATTERN = Pattern.compile("[ _,.-]+");
    private static final Map<String, String> WORD_SUBSTITUTIONS = new HashMap<String, String>();
    static {
        WORD_SUBSTITUTIONS.put("zéro", "0");
        WORD_SUBSTITUTIONS.put("un", "1");
        WORD_SUBSTITUTIONS.put("deux", "2");
        WORD_SUBSTITUTIONS.put("trois", "3");
        WORD_SUBSTITUTIONS.put("quatre", "4");
        WORD_SUBSTITUTIONS.put("cinq", "5");
        WORD_SUBSTITUTIONS.put("six", "6");
        WORD_SUBSTITUTIONS.put("sept", "7");
        WORD_SUBSTITUTIONS.put("huit", "8");
        WORD_SUBSTITUTIONS.put("neuf", "9");
        WORD_SUBSTITUTIONS.put("dix", "10");
    }

    public static Set<String> getWords(String string) {
        Set<String> words = new HashSet<String>();
        Splitter wordSplitter = Splitter.on(WORD_SEPARATOR_PATTERN).trimResults().omitEmptyStrings();
        for (String word : wordSplitter.split(string.toLowerCase())) {
            words.add(normalizeWord(word));
        }

        return words;
    }

    private static String normalizeWord(String word) {
        word = removeAccents(word);
        String substitution = WORD_SUBSTITUTIONS.get(word);
        return substitution == null ? word : substitution;
    }

    private static String removeAccents(String word) {
        return word.replace('é', 'e').replace('ê', 'e').replace('ê', 'e');
    }
}
