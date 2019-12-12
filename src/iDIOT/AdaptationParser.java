package iDIOT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import iDIOT.SDD_DISTRIBUTION_Parser.SDD_DISTRIBUTION_Event;
import iDIOT.TRACKS_Parser.TRACK_event;
import tcl.lang.*;

public class AdaptationParser  {

	public String AdaptationSource =""; // AdaptationType := iTAP_CSVs | environment.tcl
	public String fileAdaptationPath;
	Interp i;
	File fileAIRSPACE_VOLUMES;
	File fileAREA_CONTOUR_POINTS;
	File fileBASIC_SECTORS;
	File fileRESPONSIBILITY_VOLUMES;
	File fileHOLDING_AIRSPACE_VOLUMES;
	File fileHOLDING_VOLUMES;
	File fileINTEREST_VOLUMES;
	File fileSECTOR_VOLUMES;
	File fileSITUATION_LINE_POINTS;
	File fileSITUATION_LINES_GUIDE;
	File fileSITUATION_LINE_CONDITIONS;
	File fileROUTE_CONDITIONS_GUIDE;
	File fileROUTE_CONDITION_AERODROMES;
	File fileFIX_POINTS;

	
	public AdaptationParser( File fileAdaptation, String AdaptationSource ) {
		
		this.AdaptationSource = AdaptationSource;
		
		if ( AdaptationSource.equals("environment.tcl") ) {
			this.fileAdaptationPath = fileAdaptation.getAbsolutePath();
			this.i = new Interp();
			try {
				 i.evalFile(this.fileAdaptationPath);				 
			 } catch (TclException e) {
				 System.out.println("Exception: " + e.getMessage());
				 int code = e.getCompletionCode();
		         switch (code) {
		         	case TCL.ERROR:
		         		System.err.println(i.getResult().toString());
		                break;
		         	case TCL.BREAK:
		            	System.err.println("invoked \"break\" outside of a loop");
		            	break;
		         	case TCL.CONTINUE:
		         		System.err.println("invoked \"continue\" outside of a loop");
		         		break;
		         	default:
		         		System.err.println("command returned bad error code: " + code);
		         		break;
		         }
            }		
		}else if(AdaptationSource.equals("iTAP_CSVs")){
			this.fileAdaptationPath = fileAdaptation.getAbsolutePath();
			
			String pathAIRSPACE_VOLUMES = fileAdaptationPath + "/" + "AIRSPACE_VOLUMES_GUIDE.CSV";
			fileAIRSPACE_VOLUMES  = new File( pathAIRSPACE_VOLUMES );
			
			String pathAREA_CONTOUR_POINTS = fileAdaptationPath + "/" +  "AREA_CONTOUR_POINTS.CSV";
			fileAREA_CONTOUR_POINTS  = new File( pathAREA_CONTOUR_POINTS );
			
			String pathBASIC_SECTORS = fileAdaptationPath + "/" +  "BASIC_SECTORS.CSV";
			fileBASIC_SECTORS  = new File( pathBASIC_SECTORS );

			String pathRESPONSIBILITY_VOLUMES = fileAdaptationPath + "/" +  "RESPONSIBILITY_VOLUMES.CSV";
			fileRESPONSIBILITY_VOLUMES  = new File( pathRESPONSIBILITY_VOLUMES );
			
			String pathHOLDING_AIRSPACE_VOLUMES = fileAdaptationPath + "/" +  "HOLDING_AIRSPACE_VOLUMES.CSV";
			fileHOLDING_AIRSPACE_VOLUMES  = new File( pathHOLDING_AIRSPACE_VOLUMES );
			
			String pathHOLDING_VOLUMES = fileAdaptationPath + "/" +  "HOLDING_VOLUMES_GUIDE.CSV";
			fileHOLDING_VOLUMES  = new File( pathHOLDING_VOLUMES );

			String pathINTEREST_VOLUMES = fileAdaptationPath + "/" +  "INTEREST_VOLUMES.CSV";
			fileINTEREST_VOLUMES  = new File( pathINTEREST_VOLUMES );
			
			String pathSECTOR_VOLUMES = fileAdaptationPath + "/" +  "SECTOR_VOLUMES.CSV";
			fileSECTOR_VOLUMES  = new File( pathSECTOR_VOLUMES );
			
			String pathSITUATION_LINE_POINTS = fileAdaptationPath + "/" +  "SITUATION_LINE_POINTS.CSV";
			fileSITUATION_LINE_POINTS  = new File( pathSITUATION_LINE_POINTS );
			
			String pathSITUATION_LINES_GUIDE = fileAdaptationPath + "/" +  "SITUATION_LINES_GUIDE.CSV";
			fileSITUATION_LINES_GUIDE  = new File( pathSITUATION_LINES_GUIDE );
			
			String pathSITUATION_LINE_CONDITIONS = fileAdaptationPath + "/" +  "SITUATION_LINE_CONDITIONS.CSV";
			fileSITUATION_LINE_CONDITIONS  = new File( pathSITUATION_LINE_CONDITIONS );

			String pathROUTE_CONDITIONS_GUIDE = fileAdaptationPath + "/" +  "ROUTE_CONDITIONS_GUIDE.CSV";
			fileROUTE_CONDITIONS_GUIDE  = new File( pathROUTE_CONDITIONS_GUIDE );
			
			String pathROUTE_CONDITION_AERODROMES = fileAdaptationPath + "/" +  "ROUTE_CONDITION_AERODROMES.CSV";
			fileROUTE_CONDITION_AERODROMES  = new File( pathROUTE_CONDITION_AERODROMES );
			
			String pathFIX_POINTS = fileAdaptationPath + "/" +  "FIXPOINTS_GUIDE.CSV";
			fileFIX_POINTS  = new File( pathFIX_POINTS );
		}
	}
	
	
	//"FIXID","DESCRIPTION","LATITUDE","LONGITUDE","SITUATION"
	public static class FIX_POINT implements Comparable<FIX_POINT>  {

		public String FIXID,DESCRIPTION,LATITUDE,LONGITUDE,SITUATION;
		
		public FIX_POINT () {}
		
		public FIX_POINT (String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");

			this.FIXID = attributes[0].trim();
			this.DESCRIPTION = attributes[1].trim();			
			this.LATITUDE = attributes[2].trim();
			this.LONGITUDE = attributes[3].trim();
		}
		@Override
		public int compareTo(FIX_POINT other) {
            return this.FIXID.compareTo( other.FIXID );
        }

		@Override
		public String toString() {
			return "[" 
					+ (FIXID != null ? "FIXID=" + FIXID + ", " : "") 
					+ (DESCRIPTION != null ? "DESCRIPTION=" + DESCRIPTION  + ", " : "") 
					+ (LATITUDE != null ? "LATITUDE=" + LATITUDE  + ", " : "") 
					+ (LONGITUDE != null ? "LONGITUDE=" + LONGITUDE  : "") 
					+ "]";
		}
	}
	
	//CONDITIONID,TERMINAL_ACTION,AERODROMES_REGION,ACCOUNTING
	public static class ROUTE_CONDITION_AERODROMES  {

		public String CONDITIONID,TERMINAL_ACTION,AERODROMES_REGION,ACCOUNTING;
		
		public ROUTE_CONDITION_AERODROMES () {}
		
		public ROUTE_CONDITION_AERODROMES (String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");

			this.CONDITIONID = attributes[0].trim();
			this.TERMINAL_ACTION = attributes[1].trim();			
			this.AERODROMES_REGION = attributes[2].trim();
			this.ACCOUNTING = attributes[3].trim();
		}

		@Override
		public String toString() {
			return "[" 
					+ (TERMINAL_ACTION != null ? "TERMINAL_ACTION=" + TERMINAL_ACTION + ", " : "") 
					+ (AERODROMES_REGION != null ? "AERODROMES_REGION=" + AERODROMES_REGION  : "") 
					+ "]";
		}
	}

		
	//CONDITIONID,MINIMUM_RFL,MAXIMUM_RFL,MINIMUM_SCL,MAXIMUM_SCL,BOUNDARY_SIDE,REACHING_MODE,CROSSING_LEVEL,FORCE_INDICATOR,MAINTAIN_AFTER
	public static class ROUTE_CONDITIONS_GUIDE  {

		public String CONDITIONID,MINIMUM_RFL,MAXIMUM_RFL,MINIMUM_SCL,MAXIMUM_SCL,BOUNDARY_SIDE,REACHING_MODE,CROSSING_LEVEL,FORCE_INDICATOR,MAINTAIN_AFTER;
		
		public ROUTE_CONDITIONS_GUIDE () {}
		
		public ROUTE_CONDITIONS_GUIDE (String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");

			this.CONDITIONID = attributes[0].trim();
			this.MINIMUM_RFL = attributes[1].trim();			
			this.MAXIMUM_RFL = attributes[2].trim();
			this.MINIMUM_SCL = attributes[3].trim();
			this.MAXIMUM_SCL = attributes[4].trim();
			this.BOUNDARY_SIDE = attributes[5].trim();
			this.REACHING_MODE = attributes[6].trim();
			this.CROSSING_LEVEL = attributes[7].trim();
			this.FORCE_INDICATOR = attributes[8].trim();
			this.MAINTAIN_AFTER = attributes[9].trim();
		}

		@Override
		public String toString() {
			return "[" + (CONDITIONID != null ? "CONDITIONID=" + CONDITIONID + ", " : "")
					+ (MINIMUM_RFL != null ? "MINIMUM_RFL=" + MINIMUM_RFL + ", " : "")
					+ (MAXIMUM_RFL != null ? "MAXIMUM_RFL=" + MAXIMUM_RFL + ", " : "")
					+ (MINIMUM_SCL != null ? "MINIMUM_SCL=" + MINIMUM_SCL + ", " : "")
					+ (MAXIMUM_SCL != null ? "MAXIMUM_SCL=" + MAXIMUM_SCL + ", " : "")
					+ (BOUNDARY_SIDE != null ? "BOUNDARY_SIDE=" + BOUNDARY_SIDE + ", " : "")
					+ (REACHING_MODE != null ? "REACHING_MODE=" + REACHING_MODE + ", " : "")
					+ (CROSSING_LEVEL != null ? "CROSSING_LEVEL=" + CROSSING_LEVEL + ", " : "")
					+ (FORCE_INDICATOR != null ? "FORCE_INDICATOR=" + FORCE_INDICATOR + ", " : "")
					+ (MAINTAIN_AFTER != null ? "MAINTAIN_AFTER=" + MAINTAIN_AFTER : "") + "]";
		}
	}	
	
	//SITUATION_LINEID,ROUTE_CONDITIONID,PRIORITY_NUMBER
	public static class SITUATION_LINE_CONDITIONS  {

		public String SITUATION_LINEID,ROUTE_CONDITIONID,PRIORITY_NUMBER;
		
		public SITUATION_LINE_CONDITIONS () {}
		
		public SITUATION_LINE_CONDITIONS (String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");
			
			this.SITUATION_LINEID = attributes[0].trim();
			this.ROUTE_CONDITIONID = attributes[1].trim();
			this.PRIORITY_NUMBER = attributes[2].trim();
									
		}

		@Override
		public String toString() {
			return "["
					+ (SITUATION_LINEID != null ? "SITUATION_LINEID=" + SITUATION_LINEID + ", " : "")
					+ (ROUTE_CONDITIONID != null ? "ROUTE_CONDITIONID=" + ROUTE_CONDITIONID + ", " : "")
					+ (PRIORITY_NUMBER != null ? "PRIORITY_NUMBER=" + PRIORITY_NUMBER : "") + "]";
		}
	}
	
	//SITUATION_LINEID,LONG_DESCRIPTION,AREA_CONTOURID,FIGURE_INDICATOR
	public static class SITUATION_LINES_GUIDE  {

		public String SITUATION_LINEID,LONG_DESCRIPTION,AREA_CONTOURID,FIGURE_INDICATOR;
		
		public SITUATION_LINES_GUIDE () {}
		
		public SITUATION_LINES_GUIDE (String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");
			
			this.SITUATION_LINEID= attributes[0].trim();
			this.LONG_DESCRIPTION = attributes[1].trim();
			this.AREA_CONTOURID = attributes[2].trim();
			this.FIGURE_INDICATOR = attributes[3].trim();						
		}

		@Override
		public String toString() {
			return "SITUATION_LINES_GUIDE ["
					+ (SITUATION_LINEID != null ? "SITUATION_LINEID=" + SITUATION_LINEID + ", " : "")
					+ (LONG_DESCRIPTION != null ? "LONG_DESCRIPTION=" + LONG_DESCRIPTION + ", " : "")
					+ (AREA_CONTOURID != null ? "AREA_CONTOURID=" + AREA_CONTOURID + ", " : "")
					+ (FIGURE_INDICATOR != null ? "FIGURE_INDICATOR=" + FIGURE_INDICATOR : "") + "]";
		}

	}	

	//SITUATION_LINEID,LOCATION_FORMAT,POINT_LOCATION,SEQUENCE_NUMBER,SEGMENT_COURSE,CIRCLE_RADIUS
	public static class SITUATION_LINE_POINTS  implements Comparable<SITUATION_LINE_POINTS> {

		public String SITUATION_LINEID,LOCATION_FORMAT,POINT_LOCATION,SEQUENCE_NUMBER,SEGMENT_COURSE,CIRCLE_RADIUS;
		
		public SITUATION_LINE_POINTS () {}
		
		public SITUATION_LINE_POINTS (String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");
			
			this.SITUATION_LINEID= attributes[0].trim();
			this.LOCATION_FORMAT = attributes[1].trim();
			this.POINT_LOCATION = attributes[2].trim();
			this.SEQUENCE_NUMBER = attributes[3].trim();
			this.SEGMENT_COURSE = attributes[4].trim();
			this.CIRCLE_RADIUS = attributes[5].trim();			
		}

		@Override
		public int compareTo(SITUATION_LINE_POINTS other) {
		    return Integer.compare(Integer.parseInt(this.SEQUENCE_NUMBER), Integer.parseInt ( other.SEQUENCE_NUMBER ));
		}
		
		public String toString() {
			return "["
					+ (SITUATION_LINEID != null ? "SITUATION_LINEID=" + SITUATION_LINEID + ", " : "")
					+ (LOCATION_FORMAT != null ? "LOCATION_FORMAT=" + LOCATION_FORMAT + ", " : "")
					+ (POINT_LOCATION != null ? "POINT_LOCATION=" + POINT_LOCATION + ", " : "")
					+ (SEQUENCE_NUMBER != null ? "SEQUENCE_NUMBER=" + SEQUENCE_NUMBER + ", " : "")
					+ (SEGMENT_COURSE != null ? "SEGMENT_COURSE=" + SEGMENT_COURSE + ", " : "")
					+ (CIRCLE_RADIUS != null ? "CIRCLE_RADIUS=" + CIRCLE_RADIUS : "") + "]";
		}
	}	
	

	//SECTOR_VOLUMEID,LONG_DESCRIPTION,ASSIGNMENT_MODE
	public class SECTOR_VOLUME {		

		public String SECTOR_VOLUMEID;
		public String LONG_DESCRIPTION;		
		public String ASSIGNMENT_MODE;
					
		public SECTOR_VOLUME  () {}		
		public SECTOR_VOLUME(String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");
			
			this.SECTOR_VOLUMEID = attributes[0].trim();
			this.LONG_DESCRIPTION = attributes[1].trim();			
			this.ASSIGNMENT_MODE = attributes[2].trim();
		}
		
		@Override
		public String toString() {
			return "SECTOR_VOLUME [" + (SECTOR_VOLUMEID != null ? "SECTOR_VOLUMEID=" + SECTOR_VOLUMEID + ", " : "")
					+ (LONG_DESCRIPTION != null ? "LONG_DESCRIPTION=" + LONG_DESCRIPTION + ", " : "")
					+ (ASSIGNMENT_MODE != null ? "ASSIGNMENT_MODE=" + ASSIGNMENT_MODE : "") + "]";
		}
	}	
	
		
	//HOLDING_VOLUMEID;LONG_DESCRIPTION;HOLDING_FIXID;INBOUND_COURSE;TURN_DIRECTION;LEG_DURATION;PATTERN_TIME;DEFAULT_USAGE
	public static class HOLDING_VOLUME {		

		public String HOLDING_VOLUMEID;
		public String LONG_DESCRIPTION;		
		public String HOLDING_FIXID;
		public String INBOUND_COURSE;
		public String TURN_DIRECTION;
		public String LEG_DURATION;
		public String PATTERN_TIME;
		public String DEFAULT_USAGE;
					
		public HOLDING_VOLUME  () {}
		
		public HOLDING_VOLUME(String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");
			
			this.HOLDING_VOLUMEID = attributes[0].trim();
			this.LONG_DESCRIPTION = attributes[1].trim();			
			this.HOLDING_FIXID = attributes[2].trim();
			this.INBOUND_COURSE = attributes[3].trim();
			this.TURN_DIRECTION = attributes[4].trim();
			this.LEG_DURATION = attributes[5].trim();
			this.PATTERN_TIME = attributes[6].trim();
			this.DEFAULT_USAGE = attributes[7].trim();
		}
		
		@Override
		public String toString() {
			return "HOLDING_VOLUME [" + (HOLDING_VOLUMEID != null ? "HOLDING_VOLUMEID=" + HOLDING_VOLUMEID + ", " : "")
					+ (LONG_DESCRIPTION != null ? "LONG_DESCRIPTION=" + LONG_DESCRIPTION + ", " : "")
					+ (HOLDING_FIXID != null ? "HOLDING_FIXID=" + HOLDING_FIXID + ", " : "")
					+ (INBOUND_COURSE != null ? "INBOUND_COURSE=" + INBOUND_COURSE + ", " : "")
					+ (TURN_DIRECTION != null ? "TURN_DIRECTION=" + TURN_DIRECTION + ", " : "")
					+ (LEG_DURATION != null ? "LEG_DURATION=" + LEG_DURATION + ", " : "")
					+ (PATTERN_TIME != null ? "PATTERN_TIME=" + PATTERN_TIME + ", " : "")
					+ (DEFAULT_USAGE != null ? "DEFAULT_USAGE=" + DEFAULT_USAGE : "") + "]";
		}		
	}	
	
	//AIRSPACE_VOLUMEID;HOLDING_VOLUMEID
	public static class HOLDING_AIRSPACE_VOLUME {

		public String AIRSPACE_VOLUMEID;
		public String HOLDING_VOLUMEID;
		
		public HOLDING_AIRSPACE_VOLUME  () {}
		
		public HOLDING_AIRSPACE_VOLUME(String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");
			
			this.AIRSPACE_VOLUMEID = attributes[0].trim();
			this.HOLDING_VOLUMEID = attributes[1].trim();			
		}
		
		@Override
		public String toString() {
			return "HOLDING_AIRSPACE_VOLUME ["
					+ (AIRSPACE_VOLUMEID != null ? "AIRSPACE_VOLUMEID=" + AIRSPACE_VOLUMEID + ", " : "")
					+ (HOLDING_VOLUMEID != null ? "HOLDING_VOLUMEID=" + HOLDING_VOLUMEID : "") + "]";
		}		
	}
	
	
	
	
	//AIRSPACE_VOLUMEID	LONG_DESCRIPTION	AREA_CONTOURID	BAROM_STATIONID	LIMITS_MAGNITUDE	LOWER_FACE_LIMIT	UPPER_FACE_LIMIT
	public static class AIRSPACE_VOLUME {
		public String AIRSPACE_VOLUMEID	;
		public String LONG_DESCRIPTION;
		public String AREA_CONTOURID;
		public String BAROM_STATIONID	;
		public String LIMITS_MAGNITUDE	;
		public String LOWER_FACE_LIMIT;
		public String UPPER_FACE_LIMIT;		
		
		public AIRSPACE_VOLUME () {}
		
		public AIRSPACE_VOLUME (String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");
			
			this.AIRSPACE_VOLUMEID = attributes[0].trim();
			this.LONG_DESCRIPTION = attributes[1].trim();
			this.AREA_CONTOURID = attributes[2].trim();
			this.BAROM_STATIONID = attributes[3].trim();
			this.LIMITS_MAGNITUDE = attributes[4].trim();
			this.LOWER_FACE_LIMIT = attributes[5].trim();
			this.UPPER_FACE_LIMIT = attributes[6].trim();    
		}
		
		public String toString (){
			String objectString;
			objectString = AIRSPACE_VOLUMEID + ",";
			objectString += LONG_DESCRIPTION + ",";
			objectString += AREA_CONTOURID + ",";
			objectString += BAROM_STATIONID + ",";
			objectString += LIMITS_MAGNITUDE + ",";
			objectString += LOWER_FACE_LIMIT + ",";
			objectString += UPPER_FACE_LIMIT + ",";
			
			return objectString;
		}
	}
	//AREA_CONTOURID	LOCATION_FORMAT	POINT_LOCATION	SEQUENCE_NUMBER	SEGMENT_COURSE	CIRCLE_RADIUS
	public static class AREA_CONTOUR_POINT implements Comparable<AREA_CONTOUR_POINT> {
		public String AREA_CONTOURID	;
		public String LOCATION_FORMAT;
		public String POINT_LOCATION;
		public String SEQUENCE_NUMBER	;
		public String SEGMENT_COURSE	;
		public String CIRCLE_RADIUS;
		
		public AREA_CONTOUR_POINT () {}
		
		public AREA_CONTOUR_POINT (String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");
			
			this.AREA_CONTOURID = attributes[0].trim();
			this.LOCATION_FORMAT = attributes[1].trim();
			this.POINT_LOCATION = attributes[2].trim();
			this.SEQUENCE_NUMBER = attributes[3].trim();
			this.SEGMENT_COURSE = attributes[4].trim();
			this.CIRCLE_RADIUS = attributes[5].trim();
		}
		
		@Override
		public int compareTo(AREA_CONTOUR_POINT other) {
		    return Integer.compare(Integer.parseInt(this.SEQUENCE_NUMBER), Integer.parseInt ( other.SEQUENCE_NUMBER ));
		}
		
		public String toString (){
			String objectString;
			objectString = AREA_CONTOURID + ",";
			objectString += LOCATION_FORMAT + ",";
			objectString += POINT_LOCATION + ",";
			objectString += SEQUENCE_NUMBER + ",";
			objectString += SEGMENT_COURSE + ",";
			objectString += CIRCLE_RADIUS + ",";
			
			return objectString;
		}
	}
	
	//BASIC_SECTORID	TYPE_INDICATOR	PRIORITY_NUMBER	MIN_CROSS_TIME	HAND_OVER_MODE	FORCE_DISTANCE SECTOR_FLAVOURID SECTOR_VOLUMEID
	public static class BASIC_SECTORS  implements Comparable<BASIC_SECTORS> {

		public String BASIC_SECTORID	;
		public String TYPE_INDICATOR;
		public String PRIORITY_NUMBER;
		public String MIN_CROSS_TIME	;
		public String HAND_OVER_MODE	;
		public String FORCE_DISTANCE;
		public String SECTOR_VOLUMEID;
		public String SECTOR_FLAVOURID;
		public String reference;
		
		public BASIC_SECTORS () {}
		
		public BASIC_SECTORS (String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");
			
			this.BASIC_SECTORID = attributes[0].trim();
			this.TYPE_INDICATOR = attributes[1].trim();
			this.PRIORITY_NUMBER = attributes[2].trim();
			this.MIN_CROSS_TIME = attributes[3].trim();
			this.HAND_OVER_MODE = attributes[4].trim();
			this.FORCE_DISTANCE = attributes[5].trim();			
			this.SECTOR_FLAVOURID = attributes[6].trim();
			this.SECTOR_VOLUMEID = attributes[7].trim();
		}
		
		@Override
		public String toString() {
			return "BASIC_SECTORS [BASIC_SECTORID=" + BASIC_SECTORID + ", TYPE_INDICATOR=" + TYPE_INDICATOR
					+ ", PRIORITY_NUMBER=" + PRIORITY_NUMBER + ", MIN_CROSS_TIME=" + MIN_CROSS_TIME
					+ ", HAND_OVER_MODE=" + HAND_OVER_MODE + ", FORCE_DISTANCE=" + FORCE_DISTANCE + ", SECTOR_VOLUMEID="
					+ SECTOR_VOLUMEID + ", SECTOR_FLAVOURID=" + SECTOR_FLAVOURID + "]";
		}
		@Override
		public int compareTo(BASIC_SECTORS other) {
		    return Integer.compare(Integer.parseInt(this.reference), Integer.parseInt ( other.reference ));
		}
	}

	//AIRSPACE_VOLUMEID	SECTOR_VOLUMEID	
	public class INTEREST_VOLUMES  {

		public String AIRSPACE_VOLUMEID;
		public String SECTOR_VOLUMEID;
		
		public INTEREST_VOLUMES  () {}
		
		public INTEREST_VOLUMES(String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");
			
			this.AIRSPACE_VOLUMEID = attributes[0].trim();
			this.SECTOR_VOLUMEID = attributes[1].trim();			
		}

		@Override
		public String toString() {
			return "INTEREST_VOLUMES ["
					+ (AIRSPACE_VOLUMEID != null ? "AIRSPACE_VOLUMEID=" + AIRSPACE_VOLUMEID + ", " : "")
					+ (SECTOR_VOLUMEID != null ? "SECTOR_VOLUMEID=" + SECTOR_VOLUMEID : "") + "]";
		}
		
	}
	
	//AIRSPACE_VOLUMEID	SECTOR_VOLUMEID	
	public static class RESPONSIBILITY_VOLUMES  {

		public String AIRSPACE_VOLUMEID;
		public String SECTOR_VOLUMEID;
		
		public RESPONSIBILITY_VOLUMES  () {}
		
		public RESPONSIBILITY_VOLUMES(String line) {
	
			String lineFormatted = line.replaceAll("\"", "");
			String[] attributes = lineFormatted.split(",");
			
			this.AIRSPACE_VOLUMEID = attributes[0].trim();
			this.SECTOR_VOLUMEID = attributes[1].trim();			
		}
		
		@Override
		public String toString() {
			return "RESPONSIBILITY_VOLUMES [AIRSPACE_VOLUMEID=" + AIRSPACE_VOLUMEID + ", SECTOR_VOLUMEID="
					+ SECTOR_VOLUMEID + "]";
		}
	}
	
	
	public List<FIX_POINT> getFIX_POINTs() {
		
		List<FIX_POINT> list = new ArrayList<FIX_POINT>();
		
		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
				String[] FIX_POINTS_ids = i.getVar("FIX(names)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = FIX_POINTS_ids.length; j < len; j++) {    				
    				FIX_POINT fix = new FIX_POINT();
    				fix.FIXID = FIX_POINTS_ids[j];
    				String geodesic = i.getVar("FIX", FIX_POINTS_ids[j] + "," + "geodesic", 0).toString();    				
    				Pattern r = Pattern.compile("(\\d{2}\\d{2}\\d{2}(N|S))(\\d{3}\\d{2}\\d{2}(W|E))");
    				Matcher m = r.matcher( geodesic );
    				while (m.find()) {	    	   
    					  fix.LATITUDE	=	m.group(1) != null ? m.group(1) : "";
    					  fix.LONGITUDE =	m.group(3) != null ? m.group(3) : "";
    				}    				
					list.add(fix);    				
    			}    			
			 } catch (TclException e) {
				 System.out.println("Exception on getFIX_POINTs: " + e.getMessage());
				 int code = e.getCompletionCode();
				 System.err.println("command returned bad error code: " + code);		         
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){
			File inputFile = fileFIX_POINTS;		
	        try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		FIX_POINT asp = new FIX_POINT( line );
	        		list.add( asp );	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}    
		}
		return list;
	}	

	
	//ROUTE_CONDITION_AERODROMES
	public List<ROUTE_CONDITION_AERODROMES> getROUTE_CONDITION_AERODROMES() {
		
		List<ROUTE_CONDITION_AERODROMES> list = new ArrayList<ROUTE_CONDITION_AERODROMES>();
		
		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
    			i.eval("array names CST_CONDITION *,arr");
    			String[] CST_CONDITION_keys = i.getResult().toString().split(" ");
    			for (int j=0, len=CST_CONDITION_keys.length; j < len; j++ ) {
    				String[] tokens = CST_CONDITION_keys[j].split(",");
    				if (tokens.length != 2) continue;
    				String index = tokens[0];
    				String[] arrivals = i.getVar("CST_CONDITION", index + "," + "arr", 0).toString().split(" ");;
        			for (int k=0, size=arrivals.length; k < size; k++ ) {
        				ROUTE_CONDITION_AERODROMES rca = new ROUTE_CONDITION_AERODROMES();
        				rca.CONDITIONID = index;
        				rca.TERMINAL_ACTION = "A";
        				rca.AERODROMES_REGION = arrivals[k];
        				list.add(rca);
        			}
    			}
    			i.eval("array names CST_CONDITION *,dep");
    			CST_CONDITION_keys = i.getResult().toString().split(" ");
    			for (int j=0, len=CST_CONDITION_keys.length; j < len; j++ ) {
    				String[] tokens = CST_CONDITION_keys[j].split(",");
    				if (tokens.length != 2) continue;
    				String index = tokens[0];
    				String[] departures = i.getVar("CST_CONDITION", index + "," + "dep", 0).toString().split(" ");;
        			for (int k=0, size=departures.length; k < size; k++ ) {
        				ROUTE_CONDITION_AERODROMES rca = new ROUTE_CONDITION_AERODROMES();
        				rca.CONDITIONID = index;
        				rca.TERMINAL_ACTION = "D";
        				rca.AERODROMES_REGION = departures[k];
        				list.add(rca);
        			}
    			}
			 } catch (TclException e) {
				 System.out.println("Exception on getROUTE_CONDITION_AERODROMES: " + e.getMessage());
				 int code = e.getCompletionCode();
				 System.err.println("command returned bad error code: " + code);		         
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){
			File inputFile = fileROUTE_CONDITION_AERODROMES;		
	        try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		ROUTE_CONDITION_AERODROMES asp = new ROUTE_CONDITION_AERODROMES( line );
	        		list.add( asp );	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}    
		}
		return list;
	}	
	
	
	
	//ROUTE_CONDITIONS_GUIDE
	public List<ROUTE_CONDITIONS_GUIDE> getROUTE_CONDITIONS_GUIDE() {
		
		List<ROUTE_CONDITIONS_GUIDE> list = new ArrayList<ROUTE_CONDITIONS_GUIDE>();
		
		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
    			i.eval("array names CST_CONDITION");
    			String[] CST_CONDITION_keys = i.getResult().toString().split(" ");
    			Map<String, ROUTE_CONDITIONS_GUIDE> map = new HashMap<>();
    			for (int j=0, len=CST_CONDITION_keys.length; j < len; j++ ) {
    				String[] tokens = CST_CONDITION_keys[j].split(",");
    				if (tokens.length != 2) continue;
    				String index = tokens[0];
    				ROUTE_CONDITIONS_GUIDE rcg = map.get(index);
        			if (rcg == null ) {
        				rcg = new ROUTE_CONDITIONS_GUIDE();
        				rcg.CONDITIONID = index;
        				rcg.MINIMUM_RFL = i.getVar("CST_CONDITION", index + "," + "rflmin", 0).toString();
        				rcg.MAXIMUM_RFL = i.getVar("CST_CONDITION", index + "," + "rflmax", 0).toString();
        				rcg.MINIMUM_SCL = i.getVar("CST_CONDITION", index + "," + "sclmin", 0).toString();
        				rcg.MAXIMUM_SCL = i.getVar("CST_CONDITION", index + "," + "sclmax", 0).toString();    			
        				rcg.BOUNDARY_SIDE = i.getVar("CST_CONDITION", index + "," + "bside", 0).toString();
        				rcg.REACHING_MODE = i.getVar("CST_CONDITION", index + "," + "rWay", 0).toString();
        				rcg.CROSSING_LEVEL = i.getVar("CST_CONDITION", index + "," + "value", 0).toString();
        				rcg.FORCE_INDICATOR = i.getVar("CST_CONDITION", index + "," + "force", 0).toString();
        				rcg.MAINTAIN_AFTER = i.getVar("CST_CONDITION", index + "," + "maint", 0).toString();        				
        				map.put(index, rcg);
        			}
    			}
    			for (ROUTE_CONDITIONS_GUIDE rcg : map.values()) {
    				list.add(rcg);
    			}
			 } catch (TclException e) {
				 System.out.println("Exception on getROUTE_CONDITIONS_GUIDE: " + e.getMessage());
				 int code = e.getCompletionCode();
				 System.err.println("command returned bad error code: " + code);		         
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){
			File inputFile = fileROUTE_CONDITIONS_GUIDE;		
	        try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		ROUTE_CONDITIONS_GUIDE asp = new ROUTE_CONDITIONS_GUIDE( line );
	        		list.add( asp );	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}    
		}
		return list;
	}
	
	
	
	//SITUATION_LINE_CONDITIONS
	public List<SITUATION_LINE_CONDITIONS> getSITUATION_LINE_CONDITIONS () {
		
		List<SITUATION_LINE_CONDITIONS> list = new ArrayList<SITUATION_LINE_CONDITIONS>();
		
		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
				String[] CST_LINES__ids = i.getVar("CST_LINES(names)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = CST_LINES__ids.length; j < len; j++) {    				
    				String[] routeConditionId = i.getVar("CST_LINES", CST_LINES__ids[j], 0).toString().split(" ");
    				for (int k = 0, size = routeConditionId.length;k < size; k++  ) {
    					SITUATION_LINE_CONDITIONS slc = new SITUATION_LINE_CONDITIONS();
        				slc.SITUATION_LINEID = CST_LINES__ids[j];
        				slc.ROUTE_CONDITIONID = routeConditionId[k];    				
    					list.add(slc);
    				}    				    				
    			}    			
			 } catch (TclException e) {
				 System.out.println("Exception on getSITUATION_LINE_CONDITIONS: " + e.getMessage());
				 int code = e.getCompletionCode();
		         switch (code) {
		         	case TCL.ERROR:
		         		System.err.println(i.getResult().toString());
		                break;
		         	case TCL.BREAK:
		            	System.err.println("invoked \"break\" outside of a loop");
		            	break;
		         	case TCL.CONTINUE:
		         		System.err.println("invoked \"continue\" outside of a loop");
		         		break;
		         	default:
		         		System.err.println("command returned bad error code: " + code);
		         		break;
		         }
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){
			File inputFile = fileSITUATION_LINE_CONDITIONS;
	        try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		SITUATION_LINE_CONDITIONS asp = new SITUATION_LINE_CONDITIONS( line );
	        		list.add( asp );	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}    
		}
		return list;
	}
	
	//SITUATION_LINES_GUIDE
	public List<SITUATION_LINES_GUIDE> getSITUATION_LINES_GUIDEs () {
		
		List<SITUATION_LINES_GUIDE> list = new ArrayList<SITUATION_LINES_GUIDE>();
		
		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
				String[] CST_LINES__ids = i.getVar("CST_LINES(names)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = CST_LINES__ids.length; j < len; j++) {    				
    				SITUATION_LINES_GUIDE slg = new SITUATION_LINES_GUIDE();
    				slg.SITUATION_LINEID = CST_LINES__ids[j];
    				slg.FIGURE_INDICATOR = i.getVar("CST_LINES", CST_LINES__ids[j] + "," + "figure", 0).toString();    				
					list.add(slg);    				
    			}    			
			 } catch (TclException e) {
				 System.out.println("Exception: " + e.getMessage());				 
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){
			File inputFile = fileSITUATION_LINES_GUIDE;
	        try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		SITUATION_LINES_GUIDE asp = new SITUATION_LINES_GUIDE( line );
	        		list.add( asp );	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}    
		}
		return list;
	}
	
	
	//HOLDING_VOLUME
	public List<HOLDING_VOLUME> getHOLDING_VOLUMEs () {
		
		List<HOLDING_VOLUME> ListHOLDING_VOLUMEs = new ArrayList<HOLDING_VOLUME>();

		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
				String[] HOLD_VOLUMES__ids = i.getVar("HOLD_VOLUMES(ids)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = HOLD_VOLUMES__ids.length; j < len; j++) {
    				HOLDING_VOLUME hv = new HOLDING_VOLUME();
    				hv.HOLDING_VOLUMEID = i.getVar("HOLD_VOLUMES", HOLD_VOLUMES__ids[j] + "," + "sectorvolid", 0).toString();
    				hv.HOLDING_FIXID =  i.getVar("HOLD_VOLUMES", HOLD_VOLUMES__ids[j] + "," + "fix", 0).toString();
    				
    				ListHOLDING_VOLUMEs.add(hv);
    		    }    			
			 } catch (TclException e) {
				 System.out.println("Exception on getHOLDING_VOLUMEs: " + e.getMessage());				 
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){		
			File inputFile = fileHOLDING_VOLUMES;
			
	        try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		HOLDING_VOLUME asp = new HOLDING_VOLUME( line );
	        		ListHOLDING_VOLUMEs.add( asp );	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}    
		}
		return ListHOLDING_VOLUMEs;
	}
	

	public List<SITUATION_LINE_POINTS> getSITUATION_LINE_POINTS() {
		
		List<SITUATION_LINE_POINTS> list = new ArrayList<SITUATION_LINE_POINTS>();
		
		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
				String[] CST_LINES__ids = i.getVar("CST_LINES(names)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = CST_LINES__ids.length; j < len; j++) {    				
    				String[] geodes = i.getVar("CST_LINES", CST_LINES__ids[j] + "," + "geodes", 0).toString().split(" ");
    				for (int k = 0, size = geodes.length;k < size; k++  ) {
    					SITUATION_LINE_POINTS slp = new SITUATION_LINE_POINTS();
        				slp.SITUATION_LINEID = CST_LINES__ids[j];
    					slp.LOCATION_FORMAT =  i.getVar("CST_LINES", CST_LINES__ids[j] + "," + "pformat", 0).toString();
    					slp.POINT_LOCATION = geodes[k];
    					slp.SEQUENCE_NUMBER = String.valueOf(k+1);
    					slp.SEGMENT_COURSE = i.getVar("CST_LINES", CST_LINES__ids[j] + "," + "figure", 0).toString();
    					//slp.CIRCLE_RADIUS = "0";
    					list.add(slp);
    				}
    			}    			
			 } catch (TclException e) {
				 System.out.println("Exception on getSITUATION_LINE_POINTS: " + e.getMessage());				 
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){
			File inputFile = this.fileSITUATION_LINE_POINTS;
	        try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		SITUATION_LINE_POINTS entry = new SITUATION_LINE_POINTS( line );
	        		list.add( entry );	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}    
		}
		return list;
	}

	
	
	public List<HOLDING_AIRSPACE_VOLUME> getHOLDING_AIRSPACE_VOLUMEs () {
		
		List<HOLDING_AIRSPACE_VOLUME> ListHOLDING_AIRSPACEs = new ArrayList<HOLDING_AIRSPACE_VOLUME>();

		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
				String[] HOLD_VOLUMES__ids = i.getVar("HOLD_VOLUMES(ids)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = HOLD_VOLUMES__ids.length; j < len; j++) {
    				HOLDING_AIRSPACE_VOLUME hav = new HOLDING_AIRSPACE_VOLUME();
    				hav.AIRSPACE_VOLUMEID = HOLD_VOLUMES__ids[j];
    				hav.HOLDING_VOLUMEID = i.getVar("HOLD_VOLUMES", hav.AIRSPACE_VOLUMEID + "," + "sectorvolid", 0).toString();
    				ListHOLDING_AIRSPACEs.add(hav);
    		    }    			
			 } catch (TclException e) {
				 System.out.println("Exception on getHOLDING_AIRSPACE_VOLUMEs: " + e.getMessage());				 
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){		
			File inputFile = fileHOLDING_AIRSPACE_VOLUMES;			
	        try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		HOLDING_AIRSPACE_VOLUME asp = new HOLDING_AIRSPACE_VOLUME( line );
	        		ListHOLDING_AIRSPACEs.add( asp );	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return ListHOLDING_AIRSPACEs;
	}		

	
	public List<SECTOR_VOLUME> getSECTOR_VOLUMES() {
		
		List<SECTOR_VOLUME> list = new ArrayList<SECTOR_VOLUME>();

		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
				i.eval("array names AOR_VOLUMES *assignment");
    			String[] AOR_VOLUMES_keys = i.getResult().toString().split(" ");
    			for (int j = 0, len = AOR_VOLUMES_keys.length; j < len; j++) {
    				String[] tokens = AOR_VOLUMES_keys[j].split(",");
    				SECTOR_VOLUME sv = new SECTOR_VOLUME();
    				sv.SECTOR_VOLUMEID = tokens[0];
    				sv.ASSIGNMENT_MODE = i.getVar("AOR_VOLUMES", tokens[0] + "," + "assignment", 0).toString(); 
    				list.add(sv);
    		    }    			
			 } catch (TclException e) {
				 System.out.println("Exception on getSECTOR_VOLUMES: " + e.getMessage());	
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){		
			File inputFile = this.fileSECTOR_VOLUMES;
	        try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		SECTOR_VOLUME entry = new SECTOR_VOLUME( line );
	        		list.add( entry );	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}    
		}
		return list;
	}
	
	public List<INTEREST_VOLUMES> getINTEREST_VOLUMES (  ) {

		List<INTEREST_VOLUMES> list = new ArrayList<INTEREST_VOLUMES>();
		
		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
				String[] INT_VOLUMES__ids = i.getVar("INT_VOLUMES(ids)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = INT_VOLUMES__ids.length; j < len; j++) {
    				INTEREST_VOLUMES iv = new INTEREST_VOLUMES();
    				iv.AIRSPACE_VOLUMEID = INT_VOLUMES__ids[j];
    				iv.SECTOR_VOLUMEID = i.getVar("INT_VOLUMES", iv.AIRSPACE_VOLUMEID + "," + "sectorvolid", 0).toString();    				
    				list.add(iv);
    		    }    			
			 } catch (TclException e) {
				 System.out.println("Exception on getINTEREST_VOLUMES: " + e.getMessage());				 
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){
			File inputFile = this.fileINTEREST_VOLUMES;
		    try {	        	
		    	List<String> lines = Files.readAllLines( inputFile.toPath() );
		    	for ( String line : lines.subList( 1, lines.size()) ) {
		    		INTEREST_VOLUMES entry = new INTEREST_VOLUMES( line );
		    		list.add( entry );	        		
		    	}
		    }			
			catch (IOException e) {
				e.printStackTrace();
			}    
		}
		return list;
	}

	
	
	public List<RESPONSIBILITY_VOLUMES> getRESPONSIBILITY_VOLUMES (  ) {
				
		List<RESPONSIBILITY_VOLUMES> list = new ArrayList<RESPONSIBILITY_VOLUMES>();
		
		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
				String[] AOR_VOLUMES__ids = i.getVar("AOR_VOLUMES(ids)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = AOR_VOLUMES__ids.length; j < len; j++) {
    				RESPONSIBILITY_VOLUMES respVolume = new RESPONSIBILITY_VOLUMES();
    				respVolume.AIRSPACE_VOLUMEID = AOR_VOLUMES__ids[j];
    				respVolume.SECTOR_VOLUMEID = i.getVar("AOR_VOLUMES", respVolume.AIRSPACE_VOLUMEID + "," + "sectorvolid", 0).toString();
    				list.add(respVolume);
    		    }    			
			 } catch (TclException e) {
				 System.out.println("Exception getRESPONSIBILITY_VOLUMES: " + e.getMessage() + "\n");
				 int code = e.getCompletionCode();
		         switch (code) {
		         	case TCL.ERROR:
		         		System.err.println(i.getResult().toString());
		                break;
		         	case TCL.BREAK:
		            	System.err.println("invoked \"break\" outside of a loop");
		            	break;
		         	case TCL.CONTINUE:
		         		System.err.println("invoked \"continue\" outside of a loop");
		         		break;
		         	default:
		         		System.err.println("command returned bad error code: " + code);
		         		break;
		         }
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){
			File inputFile = this.fileRESPONSIBILITY_VOLUMES;
			try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		RESPONSIBILITY_VOLUMES entry = new RESPONSIBILITY_VOLUMES( line );
	        		list.add( entry );	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}    
        }

		return list;
	}
	
	public List<AIRSPACE_VOLUME> getAIRSPACE_VOLUMEs () {
			
		List<AIRSPACE_VOLUME> ListAIRSPACE_VOLUME = new ArrayList<AIRSPACE_VOLUME>();
				
		if ( AdaptationSource.equals("environment.tcl") ) {
			try {
				String[] AOR_VOLUMES__ids = i.getVar("AOR_VOLUMES(ids)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = AOR_VOLUMES__ids.length; j < len; j++) {
    				AIRSPACE_VOLUME airSpaceVolume = new AIRSPACE_VOLUME();
    				airSpaceVolume.AIRSPACE_VOLUMEID = AOR_VOLUMES__ids[j];
    				airSpaceVolume.AREA_CONTOURID = airSpaceVolume.AIRSPACE_VOLUMEID; // this is a shit ....
    				airSpaceVolume.LOWER_FACE_LIMIT = i.getVar("AOR_VOLUMES", airSpaceVolume.AIRSPACE_VOLUMEID + "," + "lower", TCL.OK).toString();
    				airSpaceVolume.UPPER_FACE_LIMIT = i.getVar("AOR_VOLUMES", airSpaceVolume.AIRSPACE_VOLUMEID + "," + "upper", TCL.OK).toString();    				
    				ListAIRSPACE_VOLUME.add(airSpaceVolume);
    		    }
    			String[] INT_VOLUMES__ids = i.getVar("INT_VOLUMES(ids)", TCL.OK).toString().split(" ");
     			for (int j = 0, len = INT_VOLUMES__ids.length; j < len; j++) {
     				AIRSPACE_VOLUME airSpaceVolume = new AIRSPACE_VOLUME();
     				airSpaceVolume.AIRSPACE_VOLUMEID = INT_VOLUMES__ids[j];
     				airSpaceVolume.AREA_CONTOURID = airSpaceVolume.AIRSPACE_VOLUMEID; // this is a shit ....
     				airSpaceVolume.LOWER_FACE_LIMIT = i.getVar("INT_VOLUMES", airSpaceVolume.AIRSPACE_VOLUMEID + "," + "lower", TCL.OK).toString();
     				airSpaceVolume.UPPER_FACE_LIMIT = i.getVar("INT_VOLUMES", airSpaceVolume.AIRSPACE_VOLUMEID + "," + "upper", TCL.OK).toString();    				
     				ListAIRSPACE_VOLUME.add(airSpaceVolume);
     		    }    			 
     			String[] SUA_VOLUMES__ids = i.getVar("SUA_VOLUMES(ids)", TCL.OK).toString().split(" ");
     			for (int j = 0, len = SUA_VOLUMES__ids.length; j < len; j++) {
     				AIRSPACE_VOLUME airSpaceVolume = new AIRSPACE_VOLUME();
     				airSpaceVolume.AIRSPACE_VOLUMEID = SUA_VOLUMES__ids[j];
     				airSpaceVolume.AREA_CONTOURID = airSpaceVolume.AIRSPACE_VOLUMEID; // this is a shit ....
     				airSpaceVolume.LOWER_FACE_LIMIT = i.getVar("SUA_VOLUMES", airSpaceVolume.AIRSPACE_VOLUMEID + "," + "lower", TCL.OK).toString();
     				airSpaceVolume.UPPER_FACE_LIMIT = i.getVar("SUA_VOLUMES", airSpaceVolume.AIRSPACE_VOLUMEID + "," + "upper", TCL.OK).toString();    				
     				ListAIRSPACE_VOLUME.add(airSpaceVolume);
     		    }
     			String[] HOLD_VOLUMES__ids = i.getVar("HOLD_VOLUMES(ids)", TCL.OK).toString().split(" ");
     			for (int j = 0, len = HOLD_VOLUMES__ids.length; j < len; j++) {
     				AIRSPACE_VOLUME airSpaceVolume = new AIRSPACE_VOLUME();
     				airSpaceVolume.AIRSPACE_VOLUMEID = HOLD_VOLUMES__ids[j];
     				airSpaceVolume.AREA_CONTOURID = airSpaceVolume.AIRSPACE_VOLUMEID; // this is a shit ....
     				airSpaceVolume.LOWER_FACE_LIMIT = i.getVar("HOLD_VOLUMES", airSpaceVolume.AIRSPACE_VOLUMEID + "," + "lower", TCL.OK).toString();
     				airSpaceVolume.UPPER_FACE_LIMIT = i.getVar("HOLD_VOLUMES", airSpaceVolume.AIRSPACE_VOLUMEID + "," + "upper", TCL.OK).toString();    				
     				ListAIRSPACE_VOLUME.add(airSpaceVolume);
     		    }
    			
			 } catch (TclException e) {
				 System.out.println("Exception: " + e.getMessage());				 
			 }
		}else if(AdaptationSource.equals("iTAP_CSVs")){
			File inputFile = fileAIRSPACE_VOLUMES;
			try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		AIRSPACE_VOLUME airSpaceVolume = new AIRSPACE_VOLUME( line );
	        		ListAIRSPACE_VOLUME.add(airSpaceVolume);	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}
		}   

		return ListAIRSPACE_VOLUME;
	}
	
    public List<AREA_CONTOUR_POINT> getAREA_CONTOUR_POINTS (  ) {
    	
		List<AREA_CONTOUR_POINT> ListAREA_CONTOUR_POINT= new ArrayList<AREA_CONTOUR_POINT>();

		if ( AdaptationSource.equals("environment.tcl") ) {
    		try {
                String[] AOR_VOLUMES__ids = i.getVar("AOR_VOLUMES(ids)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = AOR_VOLUMES__ids.length; j < len; j++) {    				
    				String[] points = i.getVar("AOR_VOLUMES", AOR_VOLUMES__ids[j] + "," + "geodes", TCL.OK).toString().split(" ");
    				for (int k = 0, size = points.length;k < size; k++  ) {
        				AREA_CONTOUR_POINT point = new AREA_CONTOUR_POINT();
        				point.AREA_CONTOURID = AOR_VOLUMES__ids[j];
        				point.LOCATION_FORMAT = "G"; 
        				point.POINT_LOCATION = points[k];
        				point.SEQUENCE_NUMBER = String.valueOf(k+1);  				
        				ListAREA_CONTOUR_POINT.add(point);	
    				}    				
    		    }
                String[] INT_VOLUMES__ids = i.getVar("INT_VOLUMES(ids)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = INT_VOLUMES__ids.length; j < len; j++) {    				
    				String[] points = i.getVar("INT_VOLUMES", INT_VOLUMES__ids[j] + "," + "geodes", TCL.OK).toString().split(" ");
    				for (int k = 0, size = points.length;k < size; k++  ) {
        				AREA_CONTOUR_POINT point = new AREA_CONTOUR_POINT();
        				point.AREA_CONTOURID = INT_VOLUMES__ids[j];
        				point.LOCATION_FORMAT = "G"; 
        				point.POINT_LOCATION = points[k];
        				point.SEQUENCE_NUMBER = String.valueOf(k+1);  				
        				ListAREA_CONTOUR_POINT.add(point);	
    				}    				
    		    }
                String[] SUA_VOLUMES__ids = i.getVar("SUA_VOLUMES(ids)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = SUA_VOLUMES__ids.length; j < len; j++) {    				
    				String[] points = i.getVar("SUA_VOLUMES", SUA_VOLUMES__ids[j] + "," + "geodes", TCL.OK).toString().split(" ");
    				for (int k = 0, size = points.length;k < size; k++  ) {
        				AREA_CONTOUR_POINT point = new AREA_CONTOUR_POINT();
        				point.AREA_CONTOURID = SUA_VOLUMES__ids[j];
        				point.LOCATION_FORMAT = "G"; 
        				point.POINT_LOCATION = points[k];
        				point.SEQUENCE_NUMBER = String.valueOf(k+1);  				
        				ListAREA_CONTOUR_POINT.add(point);	
    				}    				
    		    }
                String[] HOLD_VOLUMES__ids = i.getVar("HOLD_VOLUMES(ids)", TCL.OK).toString().split(" ");
    			for (int j = 0, len = HOLD_VOLUMES__ids.length; j < len; j++) {    				
    				String[] points = i.getVar("HOLD_VOLUMES", HOLD_VOLUMES__ids[j] + "," + "geodes", TCL.OK).toString().split(" ");
    				for (int k = 0, size = points.length;k < size; k++  ) {
        				AREA_CONTOUR_POINT point = new AREA_CONTOUR_POINT();
        				point.AREA_CONTOURID = HOLD_VOLUMES__ids[j];
        				point.LOCATION_FORMAT = "G"; 
        				point.POINT_LOCATION = points[k];
        				point.SEQUENCE_NUMBER = String.valueOf(k+1);  				
        				ListAREA_CONTOUR_POINT.add(point);	
    				}    				
    		    } 
			 } catch (TclException e) {
				 System.out.println("Exception: " + e.getMessage());
			 }
    	}else if(AdaptationSource.equals("iTAP_CSVs")){
	    	File inputFile = this.fileAREA_CONTOUR_POINTS;			
	        try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		AREA_CONTOUR_POINT areaContourPoint = new AREA_CONTOUR_POINT( line );
	        		ListAREA_CONTOUR_POINT.add( areaContourPoint);	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}    
    	}
		return ListAREA_CONTOUR_POINT;
	}
    
    public List<BASIC_SECTORS> getBASIC_SECTORS (  ) {
    
    	List<BASIC_SECTORS> ListBASIC_SECTORS = new ArrayList<BASIC_SECTORS>();
    	
		if ( AdaptationSource.equals("environment.tcl") ) {
    		try {
    			i.eval("array names SCR_SECTOR");
    			String[] SCR_SECTOR_keys = i.getResult().toString().split(" ");
    			Map<String, BASIC_SECTORS> map = new HashMap<>();
    			for (int j=0, len=SCR_SECTOR_keys.length; j < len; j++ ) {
    				String[] tokens = SCR_SECTOR_keys[j].split(",");
    				if (tokens.length != 3) continue;
    				String index = tokens[0]+","+tokens[1];
        			BASIC_SECTORS sector = map.get(index);
        			if (sector == null ) {
    					BASIC_SECTORS newsector = new BASIC_SECTORS();
    					newsector.BASIC_SECTORID = i.getVar("SCR_SECTOR", index + "," + "basic", 0).toString();
    					newsector.TYPE_INDICATOR = i.getVar("SCR_SECTOR", index + "," + "type", 0).toString();
    					newsector.PRIORITY_NUMBER = i.getVar("SCR_SECTOR", index + "," + "prio", 0).toString();
    					newsector.MIN_CROSS_TIME = i.getVar("SCR_SECTOR", index + "," + "cross", 0).toString();
    					newsector.HAND_OVER_MODE = i.getVar("SCR_SECTOR", index + "," + "hand", 0).toString();
    					newsector.reference = i.getVar("SCR_SECTOR", index + "," + "reference", 0).toString();
    					newsector.SECTOR_VOLUMEID = tokens[0];    					
        				map.put(index, newsector);
        			}
    			}
    			for (BASIC_SECTORS basicsector : map.values()) {
    				ListBASIC_SECTORS.add(basicsector);
    			}
    			Collections.sort(ListBASIC_SECTORS);
    			                			
    		} catch (TclException e) {
    			System.out.println("Exception getBASIC_SECTORS: " + e.getMessage());    			
           }
    	}else if(AdaptationSource.equals("iTAP_CSVs")){
	    	File inputFile = this.fileBASIC_SECTORS;
	        try {	        	
	        	List<String> lines = Files.readAllLines( inputFile.toPath() );
	        	for ( String line : lines.subList( 1, lines.size()) ) {
	        		BASIC_SECTORS sector = new BASIC_SECTORS( line );
	        		ListBASIC_SECTORS.add( sector);	        		
	        	}
	        }			
			catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	return ListBASIC_SECTORS;
	}
    
    public List<AREA_CONTOUR_POINT> getAREA_CONTOUR_POINTSofAIRSPACE_VOLUME ( AIRSPACE_VOLUME targetedVolume , List<AREA_CONTOUR_POINT> points ) {
		
    	List<AREA_CONTOUR_POINT> listRelatedPoints = new ArrayList<AREA_CONTOUR_POINT>();
    	
    	for ( AREA_CONTOUR_POINT point : points) {
    		if ( point.AREA_CONTOURID.contains( targetedVolume.AREA_CONTOURID ) ){
    			listRelatedPoints.add(point);
    		}
    	}
    				
    	return listRelatedPoints;
	}
	
	public void printAIRSPACE_VOLUMEs ( List<AIRSPACE_VOLUME> volumes ) {
		
		for ( AIRSPACE_VOLUME volume : volumes) {
    		System.out.println(volume.toString());	        		
    	}
	}
	public void printAREA_CONTOUR_POINTs ( List<AREA_CONTOUR_POINT> points) {
		
		for ( AREA_CONTOUR_POINT point : points) {
    		System.out.println(point.toString());	        		
    	}
	}
	
	public String getAssignmentMode ( INTEREST_VOLUMES interestVolume , List<SECTOR_VOLUME> listSECTOR_VOLUMES ) {
		String mode = null;
		
    	for ( SECTOR_VOLUME secVol : listSECTOR_VOLUMES) {
    		if ( secVol.SECTOR_VOLUMEID.equals( interestVolume.SECTOR_VOLUMEID ) ){
    			mode = secVol.ASSIGNMENT_MODE;
    			break;
    		}
    	}		
		return mode;		
	}

}