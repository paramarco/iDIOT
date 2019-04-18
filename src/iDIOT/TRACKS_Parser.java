package iDIOT;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;

public class TRACKS_Parser {
	
	public static class TRACK_event extends iCASevent {
		
		public String ARCID ;
		Position Position;	
		
		public TRACK_event(Date timeStamp, String aRCID, gov.nasa.worldwind.geom.Position position) {
			
			super(timeStamp);
			
			ARCID = aRCID;
			Position = position;
		}

		@Override
		public String toString() {
			return "[" + (ARCID != null ? "ARCID=" + ARCID + "\n " : "")
					+ (Position != null ? "Position=" + Position + "\n " : "")
					+ (timeStamp != null ? "timeStamp=" + timeStamp : "") + "]";
		}
		
	
	}
	
	public static Map<String, List<TRACK_event>> loadTRACKS_events( List<File> listFilesTracks ) {
		
    	Map<String, List<TRACK_event>> map = new HashMap<>();		
				
		for (File fileTRACKS  : listFilesTracks) {
			
			List<String> listTRACKs = new ArrayList<String>();
			
	        try {
	        	
	            Scanner targetFile = new Scanner(fileTRACKS);
	            targetFile.useDelimiter("____________________________");
	            String token1 = "";
	            // while loop
	            while (targetFile.hasNext()) {
	              // find next line
	              token1 = targetFile.next();
	              listTRACKs.add(token1);
	            }
	            targetFile.close();
	          
	            Pattern r;
	            Matcher m;
	            
	    		double currentLat = 0, currentLon = 0, Altitude = 0 ;	    		
	    		
	            for ( String track : listTRACKs) {
	            	
	            	//TODO get date
	            	Date eventDate = null;
	            	Pattern regExTimeStamp = Pattern.compile("Time of Track \\(070\\).*\\((.*)\\)");
	                Matcher matcherTimeStamp;
	                
	            	matcherTimeStamp = regExTimeStamp.matcher( track );    			
	    			if ( matcherTimeStamp.find()) {
	    				if ( matcherTimeStamp.group(1) != null) {	    					
	    					try {
	    						String sDate = matcherTimeStamp.group(1);
	    						eventDate = new SimpleDateFormat("HH:mm:ss ddMMyy").parse(sDate);
							} catch (ParseException e) {
								e.printStackTrace();
							}	    					
	    				}
	    			}
	            	
	            	String foundCallsign = "";
		        	
		        	r = Pattern.compile("LAT \\[deg\\] : ([0-9]+(\\.[0-9]+)?)");
					m = r.matcher( track );
		
					while (m.find()) {	    	   
			    	   if ( m.group(1) != null) {
			    		   String foundLAT =  m.group(1) ;
			    		   currentLat = Double.parseDouble(foundLAT);
			    	   }
			       }
					
		        	r = Pattern.compile("LON \\[deg\\] : ([0-9]+(\\.[0-9]+)?)");
					m = r.matcher( track );
		
					while (m.find()) {	    	   
			    	   if ( m.group(1) != null) {
			    		   String foundLON =  m.group(1) ;
			    		   currentLon = Double.parseDouble(foundLON);
	
			    	   }
			       }
					
		        	r = Pattern.compile("Callsign        \\(EXT2\\) : (\\w+)");
					m = r.matcher( track );
		
					while (m.find()) {	    	   
			    	   if ( m.group(1) != null) {
			    		   foundCallsign =  m.group(1) ;
			    	   }
			       }
					
		        	r = Pattern.compile("Flight level measured \\(136\\)              : ([0-9]+)");
					m = r.matcher( track );
		
					while (m.find()) {	    	   
			    	   if ( m.group(1) != null) {
			    		   String foundAltitude =  m.group(1) ;
			    		   double dfoundAltitude = Double.parseDouble(foundAltitude);
			    		   dfoundAltitude = dfoundAltitude * 10;
			    		   Altitude = dfoundAltitude;
	
			    	   }
			       }			
					
					if (foundCallsign != "" && currentLat != 0 && currentLon != 0 && Altitude != 0 && eventDate != null ) {
						TRACK_event event = new TRACK_event(
							eventDate,
							foundCallsign, 
							new Position ( LatLon.fromDegrees(currentLat ,currentLon) ,  Altitude ) 
						);
							//tracksEvents.add(event);
						List<TRACK_event> listEvents = map.get(event.ARCID);
			    		if ( listEvents == null) {
			    			List<TRACK_event> newList = new ArrayList<TRACK_event>();
			    			newList.add(event);
			    			map.put(event.ARCID, newList );
			    		}
			    		else {
			    			listEvents.add(event);
			    		}
					}									
		        }
	            
	       
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}

		return map;
	}

	
	

	public static LatLon parseLatLon(String latLonString) {
		
		double latitude;
		double longitude;
		double degreesLat = 0, degreesLon = 0 ;
		double minutesLat = 0, minutesLon = 0 ;
		double secondsLat = 0, secondsLon = 0 ;
		double latitudeSign = 1;
		double longitudeSign = 1;
		
		Pattern r = Pattern.compile("^(\\d{2})(\\d{2})(\\d{2})(N|S)(\\d{3})(\\d{2})(\\d{2})(W|E)");
		Matcher m = r.matcher( latLonString );
		while (m.find()) {	    	   
		  if ( m.group(1) != null) {
			  degreesLat =  Double.valueOf( m.group(1) ) ;
			  minutesLat =  Double.valueOf( m.group(2) ) ;
			  secondsLat =  Double.valueOf( m.group(3) ) ;
			  degreesLon =  Double.valueOf( m.group(5) ) ;
			  minutesLon =  Double.valueOf( m.group(6) ) ;
			  secondsLon =  Double.valueOf( m.group(7) ) ;
			  if ( m.group(4).contains("S") ) latitudeSign = -1;
			  if ( m.group(8).contains("W") ) longitudeSign = -1;
		  }
		}
				
		latitude = ( degreesLat + ( (minutesLat * 60) + secondsLat ) / (60*60) ) * latitudeSign;
		longitude = ( degreesLon + ( (minutesLon * 60) + secondsLon ) / (60*60) ) * longitudeSign;				
		
		return LatLon.fromDegrees( latitude, longitude);
		
	}

}
