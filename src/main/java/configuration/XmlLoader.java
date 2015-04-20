package configuration;
import frontend.annotation.xml.ArrayElement;
import frontend.annotation.xml.ElementGroup;
import frontend.annotation.xml.FieldElement;
import frontend.annotation.xml.RootElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
                    Element src = rootField;
                    if(field.isAnnotationPresent(ElementGroup.class)) {
                        src = (Element) root.getElementsByTagName(
                                field.getAnnotation(ElementGroup.class).name()
                        ).item(0);
                    }

                    if(field.isAnnotationPresent(ArrayElement.class)) {
                        List<String> array = new ArrayList<>();
                        NodeList elements = src.getElementsByTagName(fieldElement);
                        for(int i = 0; i < elements.getLength(); i++){
                            array.add(elements.item(i).getTextContent());
                        }
                        field.set(instance, array);
                    } else {
                        field.set(instance, src.getElementsByTagName(fieldElement)
                                .item(0).getTextContent());
                    }
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
