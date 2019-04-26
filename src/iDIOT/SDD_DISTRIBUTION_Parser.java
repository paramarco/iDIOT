package iDIOT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nasa.worldwind.geom.LatLon;

public class SDD_DISTRIBUTION_Parser {
	
	private static BufferedReader br;

	static class SDD_DISTRIBUTION_Event extends iCASevent {
		
		public String ARCID;
		public List<VolumeSequence> listVolumeSequence;
		public List<ProgressionEntry> listProgression;
		
		public  SDD_DISTRIBUTION_Event( Date timeStamp, String aRCID, List<VolumeSequence> listVolumeSequence, List<ProgressionEntry> listProgression) {
			super(timeStamp);
			ARCID = aRCID;
			this.listVolumeSequence = listVolumeSequence;
			this.listProgression = listProgression;
		}
		
	}
	public static class VolumeSequence{
		
		public String Nr, Sector_Kind, Ref, Time_indicator, Skipped ,Entry_Ref , Entry_Kind, Exit_Ref, HandOver, OrigRef, SegKey, Aux_From, Aux_To;
		
		@Override
		public String toString() {
			return "VolumeSequence [" + (Nr != null ? "Nr=" + Nr + ", " : "")
					+ (Sector_Kind != null ? "Sector_Kind=" + Sector_Kind + ", " : "")
					+ (Ref != null ? "Ref=" + Ref + ", " : "")
					+ (Time_indicator != null ? "Time_indicator=" + Time_indicator + ", " : "")
					+ (Skipped != null ? "Skipped=" + Skipped + ", " : "")
					+ (Entry_Ref != null ? "Entry_Ref=" + Entry_Ref + ", " : "")
					+ (Entry_Kind != null ? "Entry_Kind=" + Entry_Kind + ", " : "")
					+ (Exit_Ref != null ? "Exit_Ref=" + Exit_Ref + ", " : "")
					+ (HandOver != null ? "HandOver=" + HandOver + ", " : "")
					+ (OrigRef != null ? "OrigRef=" + OrigRef + ", " : "")
					+ (SegKey != null ? "SegKey=" + SegKey + ", " : "")
					+ (Aux_From != null ? "Aux_From=" + Aux_From + ", " : "")
					+ (Aux_To != null ? "Aux_To=" + Aux_To : "") + "]";
		}
		 
	}

	/*
	                      SFPL  PROGRESSION
	------------------------------------------------------------------------------------
	           Sec  Pnt   Pnt
	 Nr  Sg-Lg  Ind  Kind  Ind  X,Y              Level  K  Q  Time Over      RFL    PFL    CST  Element                     MSA
	 */
	public static class ProgressionEntry{
		public String Nr, Sg_Lg, Sec_Ind, Pnt_Kind, Pnt_Ind, X_Y, FL, K, Q, ETO, RFL, PFL, CST;
		public LatLon Element;
		public String MSA;
		
		@Override
		public String toString() {
			return "[" + (Nr != null ? "Nr=" + Nr + ", " : "")
					+ (Sg_Lg != null ? "Sg_Lg=" + Sg_Lg + ", " : "")
					+ (Sec_Ind != null ? "Sec_Ind=" + Sec_Ind + ", " : "")
					+ (Pnt_Kind != null ? "Pnt_Kind=" + Pnt_Kind + ", " : "")
					+ (Pnt_Ind != null ? "Pnt_Ind=" + Pnt_Ind + ", " : "") + (X_Y != null ? "X_Y=" + X_Y + ", " : "")
					+ (FL != null ? "FL=" + FL + ", " : "") + (K != null ? "K=" + K + ", " : "")
					+ (Q != null ? "Q=" + Q + ", " : "") + (ETO != null ? "ETO=" + ETO + ", " : "")
					+ (RFL != null ? "RFL=" + RFL + ", " : "") + (PFL != null ? "PFL=" + PFL + ", " : "")
					+ (CST != null ? "CST=" + CST + ", " : "") + (Element != null ? "Element=" + Element + ", " : "")
					+ (MSA != null ? "MSA=" + MSA : "") + "]";
		}		
	}
	
	
	public static Map<String, List<SDD_DISTRIBUTION_Event>> loadSDD_DISTRIBUTION ( List<File> listFile_FP_DISTRIBUTION ) {
		
    	Map<String, List<SDD_DISTRIBUTION_Event>> map = new HashMap<>();
		
		Pattern r = Pattern.compile("[0-9]{1,2} \\w{3} 20[0-9]{2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}");
        Matcher m;
                				
		List<String> blockList = new ArrayList<String>();
        try {
        	
        	for ( File file_SDD_DISTRIBUTION : listFile_FP_DISTRIBUTION ) {	        	
	        	
	        	String thisLine;
	        	String block = "";
	        	br = new BufferedReader(new FileReader(file_SDD_DISTRIBUTION.getAbsolutePath()));
	        	
	            while ((thisLine = br.readLine()) != null) {	            	
	    			m = r.matcher( thisLine );	    			
	    			if ( m.find()) {
	    				if ( block != "") {
	    					blockList.add(block);
	    					block = "";
	    				}
	    				block += thisLine + "\n";
	    			}else {
	    				block += thisLine + "\n";
	    			}
	    			
	            } // end while
	            blockList.add(block);
	            
	            for ( String sblock : blockList) {    
	            	            	
	            	//23 Oct 2018 7:6:39 "dd MMM yyyy HH:mm:ss";
	        		Pattern regExTimeStamp = Pattern.compile("[0-9]{1,2} \\w{3} 20[0-9]{2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}");
	                Matcher matcherTimeStamp;
	                
	            	Date dateEvent = null;
	            	SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy H:m:s", Locale.ENGLISH);
	            	matcherTimeStamp = regExTimeStamp.matcher( sblock );    			
	    			if ( matcherTimeStamp.find()) {
	    				if ( matcherTimeStamp.group(0) != null) {	    					
	    					try {
	    						String sDate = matcherTimeStamp.group(0);
								dateEvent = formatter.parse(sDate);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	    					
	    				}else {
	    					System.out.println("DEBUG ::: hey ...");
	    				}
	    			} 
	    			
	    			String callsign = "";
	    			
	    			Pattern regExCallSign = Pattern.compile("ARCID  : (\\w+)");
	    			Matcher matcherCallSign = regExCallSign.matcher( sblock ); 
	    			   			
	    			if ( matcherCallSign.find()) {
	    				if ( matcherCallSign.group(1) != null) {
	    					callsign = matcherCallSign.group(1);
	    				}
	    			}	    			
	    			
	    			//  Nr  Sector Kind  Ref  Time indicator Skipped  Entry Ref  Entry Kind  Exit Ref  HandOver  OrigRef  SegKey  Aux_From  Aux_To  
	    			//  01  E_ASSIGNED   006  FALSE          FALSE    00001      HORIZONTAL  0008      FALSE      006 A        FALSE 000 000	    			
	    			
	    			List<VolumeSequence> listVolumeSequence = new ArrayList<VolumeSequence>();
	    			
	    			Pattern regExVolSeq = Pattern.compile("([0-9]{2})\\s+(\\w+)\\s+([0-9]{3})\\s+(\\w+)\\s+(\\w+)\\s+([0-9]{5})\\s+(\\w+)\\s+([0-9]{4})\\s+(\\w+)\\s+([0-9]{3})\\s+(\\w+)\\s+(\\w+)\\s+([0-9]{3})\\s+([0-9]{3})");
	    			Matcher matcherVolseq = regExVolSeq.matcher( sblock ); 
	    			while ( matcherVolseq.find()) {
	    				VolumeSequence volSequence = new VolumeSequence();
	    				if ( matcherVolseq.group(1) != null) {
	    					volSequence.Nr = matcherVolseq.group(1);
	    				}
	    				if ( matcherVolseq.group(2) != null) {
	    					volSequence.Sector_Kind = matcherVolseq.group(2);
	    				}
	    				if ( matcherVolseq.group(3) != null) {
	    					volSequence.Ref = matcherVolseq.group(3);
	    				}
	    				listVolumeSequence.add(volSequence);
	    			}
	    			/*
	    			 *                      SFPL  PROGRESSION
					------------------------------------------------------------------------------------
					           Sec  Pnt   Pnt
					 Nr  Sg-Lg  Ind  Kind  Ind  X,Y              Level  K  Q  Time Over      RFL    PFL    CST  Element                     MSA
					 ==  =====  ===  ====  ===  ===============  =====  =  =  =============  =====  =====  ===  ===========================  ===
					  1  01-01    1  ADEP    1   298525,-644220    14   A  1  181023-070800  F280   F280     1  482200N0114916E             
					  2  01-01    0  NONE    0   276712,-610643    88   A  1  181023-071030  F280   F280     0  483019N0114134E             
					  3  01-01    0  NONE    0   274964,-607952    93   A  1  181023-071040  F280   F280     0  483059N0114057E             
					  4  01-01    0  NONE    0   271428,-602509   103   A  1  181023-071100  F280   F280     0  483219N0113941E             
					  5  01-01    0  NONE    0   269357,-599321   108   A  1  181023-071110  F280   F280     0  483307N0113857E             
					  6  01-01    0  NONE    0   252483,-573346   190   A  1  181023-071450  F280   F280     2  483932N0113257E             
					  7  01-01    0  FIX     2   251162,-571312   190   A  1  181023-071455  F280   F280     3  484002N0113229E,INPUD   
					  */
	    			List<ProgressionEntry> listProgression = new ArrayList<ProgressionEntry>();
	    			
	    			Pattern regExProgression = Pattern.compile("(\\d{1,2})\\s+(\\d\\d-\\d\\d)\\s+(\\d+)\\s+(\\w+)\\s+(\\d+)\\s+(-?\\d+,-?\\d+)\\s+(\\d+)\\s+(\\w)\\s+(\\d)\\s+(\\d+-\\d+)\\s+(\\w+)\\s+(\\w+)\\s+(\\d+)\\s+(\\w+)(,(\\w+))?");
	    			Matcher matcherProgression = regExProgression.matcher( sblock ); 
	    			while ( matcherProgression.find()) {
	    				ProgressionEntry progresion = new ProgressionEntry();
	    				if ( matcherProgression.group(1) != null) {
	    					progresion.Nr = matcherProgression.group(1);
	    				}
	    				if ( matcherProgression.group(7) != null) {
	    					progresion.FL = matcherProgression.group(7);
	    				}
	    				if ( matcherProgression.group(10) != null) {
	    					progresion.ETO = matcherProgression.group(10);
	    				}
	    				
	    				if ( matcherProgression.group(14) != null) {
	    					progresion.Element = parseLatLon( matcherProgression.group(14) );	    					
	    				}
	    				listProgression.add(progresion);
	    			}
	    			

	    			if ( dateEvent != null && callsign != "") {
	    				SDD_DISTRIBUTION_Event event = new SDD_DISTRIBUTION_Event( dateEvent, callsign, listVolumeSequence, listProgression );
		            			            	
		            	List<SDD_DISTRIBUTION_Event> listEvents = map.get(event.ARCID);
		        		if ( listEvents == null) {
		        			List<SDD_DISTRIBUTION_Event> newList = new ArrayList<SDD_DISTRIBUTION_Event>();
		        			newList.add(event);
		        			map.put(event.ARCID, newList );
		        		}
		        		else {
		        			listEvents.add(event);
		        		}		            	
	    			}
	            }           

    		}
		}			
		catch (IOException e) {
			e.printStackTrace();
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
