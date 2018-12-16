package ir.component.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.ibm.icu.text.Normalizer2;

/**
 * @author Mohammad Yasin Kaji
 */
public class UnicodeNormalizer {
    static final Pattern DIGIT_0 = Pattern.compile("[٠۰߀०০੦૦୦௦౦೦൦๐໐０]");
    static final Pattern DIGIT_1 = Pattern.compile("[١۱߁१১੧૧୧௧౧೧൧๑໑１]");
    static final Pattern DIGIT_2 = Pattern.compile("[٢۲߂२২੨૨୨௨౨೨൨๒໒２]");
    static final Pattern DIGIT_3 = Pattern.compile("[٣۳߃३৩੩૩୩௩౩೩൩๓໓３]");
    static final Pattern DIGIT_4 = Pattern.compile("[٤۴߄४৪੪૪୪௪౪೪൪๔໔４]");
    static final Pattern DIGIT_5 = Pattern.compile("[٥۵߅५৫੫૫୫௫౫೫൫๕໕５]");
    static final Pattern DIGIT_6 = Pattern.compile("[٦۶߆६৬੬૬୬௬౬೬൬๖໖６]");
    static final Pattern DIGIT_7 = Pattern.compile("[٧۷߇७৭੭૭୭௭౭೭൭๗໗７]");
    static final Pattern DIGIT_8 = Pattern.compile("[٨۸߈८৮੮૮୮௮౮೮൮๘໘８]");
    static final Pattern DIGIT_9 = Pattern.compile("[٩۹߉९৯੯૯୯௯౯೯൯๙໙９��]");

    public static final Pattern[] DIGIT_PATTERN_LIST = { DIGIT_0, DIGIT_1, DIGIT_2, DIGIT_3, DIGIT_4, DIGIT_5, DIGIT_6, DIGIT_7, DIGIT_8,
            DIGIT_9 };

    static final Pattern PERSIAN_LETTERS = Pattern.compile("[\\u0621-\\u076d]");

    static final Pattern PERSIAN_YEH = Pattern.compile("[\u0649\u064a]");
    // static final Pattern JOINT_HAMZA_ALEF = Pattern.compile("(" + PERSIAN_LETTERS + ")[أ](" + PERSIAN_LETTERS + ")"); // do not use: causes issue for رأی
    static final Pattern PERSIAN_ALEF = Pattern.compile("[\u0623\u0625\u0671-\u0673]");
    static final Pattern PERSIAN_SMALL_YEH = Pattern.compile("[ةۀهٔ]");

    public static final String PERSIAN_BACK_JOINT_CHARS = "آادذرزژوؤ";
    static final Pattern PERSIAN_EXTRA_ZWNJ = Pattern.compile("\u200c{2,}");
    static final Pattern PERSIAN_EXTRA_ZWNJ2 = Pattern.compile("([" + PERSIAN_BACK_JOINT_CHARS + "])\u200c+");

    final static Normalizer2 NORMALIZER = Normalizer2.getInstance(null, "nfkc_cf", Normalizer2.Mode.DECOMPOSE);

    /**
     * Persian normalization procedure:
     * <ul>
     * <li>Replaces all different shapes of YEH and KAF with their Persian equivalents;</li>
     * <li>Replaces ة, ۀ, and هٔ with HEH;</li>
     * <li>Removes extra ZWNJ</li>
     * <li>Removes ZWNJ come after back-joint Persian letter. For example ZWNJ after د is removed</li>
     * </ul>
     */
    public static String normalizePersian(String str) {
        if (str == null) {
            return null;
        }
        str = normalizeNewsTextPersian(str);

        // no need for alef normalization, normalize() method does the job
        // str = PERSIAN_ALEF.matcher(str).replaceAll("\u0627"); // alef
        str = PERSIAN_SMALL_YEH.matcher(str).replaceAll("ه"); // all heh types => heh
        return str;
    }

    /**
     * <ul>
     * <li>Replaces all different shapes of YEH and KAF with their Persian equivalents;</li>
     * <li>Removes extra ZWNJ</li>
     * <li>Removes ZWNJ come after back-joint Persian letter. For example ZWNJ after د is removed</li>
     * </ul>
     * 
     * @param str
     * @return
     */
    public static String normalizeNewsTextPersian(String str) {
        str = PERSIAN_YEH.matcher(str).replaceAll("\u06cc"); // yeh
        str = str.replace('\u0643', '\u06a9'); // kaf
        str = PERSIAN_EXTRA_ZWNJ.matcher(str).replaceAll("\u200c"); // remove extra zwnj
        str = PERSIAN_EXTRA_ZWNJ2.matcher(str).replaceAll("$1"); // zwnj not required after back-joint Persian letters
        return str;
    }

    public static String normalizeNewsTextGeneric(String str) {
        return str;
    }

    public static String normalizeNewsText(String lang, String str) {
        // str = normalizeNewsTextGeneric(str);
        if ("fa".equals(lang)) {
            str = normalizeNewsTextPersian(str);
        }
        return str;
    }

    /**
     * Normalizes unicode strings which are added to the end of URLs. Replaces whitespaces with dash.
     * 
     * @param str
     * @return
     */
    public static String normalizeUrl(String str) {
        // first remove tatweel and diacritics and then change other non-letters to: -
        // http://www.fileformat.info/info/unicode/category/Lm/list.htm
        // http://www.fileformat.info/info/unicode/category/Mn/list.htm
        str = str.replaceAll("[\\p{Lm}\\p{Mn}]+", "").replaceAll("[^\\p{Nd}\\p{Lo}\\p{Lu}\\p{Ll}]+", "-");

        // remove extra -
        str = str.replaceAll("-{2,}", "-");

        if (str.endsWith("-") && str.length() > 1) {
            str = str.substring(0, str.length() - 1);
        }
        if (str.startsWith("-") && str.length() > 1) {
            str = str.substring(1);
        }
        return str;
    }

    /**
     * @param str
     * @return
     */
    public static String normalizeFileName(String str) {
        if (str == null) {
            return null;
        }
        // first remove tatweel and diacritics and then change other non-letters to: -
        // http://www.fileformat.info/info/unicode/category/Lm/list.htm
        // http://www.fileformat.info/info/unicode/category/Mn/list.htm
        str = str.replaceAll("[\\p{Lm}\\p{Mn}]+", "").replaceAll("[^.-_\\p{Nd}\\p{Lo}\\p{Lu}\\p{Ll}]+", "-");

        if (str.endsWith("-") && str.length() > 1) {
            str = str.substring(0, str.length() - 1);
        }
        if (str.startsWith("-") && str.length() > 1) {
            str = str.substring(1);
        }
        return str;
    }

    /**
     * Converts any Unicode digits into their ASCII equivalent. For example given 23۹٤۴ returns 23944
     * 
     * @param str
     * @return
     */
    public static String normalizeUnicodeDigits(String str) {
        for (int i = 0; i < DIGIT_PATTERN_LIST.length; i++) {
            Pattern dp = DIGIT_PATTERN_LIST[i];
            str = dp.matcher(str).replaceAll(String.valueOf(i));
        }
        return str;
    }

    /**
     * Normalizes a string in these ways:
     * <ul>
     * <li>removes any accent or diacritics or spaces (and marks including ZWJ and ZWNJ)</li>
     * <li>makes it lowercase (if casing is meaningful for a character)</li>
     * <li>converts all kinds of Unicode digits to ASCII</li>
     * </ul>
     * 
     * @param str
     * @return normalized string or null if str is null.
     */
    public static String normalize(String str) {
        if (str == null) {
            return null;
        }

        String normalized = NORMALIZER.normalize(str);
        normalized = normalized.replaceAll("[^\\p{Nd}\\p{Lo}\\p{Lu}\\p{Ll}]+", ""); // remove diacritics and any form of spaces
        return normalizeUnicodeDigits(normalized);
    }

    public static String normalizePersianNewsTextForNer(String str) {
        if (str == null) {
            return null;
        }

        // String normalized = NORMALIZER.normalize(str);
        String normalized = str.replaceAll("[\\p{Pd}\\u0640]+", "-"); // normalize dashes and arabic tatweel

        // \\p{Pc}\\p{Pd}\\p{Pe}\\p{Pf}\\p{Pi}\\p{Po}\\p{Ps}
        normalized = normalized.replaceAll("[^\\p{N}\\p{L} \\u200c\\u0654\\p{P}]+", ""); // remove diacritics and any form of spaces
        normalized = normalizeNewsTextPersian(normalized);

        // normalized = JOINT_HAMZA_ALEF.matcher(normalized).replaceAll("$1ئ$2"); // alef
        normalized = PERSIAN_ALEF.matcher(normalized).replaceAll("\u0627"); // alef
        normalized = StringUtils.replace(normalized, "ة", "ه");
        normalized = StringUtils.replace(normalized, "ۀ", "هٔ");
        // PERSIAN_SMALL_YEH.matcher(normalized).replaceAll("ه"); // all heh types => heh

        normalized = normalizeUnicodeDigits(normalized);

        normalized = normalized.replaceAll("[\n\r]+", "\n").replaceAll("\\p{Zs}+", " ")
                .replaceAll("([^.:!?؟]+)"
                        + "([.:!?؟]+[\\p{Po}\\p{Ps}]?)\\s*([\\p{L}\\p{Nd}\\p{Po}\\p{Pe}])",
                        "$1$2\n$3");

        return normalized;
    }

    public static String normalize(String lang, String str) {
        str = normalize(str);
        if ("fa".equals(lang)) {
            str = normalizePersian(str);
        }

        return str;
    }

    public static void main2(String[] args) {
        String normalized = normalize("ΜΆΪΟΣ schöN ۱۲_32 ه‍ می‌رِوَمِم");
        System.out.println(NORMALIZER.normalize("آمده‌ام"));
        System.out.println(normalized);
        System.out.println(normalize("آمده‌ام خانهٔ أمارت").equals(normalize("fa", "آمده‌ام خانهٔ أمارت")));
        System.out.println(normalizePersianNewsTextForNer("خانهٔ لانة کاشانۀ رئیس"));
        System.out.println(normalizePersianNewsTextForNer("آمده‌ام خانهٔ جرأتِ هيئت أمارت"));
        System.out.println(normalize("رئیس"));
    }

    public static void main(String[] args) throws Exception {
        // nerDataProvider(args);
        normalizeSdtIn();
    }

    private static void normalizeSdtIn() throws UnsupportedEncodingException, IOException {
        BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = systemIn.readLine()) != null) {
            sb.append(line).append('\n');
        }
        System.out.printf("\n\n\nNormalized output:\n%s", normalizePersianNewsTextForNer(sb.toString()));
    }

    public static void nerDataProvider(String[] args) throws Exception {
        File parent = new File("C:/Users/yasin/OneDrive/nastooh/nlp/ner/200k-phase2");
        // String[] files = { "politics", "economics", "society", "world", "culture", "sci-tech", "sports", "local", "lifestyle" };
        String[] files = { "lifestyle", };
        for (int i = 0; i < files.length; i++) {
            File f = new File(parent, files[i] + ".txt");
            File outDir = new File(parent, "normal-" + files[i]);
            outDir.mkdirs();

            FileReader fr = new FileReader(f);
            StringWriter sw = new StringWriter();
            IOUtils.copy(fr, sw);
            fr.close();
            String content = sw.toString();
            String[] newsList = StringUtils.split(content, "####");
            for (int j = 0; j < newsList.length; j++) {
                FileWriter fw = new FileWriter(new File(outDir, String.format("%s-%02d.txt", files[i], j)));
                String fin = normalizePersianNewsTextForNer(newsList[j]);
                fw.write(fin.trim());
                fw.close();
            }
        }
    }

    // for tokenizer here are delimiters: 
}
