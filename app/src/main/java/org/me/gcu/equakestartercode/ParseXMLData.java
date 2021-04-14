package org.me.gcu.equakestartercode;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;
import java.util.ArrayList;
/**Created by ismail adam on 25/03/2021
 * Student ID: S1908016 */
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
            // the getName method might return null if
            // the parser isn't inside a tag
            String tagName = xpp.getName();

                // take action depending on the type of event inside the parser
                // at some point it will read start of a tag in the xml and
                // when that happens the event type will change to START_TAG, if that
                // happens, we are only interested if it is an entry tag because we are
                // only doing anything with the data in the individual entries, we are not
                // touching any of the data that is not in the entry.
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
                                String[] descArr = textValue.split(";");
                                //0: "Origin date/time: Mon, 01 Feb 2021 18:05:16 "
                                //1: " Location: LLANVEYNOE,HEREF "
                                //2: " Lat/long: 51.961,-3.043 "
                                //3: " Depth: 13 km "
                                //4: " Magnitude: 1.9"
                                String dateTime = descArr[0].split(":")[1];
                                String location = descArr[1].split(":")[1];
                                double latitude = Double.parseDouble(descArr[2].split(":")[1].split(",")[0]);
                                double longitude = Double.parseDouble(descArr[2].split(":")[1].split(",")[1]);
                                // replace everything except numbers and decimal point
                                double depth = Double.parseDouble(descArr[3].replaceAll("[^0-9.-]", ""));
                                double magnitude = Double.parseDouble(descArr[4].split(":")[1]);
                                Description newDescription = new Description(dateTime, location, latitude, longitude, depth, magnitude);
                                currentRecord.setDescription(newDescription);
                            } else if("link".equalsIgnoreCase(tagName)){
                                currentRecord.setLink(textValue);
                            }
                            else if("category".equalsIgnoreCase(tagName)){
                                currentRecord.setCategory(textValue);
                            }
                        }
                        break;
                    default: // nothing else to do
                }
                eventType = xpp.next();
            }
        }catch (Exception e){
            status = false;
            Log.e(TAG, "parseXML: Error whiling parsing xml data");
            e.printStackTrace();
        }
        return status;
    }


}
