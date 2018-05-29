import com.dexmohq.dexpenses.util.config.Regex;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @author Henrik Drefs
 */
public class ValidationTest {

    @JsonDeserialize(as = Bean.class)
    interface IBean {

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bean implements IBean {
        @Min(5)
        private int n;

        @Regex
        private String regex;

    }

    public static class Json extends LinkedHashMap<String, IBean> {

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JsonWrapper {

        @JsonIgnore
        @Valid
        private Json json = new Json();

        @JsonAnySetter
        public void add(String key, IBean bean) {
            json.put(key, bean);
        }

        @JsonAnyGetter
        public Json getAny() {
            return json;
        }
    }

    public static void main(String[] args) throws IOException {
        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        final Json json = new Json();
        json.put("foo", new Bean(4, ".*"));
        json.put("bar", new Bean(5, "^["));

        final JsonWrapper jsonWrapper = new JsonWrapper(json);
        final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        final String s = objectMapper.writeValueAsString(jsonWrapper);
        System.out.println(s);
        final JsonWrapper jsonWrapper1 = objectMapper.readValue(s, JsonWrapper.class);
        System.out.println(jsonWrapper1);
        final Set violations = validator.validate(jsonWrapper1);
        if (violations.isEmpty()) {
            System.out.println("valid");
        } else {
            System.out.println("invalid");
            violations.forEach(System.out::println);
        }
    }

}
