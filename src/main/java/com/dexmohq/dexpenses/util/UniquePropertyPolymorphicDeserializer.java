package com.dexmohq.dexpenses.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UniquePropertyPolymorphicDeserializer<T> extends StdDeserializer<T> {

    /*
     the registry of unique field names to Class types
      */
    private Map<String, Class<? extends T>> registry;

    public UniquePropertyPolymorphicDeserializer(Class<T> clazz) {
        super(clazz);
        registry = new HashMap<>();
    }

    public void register(String uniqueProperty, Class<? extends T> clazz) {
        registry.put(uniqueProperty, clazz);
    }

    public T emptyValue(DeserializationContext ctx) throws JsonMappingException {
        throw ctx.instantiationException(_valueClass, "No default value for empty JSON provided");
    }

    public Class<? extends T> defaultValueClass() {
        return null;
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        Class<? extends T> clazz = null;

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        TreeNode obj = mapper.readTree(jp);

        if (!(obj instanceof ObjectNode)) {
            throw ctx.instantiationException(this._valueClass, "must be object type");
        }
        Iterator<Map.Entry<String, JsonNode>> elementsIterator = ((ObjectNode) obj).fields();
        if (!elementsIterator.hasNext()) {
            return emptyValue(ctx);
        }
        while (elementsIterator.hasNext()) {
            Map.Entry<String, JsonNode> element = elementsIterator.next();
            String name = element.getKey();
            if (registry.containsKey(name)) {
                clazz = registry.get(name);
                break;
            }
        }
        if (clazz == null) {
            clazz = defaultValueClass();
        }
        if (clazz == null) {
            throw ctx.instantiationException(this._valueClass, "No registered unique properties found for polymorphic deserialization");
        }

        return mapper.treeToValue(obj, clazz);
    }

}