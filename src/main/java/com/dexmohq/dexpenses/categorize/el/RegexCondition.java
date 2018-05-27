package com.dexmohq.dexpenses.categorize.el;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;


/**
 * @author Henrik Drefs
 */
@JsonInclude(NON_DEFAULT)
@Data
public class RegexCondition implements Condition {

    private final String regex;
    private boolean caseSensitive;
    private boolean unicodeCase;
    private boolean canonEQ;
    private boolean comments;
    private boolean dotAll;
    private boolean literal;
    private boolean unicodeCharacterClass;
    private boolean unixLines;
    private boolean multiline;

    @JsonCreator
    public RegexCondition(@JsonProperty("regex") String regex) {
        this.regex = regex;
    }

    @Override
    public Predicate<CategoryInfo> get() {
        int flags = 0;
        if (!caseSensitive) {
            flags |= Pattern.CASE_INSENSITIVE;
        }
        if (unicodeCase) {
            flags |= Pattern.UNICODE_CASE;
        }
        if (canonEQ) {
            flags |= Pattern.CANON_EQ;
        }
        if (comments) {
            flags |= Pattern.COMMENTS;
        }
        if (dotAll) {
            flags |= Pattern.DOTALL;
        }
        if (literal) {
            flags |= Pattern.LITERAL;
        }
        if (unicodeCharacterClass) {
            flags |= Pattern.UNICODE_CHARACTER_CLASS;
        }
        if (unixLines) {
            flags |= Pattern.UNIX_LINES;
        }
        if (multiline) {
            flags |= Pattern.MULTILINE;
        }
        final Pattern pattern = Pattern.compile(regex, flags);
        return categoryInfo -> pattern.matcher(categoryInfo.getHeader()).find();
    }

}
