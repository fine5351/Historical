package com.finekuo.normalcore.util.json;

import com.finekuo.normalcore.annotation.MaskedString;
import com.finekuo.normalcore.util.Gsons; // Changed from Jsons to Gsons
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaskedStringGsonSerializerTest {

    // Helper class with @MaskedString annotation (can be shared or re-declared)
    static class TestBean {
        @MaskedString
        public String myField;

        public TestBean(String myField) {
            this.myField = myField;
        }
    }

    // Helper class without @MaskedString annotation (can be shared or re-declared)
    static class NonAnnotatedBean {
        public String data;

        public NonAnnotatedBean(String data) {
            this.data = data;
        }
    }

    @Test
    void testSerializeFieldWithMaskedString() {
        TestBean bean = new TestBean("secretdata");
        String json = Gsons.toJson(bean); // Use Gsons
        assertEquals("{\"myField\":\"**********\"}", json);
    }

    @Test
    void testSerializeNullFieldWithMaskedString() {
        TestBean bean = new TestBean(null);
        String json = Gsons.toJson(bean); // Use Gsons
        assertEquals("{\"myField\":null}", json);
    }

    @Test
    void testSerializeNonAnnotatedField() {
        NonAnnotatedBean bean = new NonAnnotatedBean("publicdata");
        String json = Gsons.toJson(bean); // Use Gsons
        assertEquals("{\"data\":\"publicdata\"}", json);
    }

    @Test
    void testSerializeEmptyStringFieldWithMaskedString() {
        TestBean bean = new TestBean("");
        String json = Gsons.toJson(bean); // Use Gsons
        assertEquals("{\"myField\":\"\"}", json);
    }
}
