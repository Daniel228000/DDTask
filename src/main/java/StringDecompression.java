import exceptions.InvalidExpressionException;
import exceptions.UnassignedFieldException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringDecompression {

    public static void main(String[] args) {
        StringInflater inflater = new StringInflater();
        String s1 = "8[xyz]4[xy]z";
        String s2 = "7[y]";
        String s3 = "2[b4[design]]";
        System.out.println(s1 + " -> " + inflater.inflate(s1));
        System.out.println(s2 + " -> " + inflater.inflate(s2));
        System.out.println(s3 + " -> " + inflater.inflate(s3));
    }

    public static class StringValidator {
        private String expression;

        public StringValidator() { }
        public boolean validate() {
            if (expression != null) {
                boolean hasEqualNumberOfBrackets = expression.replace("[", "").length() ==
                        expression.replace("]", "").length();
                List<Integer> list = new ArrayList<>();
                for (int i = 0; i < expression.length(); i++)
                    if (expression.charAt(i) == '[') list.add(i);

                boolean isValidFormatOfBrackets = list
                        .stream()
                        .allMatch(integer -> integer - 1 != -1 &&
                                Character.isDigit(expression.charAt(integer - 1)));

                boolean consistsOfValidCharacters = Pattern.matches("[a-z\\d\\[\\]]*", expression);

                boolean lastCharacterIsNotDigit = !Character.isDigit(expression.charAt(expression.length() - 1));

                return hasEqualNumberOfBrackets && isValidFormatOfBrackets && consistsOfValidCharacters && lastCharacterIsNotDigit;
            }
            else throw new UnassignedFieldException("Expression is not initialized");
        }
        public void setExpression(String expression) {
            this.expression = expression;
        }
    }

    public static class StringInflater {

        private int posLastParsedToken = 0;

        public String inflate(String expression) {
            StringValidator validator = new StringValidator();
            validator.setExpression(expression);
            if (!validator.validate()) throw new InvalidExpressionException("Invalid expression");
            else return parse(tokenize(expression), 0, false);
        }

        @NotNull
        private Object[] tokenize(@NotNull final String expression) {
            List<Object> tokens = new ArrayList<>();
            int i = 0;

            while (i < expression.length()) {
                if (Character.isDigit(expression.charAt(i))) {
                    StringBuilder number = new StringBuilder();
                    while (Character.isDigit(expression.charAt(i)) && i < expression.length()) {
                        number.append(expression.charAt(i++));
                    }
                    if (!Pattern.matches("0+\\d*", number.toString()))
                        tokens.add(Integer.valueOf(number.toString()));
                    else throw new InvalidExpressionException("Syntax error in " + expression + " in indexes " + (number.length() - i) + ".." + (i-1) + "\n" + number.toString() +" is not allowed here");
                } else {
                    tokens.add(expression.charAt(i++));
                }
            }
            return tokens.toArray(new Object[0]);
        }

        private String parse(@NotNull Object[] tokens, int pos, boolean nested) {
            posLastParsedToken = pos;
            String result;
            if (tokens[pos] instanceof Integer) {
                int repetition = (int) tokens[pos];
                if (tokens[pos + 1].equals("[")) {
                    result = parse(tokens, pos + 1, true);
                } else {
                    result = parse(tokens, pos + 1, false);
                }
                result = repeat(result, repetition);
                if (posLastParsedToken + 1 == tokens.length) {
                    return result;
                } else {
                    return result + parse(tokens, posLastParsedToken + 1, false);
                }
            } else if (tokens[pos].equals('[')) {
                return parse(tokens, pos + 1, true);
            } else if (tokens[pos].equals(']')) {
                return "";
            } else {
                if (nested) {
                    return tokens[pos] + parse(tokens, pos + 1, true);
                } else {
                    //
                    if (pos == 0 || tokens[pos - 1].equals(']'))
                        return "" + tokens[pos];
                    else throw new InvalidExpressionException("Numbers compatible only with brackets");
                }
            }
        }

        @NotNull
        private String repeat(final String s,
                              final int repetition) {
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < repetition; i++) {
                result.append(s);
            }
            return result.toString();
        }
    }
}
