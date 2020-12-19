package com.example.spy;

import android.content.Context;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class XMLParser extends ArrayList{


    static HashMap<String,String> rules = new HashMap<>();
    static ArrayList<HashMap<String, String>> rulesList = new ArrayList<>();

    public static ArrayList FromXMLParser(Context context, String filePath)
    {

         try{
            //ListView listView = (ListView) findViewById(R.id.wrap_content);
             SAXParserFactory parserFactory = SAXParserFactory.newInstance();
             SAXParser parser = parserFactory.newSAXParser();
             DefaultHandler handler = new DefaultHandler(){
                String currentValue = "";
                boolean currentElement = false;
                public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
                    currentElement = true;
                    currentValue = "";
                    if(localName.equals("rules")){
                        rules = new HashMap<>();
                    }
                }
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    currentElement = false;
                    if (localName.equalsIgnoreCase("item"))
                        rules.put("item", currentValue);
                    else if (localName.equalsIgnoreCase("rules"))
                        rulesList.add(rules);
                }
                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (currentElement) {
                        currentValue = currentValue +  new String(ch, start, length);
                    }
                }
            };
             InputStream istream = context.getAssets().open(filePath);
             parser.parse(istream,handler);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
             e.printStackTrace();
         }

        return rulesList;
    }


}
