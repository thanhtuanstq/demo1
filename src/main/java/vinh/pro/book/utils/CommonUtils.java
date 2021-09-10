package vinh.pro.book.utils;

import lombok.NonNull;

import java.util.regex.Pattern;

public class CommonUtils {
    private static final String REGEX_EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static boolean isValidMail(@NonNull String mail) {
        Pattern pattern = Pattern.compile(REGEX_EMAIL_PATTERN);

        return pattern.matcher(mail).matches();
    }

    public static String escapeMetaCharacters(final String inputString) {
        final String[] metaCharacters = {"\\",
                                         "^",
                                         "$",
                                         "{",
                                         "}",
                                         "[",
                                         "]",
                                         "(",
                                         ")",
                                         ".",
                                         "*",
                                         "+",
                                         "?",
                                         "|",
                                         "<",
                                         ">",
                                         "-",
                                         "_",
                                         "&",
                                         "%"};
        String output = inputString;
        for (String metaCharacter : metaCharacters) {
            if (output.contains(metaCharacter)) {
                output = output.replace(metaCharacter, "\\" + metaCharacter);
            }
        }

        return output;
    }
}
