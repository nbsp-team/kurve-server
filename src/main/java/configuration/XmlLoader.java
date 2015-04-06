package configuration;
import frontend.annotation.xml.FieldElement;
import frontend.annotation.xml.RootElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Dimorinny on 03.04.15.
 */

public class XmlLoader {

    private static XmlLoader instance;

    private XmlLoader() {}

    public static XmlLoader getInstance() {
        if(instance == null) {
            instance = new XmlLoader();
        }
        return instance;
    }

    public Object load(Class<?> clazz, String xmlName) {
        try {

            File confFile = new File(xmlName);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(confFile);

            Element root = document.getDocumentElement();

            Element rootField = (Element) root.getElementsByTagName(
                    clazz.getAnnotation(RootElement.class).name()
            ).item(0);

            Object instance = clazz.getConstructors()[0].newInstance();

            for(Field field : clazz.getFields()) {
                if(field.isAnnotationPresent(FieldElement.class)) {
                    String fieldElement = field.getAnnotation(FieldElement.class).name();
                    field.set(instance, rootField.getElementsByTagName(fieldElement)
                            .item(0).getTextContent());
                }
            }

            return instance;

        } catch (ParserConfigurationException |
                InvocationTargetException |
                SAXException |
                InstantiationException |
                IOException |
                IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
