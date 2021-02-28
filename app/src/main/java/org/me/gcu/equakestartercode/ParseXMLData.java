package org.me.gcu.equakestartercode;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class ParseXMLData {

    private static final String TAG = "ParseXMLData";
    private final ArrayList<Item> items;

    public ParseXMLData(){
        this.items = new ArrayList<>();
    }

    public ArrayList<Item> getApplications(){
        return this.items;
    }

    public boolean parseXML(String XMLData){
        boolean status = true;
        Item currentRecord = null;
        boolean inItem = false;
        String textValue = "";

        try {
            // StringReader is a class that treats a String like a stream
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(XMLData));

            int eventType = xpp.getEventType();
            // keep looping till the end of xml document
            while (eventType != XmlPullParser.END_DOCUMENT){

            // get the name of the current tag
            // it is worth noting that getName method might return null if
            // the parser isn't inside a tag
            String tagName = xpp.getName();

            // take action depending on the type of event inside the parser
            // now, at some point it will read start of a tag in the xml and
            // when that happens the event type will change to START_TAG, if that
            // happens, we are only interested if it is an entry tag because we are
            // only doing anything with the data in the individual entries, we are not
            // touching any of the data that is not in the entry. we could modify
            // and display the title of the feed.
                switch (eventType){
                    case XmlPullParser.START_TAG:
                    //Log.d(TAG, "parse: starting tag for "+ tagName);
                        if("item".equalsIgnoreCase(tagName)){
                            inItem = true;
                            currentRecord = new Item();
                        }
                        break;
                    case XmlPullParser.TEXT: textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                    //Log.d(TAG, "parse: ending tag for "+tagName);
                        if (inItem){
                            if("item".equalsIgnoreCase(tagName)){
                                // add the current entry to items and set inItem to false
                                // because this is the end of an entry
                                items.add(currentRecord);
                                inItem = false;
                            }else if("title".equalsIgnoreCase(tagName)){
                                currentRecord.setTitle(textValue);
                            }else if("description".equalsIgnoreCase(tagName)){
                                currentRecord.setDescription(textValue);
                            }else if("link".equalsIgnoreCase(tagName)){
                                currentRecord.setLink(textValue);
                            }else if("pubDate".equalsIgnoreCase(tagName)){
                                currentRecord.setPubDate(textValue);
                            }else if("category".equalsIgnoreCase(tagName)){
                                currentRecord.setCategory(textValue);
                            }else if("lat".equalsIgnoreCase(tagName)){
                                currentRecord.setLatitude(textValue);
                            }else if("long".equalsIgnoreCase(tagName)){
                                currentRecord.setLongitude(textValue);
                            }
                        }
                        break;
                    default: // nothing else to do
                }
                eventType = xpp.next();
            }
            // traverse the list and see if it is actually worked
//            for (Item entry : items){
//                Log.d(TAG, "parse: ***************************");
//                Log.d(TAG, entry.toString());
//            }
        }catch (Exception e){
            status = false;
            Log.e(TAG, "parseXML: Error whiling parsing xml data");
            e.printStackTrace();
        }
        return status;
    }


}
