package iDIOT;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class AdaptationParser  {
	
	public String fileAdaptationPath;
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
	
	public AdaptationParser( File fileAdaptation ) {
		
		this.fileAdaptationPath = fileAdaptation.getAbsolutePath();
		String pathAIRSPACE_VOLUMES = fileAdaptationPath + "/" + "AIRSPACE_VOLUMES_GUIDE_DATA_TABLE.csv";
		fileAIRSPACE_VOLUMES  = new File( pathAIRSPACE_VOLUMES );
		
		String pathAREA_CONTOUR_POINTS = fileAdaptationPath + "/" +  "AREA_CONTOUR_POINTS_DATA_TABLE.csv";
		fileAREA_CONTOUR_POINTS  = new File( pathAREA_CONTOUR_POINTS );
		
		String pathBASIC_SECTORS = fileAdaptationPath + "/" +  "BASIC_SECTORS_DATA_TABLE.csv";
		fileBASIC_SECTORS  = new File( pathBASIC_SECTORS );

		String pathRESPONSIBILITY_VOLUMES = fileAdaptationPath + "/" +  "RESPONSIBILITY_VOLUMES_DATA_TABLE.csv";
		fileRESPONSIBILITY_VOLUMES  = new File( pathRESPONSIBILITY_VOLUMES );
		
		String pathHOLDING_AIRSPACE_VOLUMES = fileAdaptationPath + "/" +  "HOLDING_AIRSPACE_VOLUMES_DATA_TABLE.csv";
		fileHOLDING_AIRSPACE_VOLUMES  = new File( pathHOLDING_AIRSPACE_VOLUMES );
		
		String pathHOLDING_VOLUMES = fileAdaptationPath + "/" +  "HOLDING_VOLUMES_GUIDE_DATA_TABLE.csv";
		fileHOLDING_VOLUMES  = new File( pathHOLDING_VOLUMES );

		String pathINTEREST_VOLUMES = fileAdaptationPath + "/" +  "INTEREST_VOLUMES_DATA_TABLE.csv";
		fileINTEREST_VOLUMES  = new File( pathINTEREST_VOLUMES );
		
		String pathSECTOR_VOLUMES = fileAdaptationPath + "/" +  "SECTOR_VOLUMES_DATA_TABLE.csv";
		fileSECTOR_VOLUMES  = new File( pathSECTOR_VOLUMES );
		
		String pathSITUATION_LINE_POINTS = fileAdaptationPath + "/" +  "SITUATION_LINE_POINTS_DATA_TABLE.csv";
		fileSITUATION_LINE_POINTS  = new File( pathSITUATION_LINE_POINTS );
		
		String pathSITUATION_LINES_GUIDE = fileAdaptationPath + "/" +  "SITUATION_LINES_GUIDE_DATA_TABLE.csv";
		fileSITUATION_LINES_GUIDE  = new File( pathSITUATION_LINES_GUIDE );
		
		String pathSITUATION_LINE_CONDITIONS = fileAdaptationPath + "/" +  "SITUATION_LINE_CONDITIONS_DATA_TABLE.csv";
		fileSITUATION_LINE_CONDITIONS  = new File( pathSITUATION_LINE_CONDITIONS );

		String pathROUTE_CONDITIONS_GUIDE = fileAdaptationPath + "/" +  "ROUTE_CONDITIONS_GUIDE_DATA_TABLE.csv";
		fileROUTE_CONDITIONS_GUIDE  = new File( pathROUTE_CONDITIONS_GUIDE );

	}

		
	//CONDITIONID,MINIMUM_RFL,MAXIMUM_RFL,MINIMUM_SCL,MAXIMUM_SCL,BOUNDARY_SIDE,REACHING_MODE,CROSSING_LEVEL,FORCE_INDICATOR,MAINTAIN_AFTER
	public static class ROUTE_CONDITIONS_GUIDE  {

		public String CONDITIONID,MINIMUM_RFL,MAXIMUM_RFL,MINIMUM_SCL,MAXIMUM_SCL,BOUNDARY_SIDE,REACHING_MODE,CROSSING_LEVEL,FORCE_INDICATOR,MAINTAIN_AFTER;
		
		public ROUTE_CONDITIONS_GUIDE () {}
		
		public ROUTE_CONDITIONS_GUIDE (String line) {
	
			String[] attributes = line.split(",");

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
			return " [" + (CONDITIONID != null ? "CONDITIONID=" + CONDITIONID + ", " : "")
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
	
			String[] attributes = line.split(",");
			
			this.SITUATION_LINEID= attributes[0].trim();
			this.ROUTE_CONDITIONID = attributes[1].trim();
			this.PRIORITY_NUMBER = attributes[2].trim();
									
		}

		@Override
		public String toString() {
			return "SITUATION_LINE_CONDITIONS ["
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
	
			String[] attributes = line.split(",");
			
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
	
			String[] attributes = line.split(",");
			
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
			return "SITUATION_LINE_POINTS ["
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
	
			String[] attributes = line.split(",");
			
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
	
			String[] attributes = line.split(",");
			
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
	
			String[] attributes = line.split(",");
			
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
	
			String[] attributes = line.split(",");
			
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
	
			String[] attributes = line.split(",");
			
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
	
	//BASIC_SECTORID	TYPE_INDICATOR	PRIORITY_NUMBER	MIN_CROSS_TIME	HAND_OVER_MODE	FORCE_DISTANCE	SECTOR_VOLUMEID	SECTOR_FLAVOURID
	public static class BASIC_SECTORS  {

		public String BASIC_SECTORID	;
		public String TYPE_INDICATOR;
		public String PRIORITY_NUMBER;
		public String MIN_CROSS_TIME	;
		public String HAND_OVER_MODE	;
		public String FORCE_DISTANCE;
		public String SECTOR_VOLUMEID;
		public String SECTOR_FLAVOURID;
		
		public BASIC_SECTORS () {}
		
		public BASIC_SECTORS (String line) {
	
			String[] attributes = line.split(",");
			
			this.BASIC_SECTORID = attributes[0].trim();
			this.TYPE_INDICATOR = attributes[1].trim();
			this.PRIORITY_NUMBER = attributes[2].trim();
			this.MIN_CROSS_TIME = attributes[3].trim();
			this.HAND_OVER_MODE = attributes[4].trim();
			this.FORCE_DISTANCE = attributes[5].trim();
			this.SECTOR_VOLUMEID = attributes[6].trim();
			this.SECTOR_FLAVOURID = attributes[7].trim();
		}
		
		@Override
		public String toString() {
			return "BASIC_SECTORS [BASIC_SECTORID=" + BASIC_SECTORID + ", TYPE_INDICATOR=" + TYPE_INDICATOR
					+ ", PRIORITY_NUMBER=" + PRIORITY_NUMBER + ", MIN_CROSS_TIME=" + MIN_CROSS_TIME
					+ ", HAND_OVER_MODE=" + HAND_OVER_MODE + ", FORCE_DISTANCE=" + FORCE_DISTANCE + ", SECTOR_VOLUMEID="
					+ SECTOR_VOLUMEID + ", SECTOR_FLAVOURID=" + SECTOR_FLAVOURID + "]";
		}
	}

	//AIRSPACE_VOLUMEID	SECTOR_VOLUMEID	
	public class INTEREST_VOLUMES  {

		public String AIRSPACE_VOLUMEID;
		public String SECTOR_VOLUMEID;
		
		public INTEREST_VOLUMES  () {}
		
		public INTEREST_VOLUMES(String line) {
	
			String[] attributes = line.split(",");
			
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
	
			String[] attributes = line.split(",");
			
			this.AIRSPACE_VOLUMEID = attributes[0].trim();
			this.SECTOR_VOLUMEID = attributes[1].trim();			
		}
		
		@Override
		public String toString() {
			return "RESPONSIBILITY_VOLUMES [AIRSPACE_VOLUMEID=" + AIRSPACE_VOLUMEID + ", SECTOR_VOLUMEID="
					+ SECTOR_VOLUMEID + "]";
		}
	}
	
	
	//ROUTE_CONDITIONS_GUIDE
	public List<ROUTE_CONDITIONS_GUIDE> getROUTE_CONDITIONS_GUIDE() {
		
		File inputFile = fileROUTE_CONDITIONS_GUIDE;
		
		List<ROUTE_CONDITIONS_GUIDE> list = new ArrayList<ROUTE_CONDITIONS_GUIDE>();
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

		return list;
	}
	
	
	
	//SITUATION_LINE_CONDITIONS
	public List<SITUATION_LINE_CONDITIONS> getSITUATION_LINE_CONDITIONS () {
		
		File inputFile = fileSITUATION_LINE_CONDITIONS;
		
		List<SITUATION_LINE_CONDITIONS> list = new ArrayList<SITUATION_LINE_CONDITIONS>();
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

		return list;
	}
	
	//SITUATION_LINES_GUIDE
	public List<SITUATION_LINES_GUIDE> getSITUATION_LINES_GUIDEs () {
		
		File inputFile = fileSITUATION_LINES_GUIDE;
		
		List<SITUATION_LINES_GUIDE> list = new ArrayList<SITUATION_LINES_GUIDE>();
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

		return list;
	}
	
	
	//HOLDING_VOLUME
	public List<HOLDING_VOLUME> getHOLDING_VOLUMEs () {
		
		File inputFile = fileHOLDING_VOLUMES;
		
		List<HOLDING_VOLUME> ListHOLDING_VOLUMEs = new ArrayList<HOLDING_VOLUME>();
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

		return ListHOLDING_VOLUMEs;
	}
	

	public List<SITUATION_LINE_POINTS> getSITUATION_LINE_POINTS() {
		
		File inputFile = this.fileSITUATION_LINE_POINTS;
		
		List<SITUATION_LINE_POINTS> list = new ArrayList<SITUATION_LINE_POINTS>();
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

		return list;
	}

	
	
	public List<HOLDING_AIRSPACE_VOLUME> getHOLDING_AIRSPACE_VOLUMEs () {
		
		File inputFile = fileHOLDING_AIRSPACE_VOLUMES;
		
		List<HOLDING_AIRSPACE_VOLUME> ListHOLDING_AIRSPACEs = new ArrayList<HOLDING_AIRSPACE_VOLUME>();
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

		return ListHOLDING_AIRSPACEs;
	}		

	
	public List<SECTOR_VOLUME> getSECTOR_VOLUMES() {
		
		File inputFile = this.fileSECTOR_VOLUMES;
		
		List<SECTOR_VOLUME> list = new ArrayList<SECTOR_VOLUME>();
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

		return list;
	}
	
	public List<INTEREST_VOLUMES> getINTEREST_VOLUMES (  ) {
		
		File inputFile = this.fileINTEREST_VOLUMES;
		
		List<INTEREST_VOLUMES> list = new ArrayList<INTEREST_VOLUMES>();
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

		return list;
	}

	
	
	public List<RESPONSIBILITY_VOLUMES> getRESPONSIBILITY_VOLUMES (  ) {
		
		File inputFile = this.fileRESPONSIBILITY_VOLUMES;
		
		List<RESPONSIBILITY_VOLUMES> list = new ArrayList<RESPONSIBILITY_VOLUMES>();
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

		return list;
	}
	
	public List<AIRSPACE_VOLUME> getAIRSPACE_VOLUMEs () {
		
		File inputFile = fileAIRSPACE_VOLUMES;
		
		List<AIRSPACE_VOLUME> ListAIRSPACE_VOLUME = new ArrayList<AIRSPACE_VOLUME>();
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

		return ListAIRSPACE_VOLUME;
	}
	
    public List<AREA_CONTOUR_POINT> getAREA_CONTOUR_POINTS (  ) {
    	
    	File inputFile = this.fileAREA_CONTOUR_POINTS;
		
		List<AREA_CONTOUR_POINT> ListAREA_CONTOUR_POINT= new ArrayList<AREA_CONTOUR_POINT>();
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

		return ListAREA_CONTOUR_POINT;
	}
    
    public List<BASIC_SECTORS> getBASIC_SECTORS (  ) {
    	
    	File inputFile = this.fileBASIC_SECTORS;
		
		List<BASIC_SECTORS> ListBASIC_SECTORS= new ArrayList<BASIC_SECTORS>();
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