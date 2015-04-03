package configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by Dimorinny on 03.04.15.
 */

public class ConfigLoader {

    private static ConfigLoader instance;

    private ApplicationConfig applicationConfig;
    private static final String APPLICATION_CONF_PATH =
            "src/main/resources/configuration.xml";

    private ConfigLoader() {}

    public static ConfigLoader getInstance() {
        if(instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    public ApplicationConfig loadApplicationConfig() {
        if(applicationConfig == null) {
            applicationConfig = initApplicationConfig();
        }
        return applicationConfig;
    }

    private ApplicationConfig initApplicationConfig() {
        try {

            File confFile = new File(APPLICATION_CONF_PATH);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(confFile);

            Element root = document.getDocumentElement();
            Element network = (Element) root.getElementsByTagName("network").item(0);

            int port = Integer.valueOf(
                    network.getElementsByTagName("port").item(0)
                    .getTextContent());

            String host = network.getElementsByTagName("host").item(0)
                    .getTextContent();

            Element mechanics = (Element) root.getElementsByTagName("mechanics").item(0);

            int minPlayers = Integer.valueOf(
                    mechanics.getElementsByTagName("minPlayerNumber").item(0)
                            .getTextContent());

            int maxPlayers = Integer.valueOf(
                    mechanics.getElementsByTagName("maxPlayerNumber").item(0)
                            .getTextContent());

            return new ApplicationConfig(port, host, minPlayers, maxPlayers);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
