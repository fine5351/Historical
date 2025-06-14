package com.finekuo.normalcore.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class GsonsTest {

    @Test
    void getMaskSettingsTemplate() {
        TestTemplateDTO testTemplateDTO = buildFilledTestTemplateDTO();
        log.info(Gsons.getMaskSettingsTemplate(testTemplateDTO));
    }

    private TestTemplateDTO buildFilledTestTemplateDTO() {
        TestTemplateDTO testTemplateDTO = new TestTemplateDTO();
        testTemplateDTO.setName("Parent Name");
        testTemplateDTO.setAddress("Parent Address");

        TestTemplateDTO.ChildNode1 childNode1 = new TestTemplateDTO.ChildNode1();
        childNode1.setChildName("Child Node 1 Name");
        childNode1.setChildAddress("Child Node 1 Address");

        TestTemplateDTO.ChildNode1.ChildNode2 childNode2 = new TestTemplateDTO.ChildNode1.ChildNode2();
        childNode2.setChildName("Child Node 2 Name");
        childNode2.setChildAddress("Child Node 2 Address");

        TestTemplateDTO.ChildNode1.ChildNode2.ChildNode3 childNode3 = new TestTemplateDTO.ChildNode1.ChildNode2.ChildNode3();
        childNode3.setChildName("Child Node 3 Name");
        childNode3.setChildAddress("Child Node 3 Address");

        childNode2.setChildNode3(childNode3);
        childNode1.setChildNode2(childNode2);
        testTemplateDTO.setChildNode1(childNode1);

        return testTemplateDTO;
    }

    @Data
    private static class TestTemplateDTO {

        private String name;
        private String address;
        private ChildNode1 childNode1;

        @Data
        private static class ChildNode1 {

            private String childName;
            private String childAddress;
            private ChildNode2 childNode2;

            @Data
            private static class ChildNode2 {

                private String childName;
                private String childAddress;
                private ChildNode3 childNode3;

                @Data
                private static class ChildNode3 {

                    private String childName;
                    private String childAddress;

                }

            }

        }

    }


}