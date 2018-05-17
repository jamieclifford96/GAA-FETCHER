package vargovcik.rss.reader.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import vargovcik.rss.reader.dto.*;


public class RSSFeedParser {
    static final String FEED = "feed";
    static final String UPDATED = "updated";
    static final String ID = "id";
	static final String TITLE = "title";
    static final String LINK = "link";
    static final String ENTRY = "entry";

    final URL url;

    public RSSFeedParser(String feedUrl) {
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public RSSFeed readFeed() {
        RSSFeed feed = new RSSFeed();
        try {
            boolean isFeedHeader = true;

            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            
            // read the XML document
            while (eventReader.hasNext()) {
            	
                XMLEvent event = eventReader.nextEvent();
                
                if (event.isStartElement()) {
                    String localPart = event
                    		.asStartElement()
                    		.getName()
                            .getLocalPart();
                    
                    switch (localPart) {
                    case TITLE:
                    	feed.setTitle(getCharacterData(event, eventReader));
                        break;
                    case ID:
                    	feed.setId(getCharacterData(event, eventReader));
                        break;
                    case LINK:
                    	feed.getLinks().add(getLinkData(event, eventReader));
                        break;
                    case UPDATED:
                        //feed.setUpdated(getCharacterData(event, eventReader));
                        break;
                    case ENTRY:
                    	feed.getEntries().add(getEntryData(event, eventReader));
                        break;
                    }
                    
                    
                } else if (event.isEndElement()) {                	
                    if (event.asEndElement().getName().getLocalPart() == (FEED)) {
                        
                        event = eventReader.nextEvent();
                        continue;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return feed;
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }
    
    private RSSLink getLinkData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
    	String rel = "";
    	String type = "";
    	String href = "";
    	
        Iterator<Attribute> iterator = event.asStartElement().getAttributes();        
        while(iterator.hasNext()){
            Attribute myAttribute = iterator.next();
            if(myAttribute.getName().toString().equals("rel")){
            	rel = myAttribute.getValue();
            }
            else if(myAttribute.getName().toString().equals("type")){
            	type = myAttribute.getValue();
            }
            else if(myAttribute.getName().toString().equals("href")){
            	href = myAttribute.getValue();
            }
        }

        return new RSSLink(rel, type, href);
    }
    
    private RSSFeedEntry getEntryData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
    	Iterator<Attribute> iterator = event.asStartElement().getAttributes();        
        while(iterator.hasNext()){
            
        }
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return new RSSFeedEntry();
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
