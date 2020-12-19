package com.example.spy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;


public class SpecificActivity extends Activity {

    String XMLFilePath = "Rules-RU.xml";

   /* HashMap<String,String> rules = new HashMap<>();
    ArrayList<HashMap<String, String>> rulesList = new ArrayList<>();*/
    //ArrayList<HashMap<String, String>> rulesList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.specific_layout);

          /*  XMLParser parserFactory = new XMLParser();
            XMLParser parser = new XMLParser();
            XMLParser handler= new XMLParser();*/

        ArrayList rulesList = new ArrayList(XMLParser.FromXMLParser(getApplicationContext(), XMLFilePath));
        ListAdapter adapter = new SimpleAdapter(this, rulesList, R.layout.specific_layout,new String[]{"item"},
                new int[]{R.id.item});

        ListView listView = (ListView) findViewById(R.id.wrap_content);
        listView.setAdapter(adapter);

        /* try{
            ListView listView = (ListView) findViewById(R.id.wrap_content);
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
            InputStream istream = getAssets().open("Rules-RU.xml");
            parser.parse(istream,handler);
            ListAdapter adapter = new SimpleAdapter(XMLParserActivity.this, rulesList, R.layout.specific_layout,new String[]{"item"},
                    new int[]{R.id.item});
            listView.setAdapter(adapter);
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }*/


    }


}
