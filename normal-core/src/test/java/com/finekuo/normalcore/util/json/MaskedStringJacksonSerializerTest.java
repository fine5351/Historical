package com.finekuo.normalcore.util.json;

import com.finekuo.normalcore.annotation.MaskedString;
import com.finekuo.normalcore.util.Jsons;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaskedStringJacksonSerializerTest {

    // Helper class with @MaskedString annotation
    static class TestBean {
        @MaskedString
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
