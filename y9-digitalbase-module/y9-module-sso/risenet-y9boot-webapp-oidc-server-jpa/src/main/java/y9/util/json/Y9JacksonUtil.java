package y9.util.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Y9JacksonUtil {

    // jackson的objectMapper 设计为单例，其他地方使用时，不要重复创建
    public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Y9DateFormat sdf = new Y9DateFormat();
        objectMapper.setDateFormat(sdf);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static void main(String[] args) {
        String s = "[\"aaa\",\"bbb\"]";

        String[] ss = Y9JacksonUtil.readValue(s, String[].class);
        for (String item : ss) {
            System.out.println("array item==" + item);
        }

        ss = Y9JacksonUtil.readArray(s, String.class);
        for (String item : ss) {
            System.out.println("array2 item==" + item);
        }

        List<String> list = Y9JacksonUtil.readList(s, String.class);
        for (String item : list) {
            System.out.println("list item==" + item);
        }

        String s1 = "{\"aaa\":\"111\",\"bbb\":\"222\"}";
        HashMap<String, String> map1 = Y9JacksonUtil.readHashMap(s1, String.class, String.class);
        System.out.println("aaa==" + map1.get("aaa"));

        String s2 = "[{\"aaa\":\"1a\",\"bbb\":\"1b\"},{\"aaa\":\"2a\",\"bbb\":\"2b\"}]";
        List<Map<String, Object>> list2 = Y9JacksonUtil.readListOfMap(s2);
        for (Map<String, Object> map : list2) {
            System.out.println("bbb==" + map.get("bbb"));
        }

        List<Map<String, Object>> list3 = Y9JacksonUtil.readListOfMap(s2, String.class, Object.class);
        for (Map<String, Object> map : list3) {
            System.out.println("bbb==" + map.get("bbb"));
        }

        String s3 = "{\"aaa\":\"111\",\"bbb\":[{\"q\":\"q1111\",\"t\":\"t1111\"},{\"q\":\"q2222\",\"t\":\"t2222\"}]}";
        HashMap<String, Object> map2 = Y9JacksonUtil.readHashMap(s3, String.class, Object.class);
        System.out.println("aaa==" + map2.get("aaa"));
        System.out.println("bbb==" + map2.get("bbb"));

        List<Map<String, Object>> list4 =
            Y9JacksonUtil.readListOfMap(Y9JacksonUtil.writeValueAsString(map2.get("bbb")), String.class, Object.class);
        for (Map<String, Object> map : list4) {
            System.out.println("q==" + map.get("q"));
        }

    }

    public static <T> T[] readArray(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, objectMapper.getTypeFactory().constructArrayType(valueType));
        } catch (JsonParseException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (JsonMappingException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    public static HashMap<String, Object> readHashMap(String content) {
        try {
            return objectMapper.readValue(content,
                objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class));
        } catch (JsonParseException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (JsonMappingException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    public static <K, V> HashMap<K, V> readHashMap(String content, Class<K> keyClass, Class<V> valueClass) {
        try {
            return objectMapper.readValue(content,
                objectMapper.getTypeFactory().constructMapType(HashMap.class, keyClass, valueClass));
        } catch (JsonParseException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (JsonMappingException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    public static <T> List<T> readList(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, valueType));
        } catch (JsonParseException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (JsonMappingException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    public static List<Map<String, Object>> readListOfMap(String content) {
        try {
            JavaType inner = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
            return objectMapper.readValue(content,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, inner));
        } catch (JsonParseException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (JsonMappingException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    public static <K, V> List<Map<K, V>> readListOfMap(String content, Class<K> keyClass, Class<V> valueClass) {
        try {
            JavaType inner = objectMapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
            return objectMapper.readValue(content,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, inner));
        } catch (JsonParseException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (JsonMappingException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (JsonParseException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (JsonMappingException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T readValue(String content, JavaType valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (JsonParseException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (JsonMappingException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (JsonParseException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (JsonMappingException e) {
            LOGGER.warn(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return null;
    }

    public static String writeValueAsString(Object value) {
        String s = "";
        try {
            s = objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return s;
    }

    public static String writeValueAsString(Object value, boolean include, String... filters) {
        FilterProvider filterProvider = null;
        if (include) {
            filterProvider = new SimpleFilterProvider().addFilter("propertyFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept(filters));
        } else {
            filterProvider = new SimpleFilterProvider().addFilter("propertyFilter",
                SimpleBeanPropertyFilter.serializeAllExcept(filters));
        }
        ObjectWriter writer = objectMapper.writer(filterProvider);
        String s = "";
        try {
            s = writer.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return s;
    }

    public static String writeValueAsStringWithDefaultPrettyPrinter(Object value) {
        String s = "";
        try {
            s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            LOGGER.warn(e.getMessage(), e);
        }
        return s;
    }
}
