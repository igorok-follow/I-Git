package test;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    public static void main(String[] args) {
        Map<String, Float> products = new HashMap<>();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                String name, element;
                float price;

                @Override
                public void startDocument() {
                    System.out.println("Start parsing...\n-----------------------------------");
                }

                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    element = qName;
                }

                public void endElement(String uri, String localName, String qName) {
                    element = "";
                }

                public void characters(char[] ch, int start, int length) {
                    if (element.equals("name")) {
                        name = new String(ch, start, length);
                        System.out.println(name);
                    }
                    if (element.equals("price")) {
                        price = Float.parseFloat(new String(ch, start, length).replace("$", ""));
                        System.out.println(price);
                        products.put(name, price);
                    }
                }

                @Override
                public void endDocument() {
                    System.out.println("------------------------------------\nEnd of parse...\n\n");
                }
            };
            saxParser.parse("src/main/java/test/file.xml", handler);

            float tempPrice = 0;
            String tempName = "";
            for (Map.Entry<String, Float> map : products.entrySet()) {
                if (tempPrice < map.getValue()) {
                    tempName = map.getKey();
                    tempPrice = map.getValue();
                }
                System.out.println(map.getKey() + "-" + map.getValue());
            }

            System.out.println("\n\nMost high cost have a item with name: " + tempName);
            System.out.println("His cost equals: " + tempPrice);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}