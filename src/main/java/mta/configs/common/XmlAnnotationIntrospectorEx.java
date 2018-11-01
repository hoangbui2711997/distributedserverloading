package mta.configs.common;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlAnnotationIntrospector;

public class XmlAnnotationIntrospectorEx extends JacksonXmlAnnotationIntrospector {
    @Override
    public boolean hasIgnoreMarker(AnnotatedMember m) {
        return m.hasAnnotation(JacksonIgnore.class) || super.hasIgnoreMarker(m);
    }
}
