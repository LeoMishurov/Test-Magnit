package ru.test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class Xml {
    // запись данных в xml
    public static void createXML(Collection<Integer> collection, String filePath)
    {
        // создание xml документа
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();

            // Создание корневого элемента
            Element root = document.createElement("entries");
            document.appendChild(root);

             // запись данных в документ
            for (Integer number : collection) {
                Element entry = document.createElement("entry");
                root.appendChild(entry);
                Element field = document.createElement("field");
                entry.appendChild(field);
                field.appendChild(document.createTextNode(String.valueOf(number)));
            }

            // создание файла
            try (FileOutputStream output = new FileOutputStream(filePath)) {
                writeXml(document, output);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("XML создан");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Запись Xml в файл
    private static void writeXml(Document doc, OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }

    // преобразование xml1 в xml2
    public static void transform(String xmlFilePath, String xsltFilePath, String resultXmlFilePath) {
        File stylesheet = new File(xsltFilePath);
        File datafile = new File(xmlFilePath);

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder2 = factory.newDocumentBuilder();
            Document document2 = builder2.parse(datafile);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            StreamSource styleSource = new StreamSource(stylesheet);
            Transformer transformer = transformerFactory.newTransformer(styleSource);
            DOMSource source = new DOMSource(document2);
            transformer.transform(source, new StreamResult(new FileOutputStream(resultXmlFilePath)));

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e ) {
            e.printStackTrace();
        }
    }

    //парсинг данных из xml
    public static Collection<Integer> doParse(String sequenceXmlFilePath) throws Exception {
        Collection<Integer> parsedSequence = new ArrayList<>();

        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader parser = factory.createXMLStreamReader(
                    new BufferedInputStream(new FileInputStream(sequenceXmlFilePath)));

            while (parser.hasNext()) {
                int event = parser.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    if (parser.getLocalName().equals("entry")) {
                        String intValueInAttribute = parser.getAttributeValue(null, "field");
                        if (intValueInAttribute != null) {
                            parsedSequence.add(Integer.parseInt(intValueInAttribute));
                        }
                    }
                }
            }
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Парсинг выполнен");
        return parsedSequence;
    }

}
