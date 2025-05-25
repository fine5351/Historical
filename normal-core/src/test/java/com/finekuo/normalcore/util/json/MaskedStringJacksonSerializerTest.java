package com.finekuo.normalcore.util.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.finekuo.normalcore.BaseTest;
import com.finekuo.normalcore.util.Jsons;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class MaskedStringJacksonSerializerTest extends BaseTest {

    // Helper class with @MaskedString annotation
    static class TestBean {

        @JsonSerialize(using = MaskedStringJacksonSerializer.class)
        public String myField;

        public TestBean(String myField) {
            this.myField = myField;
        }
    }

    // Helper class without @MaskedString annotation
    static class NonAnnotatedBean {
        public String data;

        public NonAnnotatedBean(String data) {
            this.data = data;
        }
    }

    @Test
    void testSerializeFieldWithMaskedString() {
        TestBean bean = new TestBean("secretdata");
        String json = Jsons.toJson(bean);
        assertEquals("{\"myField\":\"**********\"}", json);
    }

    @Test
    void testSerializeNullFieldWithMaskedString() {
        TestBean bean = new TestBean(null);
        String json = Jsons.toJson(bean);
        assertEquals("{\"myField\":null}", json);
    }

    @Test
    void testSerializeNonAnnotatedField() {
        NonAnnotatedBean bean = new NonAnnotatedBean("publicdata");
        String json = Jsons.toJson(bean);
        assertEquals("{\"data\":\"publicdata\"}", json);
    }

    @Test
    void testSerializeEmptyStringFieldWithMaskedString() {
        TestBean bean = new TestBean("");
        String json = Jsons.toJson(bean);
        assertEquals("{\"myField\":\"\"}", json);
    }
}
