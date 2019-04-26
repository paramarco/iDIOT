package iDIOT;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.CompassLayer;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.placename.PlaceNameLayer;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.airspaces.AirspaceAttributes;
import gov.nasa.worldwind.render.airspaces.BasicAirspaceAttributes;
import gov.nasa.worldwind.render.airspaces.Polygon;
import gov.nasa.worldwind.render.airspaces.TrackAirspace;
import gov.nasa.worldwind.util.WWUtil;
import gov.nasa.worldwind.util.layertree.LayerTreeNode;
import gov.nasa.worldwindx.examples.util.DirectedPath;
import gov.nasa.worldwindx.examples.util.RandomShapeAttributes;
import iDIOT.AdaptationParser.AIRSPACE_VOLUME;
import iDIOT.AdaptationParser.AREA_CONTOUR_POINT;
import iDIOT.AdaptationParser.BASIC_SECTORS;
import iDIOT.AdaptationParser.HOLDING_AIRSPACE_VOLUME;
import iDIOT.AdaptationParser.HOLDING_VOLUME;
import iDIOT.AdaptationParser.INTEREST_VOLUMES;
import iDIOT.AdaptationParser.RESPONSIBILITY_VOLUMES;
import iDIOT.AdaptationParser.ROUTE_CONDITIONS_GUIDE;
import iDIOT.AdaptationParser.SECTOR_VOLUME;
import iDIOT.AdaptationParser.SITUATION_LINES_GUIDE;
import iDIOT.AdaptationParser.SITUATION_LINE_CONDITIONS;
import iDIOT.AdaptationParser.SITUATION_LINE_POINTS;
import iDIOT.OPERATION.SECTORIZATION;
import iDIOT.OPERATION.SECTORIZATION.BASICWIDEDELIVERY.BASICWIDE;
import iDIOT.SDD_DISTRIBUTION_Parser.ProgressionEntry;
import iDIOT.SDD_DISTRIBUTION_Parser.SDD_DISTRIBUTION_Event;
import iDIOT.SDD_DISTRIBUTION_Parser.VolumeSequence;
import iDIOT.TRACKS_Parser.TRACK_event;


public class App implements TimeLineListener{
	
	public App(AppFrame my_Frame) {
		myFrame = my_Frame;
		latestEvent = new Date(0);
		firstEvent = new Date();
		listLayers = new ArrayList<Layer>();
		
	}

	public String targetPath;
	public AdaptationParser adapParser;
	
	public List<AIRSPACE_VOLUME> listVolumes;	
	public List<AREA_CONTOUR_POINT> listAreaPoints;
	public List<BASIC_SECTORS> listSectors;
	public List<RESPONSIBILITY_VOLUMES> listRESPONSIBILITY_VOLUMES;
	public List<HOLDING_AIRSPACE_VOLUME> listHOLDING_AIRSPACE;
	public List<HOLDING_VOLUME> listHoldings;
	public List<INTEREST_VOLUMES> listINTEREST_VOLUMES;
	public List<SECTOR_VOLUME> listSECTOR_VOLUMES;
	public List<SITUATION_LINE_POINTS> listSITUATION_LINE_POINTS ;
	public List<SITUATION_LINES_GUIDE> listSITUATION_LINES_GUIDE;
	public List<SITUATION_LINE_CONDITIONS>listSITUATION_LINE_CONDITIONS;
	public List<ROUTE_CONDITIONS_GUIDE>listROUTE_CONDITIONS_GUIDE;
	
	
	public SECTORIZATION sectorization;
	public SectorizationParser sectorizationParser;
	
	public AppFrame myFrame;
	
	public LayerTreeNode layerTreeNode_flightPlans;
	public LayerTreeNode layerTreeNode_RadarTracks;
	public LayerTreeNode layerTreeNode_AdaptedAirSpace;
	public LayerTreeNode layerTreeNode4Holdings;
	public LayerTreeNode layerTreeNode4BasicSectors;
	public LayerTreeNode layerTreeNode4AoIs;
	public LayerTreeNode layerTreeNode4AoIsNormal;
	public LayerTreeNode layerTreeNode4AoIsContingency;
	public LayerTreeNode layerTreeNode4AoIsExternal;
	public LayerTreeNode layerTreeNode4CST;
	
	public List<Layer> listLayers;
		
	public Map<String, List<SDD_DISTRIBUTION_Event>> mapSSD_DISTRIBUTION ; 
	public Map<String, List<TRACK_event>> mapASTERIX62;
	public Date latestEvent;
	public Date firstEvent;

	
	public static void insertBeforeCompass(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just before the compass.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof CompassLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition, layer);
    }

    public static void insertBeforePlacenames(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just before the placenames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof PlaceNameLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition, layer);
    }

    public static void insertAfterPlacenames(WorldWindow wwd, Layer layer)
    {
        // Insert the layer into the layer list just after the placenames.
        int compassPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l instanceof PlaceNameLayer)
                compassPosition = layers.indexOf(l);
        }
        layers.add(compassPosition + 1, layer);
    }

    public static void insertBeforeLayerName(WorldWindow wwd, Layer layer, String targetName)
    {
        // Insert the layer into the layer list just before the target layer.
        int targetPosition = 0;
        LayerList layers = wwd.getModel().getLayers();
        for (Layer l : layers)
        {
            if (l.getName().indexOf(targetName) != -1)
            {
                targetPosition = layers.indexOf(l);
                break;
            }
        }
        layers.add(targetPosition, layer);
    }
    
    public  Layer makeLayerforCSTLine(SITUATION_LINE_CONDITIONS situationLine_condition)
    {
    	ROUTE_CONDITIONS_GUIDE routeCondition = listROUTE_CONDITIONS_GUIDE.stream()
    			.filter(x -> situationLine_condition.ROUTE_CONDITIONID.equals(x.CONDITIONID)).findAny().orElse(null);
    	SITUATION_LINES_GUIDE situationLine = listSITUATION_LINES_GUIDE.stream()
    			.filter(x -> situationLine_condition.SITUATION_LINEID.equals(x.SITUATION_LINEID)).findAny().orElse(null);
    	List<SITUATION_LINE_POINTS> listPoints = listSITUATION_LINE_POINTS.stream()
    			.filter(x -> situationLine.SITUATION_LINEID.equals(x.SITUATION_LINEID)).collect(Collectors.toList());
    	
    	Collections.sort(listPoints);
    	
    	AirspaceAttributes attr = new BasicAirspaceAttributes();
		attr.setInteriorMaterial(new Material(Color.BLUE));
		attr.setOutlineMaterial(new Material(WWUtil.makeColorBrighter(Color.BLUE)));
		attr.setInteriorOpacity(0.7);
		attr.setOutlineWidth(3);
		attr.setDrawOutline(true);
		attr.setEnableAntialiasing(true);
		attr.setEnableLighting(true);

        RenderableLayer layer = new RenderableLayer();
        layer.setName(routeCondition.CONDITIONID);
    	
    	String displayName = routeCondition.toString();
  	
    	//TODO this conversion is shit and you know it
		double sampleAltitude = (Integer.parseInt(routeCondition.MINIMUM_RFL)*100 ) /3.2808;
		   
		ArrayList<Position> positions = new ArrayList<Position>();
		for ( SITUATION_LINE_POINTS point : listPoints ) {
			positions.add(new Position( TRACKS_Parser.parseLatLon(point.POINT_LOCATION) , sampleAltitude));	
		}
					
        Path path = new DirectedPath(positions);        
        path.setAttributes(attr);
        path.setVisible(true);
        path.setAltitudeMode(WorldWind.ABSOLUTE);
        path.setPathType(AVKey.GREAT_CIRCLE);
        path.setShowPositions(true);
        path.setShowPositionsScale(2);
        path.setValue(AVKey.DISPLAY_NAME, displayName);
        
        layer.addRenderable(path);
       
    	return layer;    	
	   	
    }

    

    //TODO adapt to INTEREST VOLUME
    public  Layer makeLayerforINTEREST_VOLUME( INTEREST_VOLUMES interestVolume )
    {
		
    	List<AIRSPACE_VOLUME> listVolumes2Display = new ArrayList<AIRSPACE_VOLUME>();

   			
		for (AIRSPACE_VOLUME airspaceVolume :listVolumes) {
			if (airspaceVolume.AIRSPACE_VOLUMEID.equals(interestVolume.AIRSPACE_VOLUMEID) ){
				listVolumes2Display.add(airspaceVolume);
			}
		}
					
		AirspaceAttributes attr = new BasicAirspaceAttributes();

		attr.setInteriorMaterial(new Material(Color.YELLOW));
		attr.setOutlineMaterial(new Material(WWUtil.makeColorBrighter(Color.YELLOW)));
		attr.setInteriorOpacity(0.7);
		attr.setOutlineWidth(2);
		attr.setDrawOutline(true);
		attr.setEnableAntialiasing(true);
		attr.setEnableLighting(true);

		String label = interestVolume.toString() + "\n";
		 	
    	return makeLayerFromAirSpaceVolumes( listVolumes2Display , listAreaPoints, label, interestVolume.AIRSPACE_VOLUMEID , attr); 
	   	
    }


    
    public  Layer makeLayerforHoldingVolume( HOLDING_VOLUME holding )
    {
		
    	List<AIRSPACE_VOLUME> listVolumes2Display = new ArrayList<AIRSPACE_VOLUME>();

    	for ( HOLDING_AIRSPACE_VOLUME hav : listHOLDING_AIRSPACE) {
    		if ( hav.HOLDING_VOLUMEID.equals(holding.HOLDING_VOLUMEID) ) {    			
    			for (AIRSPACE_VOLUME airspaceVolume :listVolumes) {
    				if (airspaceVolume.AIRSPACE_VOLUMEID.equals(hav.AIRSPACE_VOLUMEID) ){
    					listVolumes2Display.add(airspaceVolume);
    				}
    			}
    		}
    	}    	
	
		String 	label = "Holding: " + holding.HOLDING_VOLUMEID.trim() + "\n";
				label += "HOLDING_FIXID: " + holding.HOLDING_FIXID.trim() + "\n";
				label += "INBOUND_COURSE: " + holding.INBOUND_COURSE.trim() + "\n";
				label += "TURN_DIRECTION: " + holding.TURN_DIRECTION.trim() + "\n";
				label += "LEG_DURATION: " + holding.LEG_DURATION.trim() + "\n";
				label += "PATTERN_TIME: " + holding.PATTERN_TIME.trim() + "\n";
				
		AirspaceAttributes attr = new BasicAirspaceAttributes();

		attr.setInteriorMaterial(new Material(Color.BLACK));
		attr.setOutlineMaterial(new Material(WWUtil.makeColorBrighter(Color.BLACK)));
		attr.setInteriorOpacity(0.7);
		attr.setOutlineWidth(2);
		attr.setDrawOutline(true);
		attr.setEnableAntialiasing(true);
		attr.setEnableLighting(true);

		 	
    	return makeLayerFromAirSpaceVolumes( listVolumes2Display , listAreaPoints, label, holding.HOLDING_VOLUMEID , attr); 
	   	
    }
    
    public Layer makeLayerforBasicSector( Object object)
    {
    	String RefNum = ""; String  SECTOR_VOLUMEID = ""; String  BASIC_SECTORID  = "";
    	Integer index = null;
    	
    	if (object instanceof String ) {
    		RefNum = (String) object; 
    		index = Integer.parseInt(RefNum);
    		index = index - 1;
    		
    		//String targetSECTOR_VOLUMEID = sectorizationParser.getWideSectorbyBasicSectorRefNum(RefNum).getWIDESECTOR();
    		BASICWIDE basicWideSector = sectorizationParser.getBasicSectorByRefNum(RefNum);
    		BASIC_SECTORS targetBasicSector = null;
    	    for (BASIC_SECTORS bs : listSectors) {
    	        if (basicWideSector.getBASICSECTOR().equals(bs.BASIC_SECTORID)) {
    	        	targetBasicSector = bs;
    	            break;
    	        }
    	    }
    		
    		SECTOR_VOLUMEID = targetBasicSector.SECTOR_VOLUMEID;  
    		BASIC_SECTORID = targetBasicSector.BASIC_SECTORID;
    		
    	}else if ( object instanceof BASIC_SECTORS ) {
    		BASIC_SECTORS basicSector = (BASIC_SECTORS) object;
    		index = listSectors.indexOf(basicSector);
    		Integer index_aux = index + 1;
    		RefNum = index_aux.toString();
    		SECTOR_VOLUMEID = listSectors.get( index ).SECTOR_VOLUMEID;  
    		BASIC_SECTORID = listSectors.get( index ).BASIC_SECTORID;	
        	
    	}else {
    		System.out.println("ERROR ::: coudn't find this basicSector:" + object.toString() );
    		return null;
    	}
    	
    	List<AIRSPACE_VOLUME> listVolumes2Display = new ArrayList<AIRSPACE_VOLUME>();    	
		    	
		String labelWideSector = sectorizationParser.getWideSectorLabelbyBasicSectorRefNum(RefNum);
		String label = "Basic Sector: " + BASIC_SECTORID.trim() + " ("+RefNum+")" + "\n" + labelWideSector;
				
    	for ( RESPONSIBILITY_VOLUMES sector : listRESPONSIBILITY_VOLUMES) {
    		if (sector.SECTOR_VOLUMEID.equals(SECTOR_VOLUMEID)) {
    			for (AIRSPACE_VOLUME airspaceVolume :listVolumes) {
    				if (airspaceVolume.AIRSPACE_VOLUMEID.equals(sector.AIRSPACE_VOLUMEID)){
    					listVolumes2Display.add(airspaceVolume);
    				}
    			}        			
    		}        		       		
    	}    	
    	
    	return makeLayerFromAirSpaceVolumes( listVolumes2Display , listAreaPoints, label, BASIC_SECTORID, null );
    }
    
    public Layer makeLayerProgressionSample( ProgressionEntry sample, ProgressionEntry nextSample )
    {	
    	AirspaceAttributes attr = new BasicAirspaceAttributes();
		attr.setInteriorMaterial(new Material(Color.BLACK));
		attr.setOutlineMaterial(new Material(WWUtil.makeColorBrighter(Color.BLACK)));
		attr.setInteriorOpacity(0.7);
		attr.setOutlineWidth(3);
		attr.setDrawOutline(true);
		attr.setEnableAntialiasing(true);
		attr.setEnableLighting(true);

        RenderableLayer layer = new RenderableLayer();
        layer.setName(sample.toString());
    	
    	String displayName = "Nr: " + sample.Nr + " \n";
    	displayName += "FL: " + sample.FL + " \n";
    	displayName += sample.Element.toString() + " \n\n";
    	displayName += "Nr: " + nextSample.Nr + " \n";
    	displayName += "FL: " + nextSample.FL + " \n";
    	displayName += nextSample.Element.toString() + "\n";
  	
    	//TODO this conversion is shit and you know it
		double sampleAltitude = (Integer.parseInt(sample.FL)*100 ) /3.2808;
		double nextSampleAltitude =  (Integer.parseInt(nextSample.FL)*100 ) /3.2808;   
		
		ArrayList<Position> positions = new ArrayList<Position>();
		positions.add(new Position(sample.Element, sampleAltitude));
		positions.add(new Position(nextSample.Element, nextSampleAltitude));
				
        Path path = new DirectedPath(positions);        
        path.setAttributes(attr);
        path.setVisible(true);
        path.setAltitudeMode(WorldWind.ABSOLUTE);
        path.setPathType(AVKey.GREAT_CIRCLE);
        path.setShowPositions(true);
        path.setShowPositionsScale(2);
        path.setValue(AVKey.DISPLAY_NAME, displayName);
        
        layer.addRenderable(path);
       
    	return layer;    	
    }
    
    
    public static Layer makeLayerFromAirSpaceVolumes( 	List<AIRSPACE_VOLUME> listVolumes , 
    													List<AREA_CONTOUR_POINT> listAreaPoints, 
    													String label ,
    													String LayerName,
    													AirspaceAttributes airspaceAttributes)
    {
    	RandomShapeAttributes randomAttrs = new RandomShapeAttributes();
        RenderableLayer layer = new RenderableLayer();
        layer.setName(LayerName);
        
    	for ( AIRSPACE_VOLUME volume : listVolumes) {    		
    		
        	List<AREA_CONTOUR_POINT> listRelatedPoints = new ArrayList<AREA_CONTOUR_POINT>();        	
        	for ( AREA_CONTOUR_POINT point : listAreaPoints) {
        		if ( point.AREA_CONTOURID.equals( volume.AREA_CONTOURID ) ){
        			listRelatedPoints.add(point);
        		}
        	}        	
        	Collections.sort(listRelatedPoints);
        	
        	List<LatLon> listPoligonLatlon = new ArrayList<LatLon>();
        	for ( AREA_CONTOUR_POINT point : listRelatedPoints) {
        		listPoligonLatlon.add( TRACKS_Parser.parseLatLon(point.POINT_LOCATION) );    		
        	}
        	
        	AirspaceAttributes attrs;
        	attrs = (airspaceAttributes == null) ? randomAttrs.nextAttributes().asAirspaceAttributes() : airspaceAttributes ; 

        	//TODO this conversion is shit and you know it
        	double lowerAltitude = (Integer.parseInt(volume.LOWER_FACE_LIMIT)*100 ) /3.2808;
        	double upperAltitude = (Integer.parseInt(volume.UPPER_FACE_LIMIT)*100 ) /3.2808;
        	
        	String 	label2display =  "Volume: " + volume.AIRSPACE_VOLUMEID + "\n";
    		label2display += "Flight Level: [" + volume.LOWER_FACE_LIMIT + "-" + volume.UPPER_FACE_LIMIT +  "]\n" ;
    		label2display += label;
            		
        	/*for ( AREA_CONTOUR_POINT point : listRelatedPoints) {
        		label2display += "POINT: " + point.SEQUENCE_NUMBER + ", " + point.POINT_LOCATION + " \n"; 
        	}*/		
                 		
        	Polygon poly = new Polygon(attrs);
        	poly.setLocations( listPoligonLatlon );
        	poly.setAltitudes( lowerAltitude , upperAltitude );
        	poly.setValue(AVKey.DISPLAY_NAME, label2display);         
                        
            layer.addRenderable(poly);   
            
            /*  
        	List<Position> listPoligonPosition = new ArrayList<Position>();
        	for ( LatLon latLon : listPoligonLatlon) {
        		listPoligonPosition.add(new Position (latLon,upperAltitude));
        	}
            
            // Create a path with the specified positions that follows the terrain and draws a point at each position.
            Path path = new Path(listPoligonPosition);
            path.setAltitudeMode(WorldWind.ABSOLUTE);
            path.setFollowTerrain(true);
            path.setShowPositions(true);
            path.setShowPositionsScale(5);

            // Create and set an attribute bundle. Specify only the path's outline width; the position colors override
            // the outline color and opacity.
            attrs.setOutlineWidth(5);
            path.setAttributes(attrs);

            // Configure the path to draw its outline and position points in the colors below. We use three colors that
            // are evenly distributed along the path's length and gradually increasing in opacity. Position colors may
            // be assigned in any manner the application chooses. This example illustrates only one way of assigning
            // color to each path position.

            path.setPositionColors(new ExamplePositionColors());

            layer.addRenderable(path);
            */
                       
            
        }    	
         	
    	return layer;        	
    }
    
    public static Layer makeLayerFromTRACKevents( List<TRACK_event> listEvents, Date until) {
    	
    	RandomShapeAttributes randomAttrs = new RandomShapeAttributes();    	
    	AirspaceAttributes attrs = randomAttrs.nextAttributes().asAirspaceAttributes();
        RenderableLayer layer = new RenderableLayer();
        layer.setName(listEvents.get(0).ARCID);
        

    	TrackAirspace track = new TrackAirspace(attrs);
    	
    	//String displayName = "";
    	
    	TRACK_event event = null;
    	TRACK_event nextEvent = null;

        for (int i=0 ; i<listEvents.size()-1; i++ ) {
        	
        	event = listEvents.get(i);
        	nextEvent = listEvents.get(i+1);
        	
        	if (nextEvent.timeStamp.after(until) ) break;
        	
        	track.addLeg( 	new LatLon(event.Position.latitude, event.Position.longitude), 
        					new LatLon(nextEvent.Position.latitude, nextEvent.Position.longitude), 
        					event.Position.elevation, 
        					nextEvent.Position.elevation,
							300,
							300);
        }
        
        track.setValue(AVKey.DISPLAY_NAME, nextEvent != null ? nextEvent.toString() : "" );
        track.setEnableInnerCaps(false);
        track.setEnableCenterLine(true); 
        layer.addRenderable(track);        
         	
       
    	return layer;
    	
    }    
    
    public void addLayer(Layer layer)
    {
        if (myFrame.getWwd().getModel().getLayers().contains(layer))
        	myFrame.getWwd().getModel().getLayers().remove(layer);
            
        listLayers.add(layer);
        myFrame.getWwd().getModel().getLayers().add( layer);
        
    }

    public void removeLayer(Layer layer)
    {
    	myFrame.getWwd().getModel().getLayers().remove(layer);
    }
    

    
    protected void loadStaticAdaptation() {   
    	
    	listVolumes = adapParser.getAIRSPACE_VOLUMEs();
    	listAreaPoints = adapParser.getAREA_CONTOUR_POINTS();
    	listSectors = adapParser.getBASIC_SECTORS();
    	listRESPONSIBILITY_VOLUMES = adapParser.getRESPONSIBILITY_VOLUMES();
    	listHOLDING_AIRSPACE = adapParser.getHOLDING_AIRSPACE_VOLUMEs();
    	listHoldings = adapParser.getHOLDING_VOLUMEs();
    	listINTEREST_VOLUMES = adapParser.getINTEREST_VOLUMES();
    	listSECTOR_VOLUMES = adapParser.getSECTOR_VOLUMES();
    	
    	listSITUATION_LINE_POINTS = adapParser.getSITUATION_LINE_POINTS();
    	listSITUATION_LINES_GUIDE = adapParser.getSITUATION_LINES_GUIDEs();
    	listSITUATION_LINE_CONDITIONS = adapParser.getSITUATION_LINE_CONDITIONS();
    	listROUTE_CONDITIONS_GUIDE = adapParser.getROUTE_CONDITIONS_GUIDE();    	
    	
    	    	
    	// HOLDINGs 
     	for ( HOLDING_VOLUME holding : listHoldings) {     		    				
    		Layer myHoldingLayer =  makeLayerforHoldingVolume ( holding );
    		myHoldingLayer.setEnabled(false);
    		
    		LayerTreeNode layerNode = new LayerTreeNode(myHoldingLayer) ;
			layerNode.setImageSource(null);
    		layerTreeNode4Holdings.addChild(layerNode);
    		
        	myFrame.getWwd().getModel().getLayers().add(myHoldingLayer);
    	} 
     	
     	// AoR ( loop by basic sectors )
     	for ( BASIC_SECTORS basicSector : listSectors) { 
     		Layer layerBasicSector =  makeLayerforBasicSector ( basicSector );
     		layerBasicSector.setEnabled(false);
     		
    		LayerTreeNode layerNode = new LayerTreeNode(layerBasicSector) ;
			layerNode.setImageSource(null);
			layerTreeNode4BasicSectors.addChild(layerNode);
    		
        	myFrame.getWwd().getModel().getLayers().add(layerBasicSector);
     	}
     	
    	// AoI ( contingency & normal. external )
     	for (  INTEREST_VOLUMES interestVolume : listINTEREST_VOLUMES) {     		    				
   		
     		Layer myAoILayer =  makeLayerforINTEREST_VOLUME ( interestVolume );
     		myAoILayer.setEnabled(false);
    		LayerTreeNode layerNode = new LayerTreeNode(myAoILayer) ;
			layerNode.setImageSource(null);
    		
     		String assignmentMode = adapParser.getAssignmentMode (interestVolume , listSECTOR_VOLUMES );
     		if (assignmentMode.equals("N")){     			
     	       layerTreeNode4AoIsNormal.addChild(layerNode);
     		}
     		if (assignmentMode.equals("C")){     			
      	       layerTreeNode4AoIsContingency.addChild(layerNode);
      		}
     		if (assignmentMode.equals("E")){     			
       	       layerTreeNode4AoIsExternal.addChild(layerNode);
       		}
    		      		
        	myFrame.getWwd().getModel().getLayers().add(myAoILayer);
    	}
     	
     	for (  SITUATION_LINE_CONDITIONS situationLine_condition : listSITUATION_LINE_CONDITIONS) {
     		Layer myCSTlayer = makeLayerforCSTLine(situationLine_condition);
     		myCSTlayer.setEnabled(false);
    		LayerTreeNode layerNode = new LayerTreeNode(myCSTlayer) ;
			layerNode.setImageSource(null);    		
			layerTreeNode4CST.addChild(layerNode);       		    		      		
        	myFrame.getWwd().getModel().getLayers().add(myCSTlayer);     		
     	}

    }
    
    protected void show_SDD_DISTRIBUTION( Map<String, List<SDD_DISTRIBUTION_Event>> map , Date until) {
    	
    	for (List<SDD_DISTRIBUTION_Event> list_FP_DISTRIBUTION : map.values()){    	
    		
    		SDD_DISTRIBUTION_Event event = list_FP_DISTRIBUTION.get(list_FP_DISTRIBUTION.size() - 1);
	    	
    		for (int i = list_FP_DISTRIBUTION.size() - 1; i >= 0; i--) {
    			if (list_FP_DISTRIBUTION.get(i).timeStamp.before(until) ) {
    				event = list_FP_DISTRIBUTION.get(i);
    				break;
    			}
    		}
	    	
	    	List<VolumeSequence> listVolSeq = event.listVolumeSequence;
	    	
	    	RenderableLayer layer4FlightPLan = new RenderableLayer();
	        layer4FlightPLan.setName(event.ARCID);
	        LayerTreeNode LayerTreeNode4FlightPlan = new LayerTreeNode(layer4FlightPLan) ;
	        LayerTreeNode4FlightPlan.setImageSource(null);
	
	        RenderableLayer layer4controlledSectors = new RenderableLayer();
	        layer4controlledSectors.setName("Controlled sectors");
	        LayerTreeNode LayerTreeNode4ControlledSectors = new LayerTreeNode(layer4controlledSectors) ;
	        LayerTreeNode4ControlledSectors.setImageSource(null); 
	        
	        LayerTreeNode4FlightPlan.addChild(LayerTreeNode4ControlledSectors);   
	
	    	
	    	for( VolumeSequence ControlVolSeq : listVolSeq) {
	    		
	    		if (ControlVolSeq.Sector_Kind.equals("E_ASSIGNED")) {
	    			
	    			Layer myBasicSectorLayer = makeLayerforBasicSector(ControlVolSeq.Ref);
	    			
					LayerTreeNode layerNode = new LayerTreeNode(myBasicSectorLayer) ;
					layerNode.setImageSource(null);
	    			LayerTreeNode4ControlledSectors.addChild(layerNode); 			
	    			
	    			addLayer(myBasicSectorLayer);    			
	    		}
	    		
	    	}
	    	
	    	
	    	List<ProgressionEntry> listProgression = event.listProgression;
	        
	        RenderableLayer layer4progression = new RenderableLayer();
	        layer4progression.setName("Progression");
	        LayerTreeNode LayerTreeNode4progression = new LayerTreeNode(layer4progression) ;
	        LayerTreeNode4progression.setImageSource(null); 
	        
	        LayerTreeNode4FlightPlan.addChild(LayerTreeNode4progression);   
	
	    	for( int i=0 ; i<listProgression.size()-1; i++) {
	    		
	    		ProgressionEntry sample = listProgression.get(i);
	    		ProgressionEntry nextsample = listProgression.get(i+1);
	    			
    			Layer myProgessionEntryLayer = makeLayerProgressionSample(sample,nextsample );
    			
				LayerTreeNode layerNode = new LayerTreeNode(myProgessionEntryLayer) ;
				layerNode.setImageSource(null);
				LayerTreeNode4progression.addChild(layerNode); 			
    			
    			addLayer(myProgessionEntryLayer);    			
	    		
	    	}
	    	 
	    	
	    	layerTreeNode_flightPlans.addChild(LayerTreeNode4FlightPlan);
	
    	}
    }

    
    public void show_ASTERIX_62( Map<String, List<TRACK_event>> mapEvents, Date until) {
  	
    	for (List<TRACK_event> listTrackEvents : mapEvents.values()) {
    		
			Layer myTrackLayer = makeLayerFromTRACKevents ( listTrackEvents, until );
    		
    		LayerTreeNode layerTreeNodeTrack = new LayerTreeNode(myTrackLayer) ;
    		layerTreeNodeTrack.setImageSource(null);
        	layerTreeNode_RadarTracks.addChild(layerTreeNodeTrack);
        	addLayer(myTrackLayer);    	   
    	}  	    	
    }
    
    

    
	@Override
	public void onTimeLineAdjusted(Date date) {
    	
    	for (Layer layer : listLayers) {
    		myFrame.getWwd().getModel().getLayers().remove(layer);
    	}
    	listLayers.clear();
    	
    	layerTreeNode_flightPlans.removeAllChildren();
    	layerTreeNode_RadarTracks.removeAllChildren();
   
    	
    	// display all SDD_DISTRIBUTION
    	show_SDD_DISTRIBUTION( mapSSD_DISTRIBUTION, date );    	   	
    	 
    	// display all ASTERIX 62    	
    	show_ASTERIX_62( mapASTERIX62, date );   
    	
	}
    
    protected void initTreeLayerOnAppFrame () {

        
        RenderableLayer flightPlansLayer = new RenderableLayer();
        flightPlansLayer.setName("Flight plans");
        layerTreeNode_flightPlans = new LayerTreeNode(flightPlansLayer) ;
        layerTreeNode_flightPlans.setImageSource(null);
        
        RenderableLayer radarTracksLayer = new RenderableLayer();
        radarTracksLayer.setName("RADAR tracks");
        layerTreeNode_RadarTracks = new LayerTreeNode(radarTracksLayer) ;
        layerTreeNode_RadarTracks.setImageSource(null);
        
        RenderableLayer AdaptedAirSpaceLayer = new RenderableLayer();
        AdaptedAirSpaceLayer.setName("Adapted Airspace");
        layerTreeNode_AdaptedAirSpace = new LayerTreeNode(AdaptedAirSpaceLayer) ;
        layerTreeNode_AdaptedAirSpace.setImageSource(null);
        
        RenderableLayer layer4Holdings = new RenderableLayer();
        layer4Holdings.setName("Holding Volumens");
        
        layerTreeNode4Holdings = new LayerTreeNode(layer4Holdings) ;
        layerTreeNode4Holdings.setImageSource(null); 
        
        layerTreeNode_AdaptedAirSpace.addChild(layerTreeNode4Holdings);
        
        RenderableLayer layer4BasicSectors = new RenderableLayer();
        layer4BasicSectors.setName("Area of Responsibility");
        
        layerTreeNode4BasicSectors = new LayerTreeNode(layer4BasicSectors) ;
        layerTreeNode4BasicSectors.setImageSource(null); 
        
        layerTreeNode_AdaptedAirSpace.addChild(layerTreeNode4BasicSectors);
        
        
        RenderableLayer layer4AoIs = new RenderableLayer();
        layer4AoIs.setName("Area of Interest");
        
        layerTreeNode4AoIs = new LayerTreeNode(layer4AoIs) ;
        layerTreeNode4AoIs.setImageSource(null); 
        
        layerTreeNode_AdaptedAirSpace.addChild(layerTreeNode4AoIs);
        
	       RenderableLayer layer4AoInormal = new RenderableLayer();
	       layer4AoInormal.setName("Normal");
	        
	       layerTreeNode4AoIsNormal = new LayerTreeNode(layer4AoInormal) ;
	       layerTreeNode4AoIsNormal.setImageSource(null); 
	        
	       layerTreeNode4AoIs.addChild(layerTreeNode4AoIsNormal);	
	       
	       RenderableLayer layer4AoIcontingency= new RenderableLayer();
	       layer4AoIcontingency.setName("Contingency");
	        
	       layerTreeNode4AoIsContingency = new LayerTreeNode(layer4AoIcontingency) ;
	       layerTreeNode4AoIsContingency.setImageSource(null); 
	        
	       layerTreeNode4AoIs.addChild(layerTreeNode4AoIsContingency);
	       
	       RenderableLayer layer4AoIexternal= new RenderableLayer();
	       layer4AoIexternal.setName("External");
	        
	       layerTreeNode4AoIsExternal = new LayerTreeNode(layer4AoIexternal) ;
	       layerTreeNode4AoIsExternal.setImageSource(null); 
	        
	       layerTreeNode4AoIs.addChild(layerTreeNode4AoIsExternal);
	       
        RenderableLayer layer4CSTs = new RenderableLayer();
        layer4CSTs.setName("Constraint lines");
        
        layerTreeNode4CST = new LayerTreeNode(layer4CSTs) ;
        layerTreeNode4CST.setImageSource(null); 
        
        layerTreeNode_AdaptedAirSpace.addChild(layerTreeNode4CST);  
	       
        
        myFrame.layerTree.getModel().addLayer(layerTreeNode_flightPlans);
        myFrame.layerTree.getModel().addLayer(layerTreeNode_RadarTracks);
        myFrame.layerTree.getModel().addLayer(layerTreeNode_AdaptedAirSpace);
        

    }
    
    public void computeFirstLastEvent () {
 
    	for (List<TRACK_event> listEvents : mapASTERIX62.values()) {
    		
    		Date timeStampEvent = listEvents.get(listEvents.size() -1).timeStamp;
    		
    		if (latestEvent.before(timeStampEvent))
    			latestEvent = new Date(timeStampEvent.getTime());
    		
    		timeStampEvent = listEvents.get(0).timeStamp;
    		
    		if (firstEvent.after(timeStampEvent))
    			firstEvent = new Date(timeStampEvent.getTime());
 	   
    	}
    	
    	for (List<SDD_DISTRIBUTION_Event> listEvents : mapSSD_DISTRIBUTION.values()) {
    		
    		Date timeStampEvent = listEvents.get(listEvents.size() -1).timeStamp;
    		
    		if (latestEvent.before(timeStampEvent))
    			latestEvent = new Date(timeStampEvent.getTime());
    		
    		timeStampEvent = listEvents.get(0).timeStamp;
    		
    		if (firstEvent.after(timeStampEvent))
    			firstEvent = new Date(timeStampEvent.getTime());
 	   
    	}
     
    }
    
    public void showTimeline() {
       	
        TimeLineLayer timeline = new TimeLineLayer();
        timeline.setEventSource(myFrame.getWwd());
        timeline.addListener(this);
        
        timeline.setFirstDate(firstEvent);
        timeline.setLastDate(latestEvent);        
                
        insertBeforePlacenames(myFrame.getWwd(), timeline);
    }
    
    public static class ExamplePositionColors implements Path.PositionColors
    {   	
            
        protected Color[] colors = {
            new Color(1f, 0f, 0f, 0.2f),
            new Color(0f, 1f, 0f, 0.6f),
            new Color(0f, 0f, 1f, 1.0f),
        };

        public ExamplePositionColors()
        {        	
        }

        public Color getColor(Position position, int ordinal)
        {
            return this.colors[2];
        }
    }
    
    public void showErrors() {
    	
    	List<String> listErrors = new ArrayList();
    	
    	if ( sectorizationParser.fileSectorization == null ) listErrors.add("ERROR ::: couldn't find any SECTORIZATION_DUMP_*.xml ");
    	if ( adapParser.fileAdaptationPath.contains("lib") && adapParser.fileAdaptationPath.contains("adap")) {
    		listErrors.add("ERROR ::: couldn't find any \"iTAP adaptation\" directory having .csv files ");
    		listErrors.add("WARNING ::: it's loading the ./lib/adap ( R4.2.2-09A VarAv1 ) ");
    	}
    	if ( mapSSD_DISTRIBUTION.isEmpty() ) listErrors.add("ERROR ::: couldn't find any DISTRIBUTION_*.txt file inside \"lrids\" folder ");
    	if ( mapSSD_DISTRIBUTION.isEmpty() ) listErrors.add("WARNING ::: couldn't find any TRACKS_*.txt file inside \"lrids\" folder  ");
    	    	
    	String message = "In the specified folder, some files are missing: \n";
    	
    	for ( String error : listErrors) {
    		message +=  "  " + error + "\n";
    	}   	
	
    	if ( ! listErrors.isEmpty() ) {
    		JOptionPane optionPane = new JOptionPane(message,JOptionPane.WARNING_MESSAGE);
            JDialog dialog = optionPane.createDialog(null,"  Warning!");
            dialog.setIconImage(null);
            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);            	
    	}
    	
    }
        
    
    public void start () {
    	
    	
    	initTreeLayerOnAppFrame();  

    	Filewalker fileWalker = new Filewalker(targetPath);
    
    	// it loads the Sector Plan
    	sectorizationParser = new SectorizationParser( fileWalker.getFileSectorization() );
    	
    	// it loads the Adaptation 
    	adapParser = new AdaptationParser( fileWalker.getFileAdaptation() );    	
    	loadStaticAdaptation();
    	
    	// load and display all SDD_DISTRIBUTION
    	mapSSD_DISTRIBUTION = SDD_DISTRIBUTION_Parser.loadSDD_DISTRIBUTION( fileWalker.getListFileSDD_DISTRIBUTION() ); 
    	show_SDD_DISTRIBUTION( mapSSD_DISTRIBUTION, new Date()  );    	   	
 
    	// load all ASTERIX 62    	
    	mapASTERIX62 = TRACKS_Parser.loadTRACKS_events( fileWalker.getListFileASTERIX62() );
    	// filter only those ASTERIX having a saved Flight plan
    	mapASTERIX62.entrySet().removeIf(e -> !mapSSD_DISTRIBUTION.containsKey(e.getKey()) );    	
    	// show filtered ASTERIX 62 
    	show_ASTERIX_62( mapASTERIX62, new Date() );   
    	    	
    	computeFirstLastEvent();

    	showTimeline();
    	
    	showErrors();
    	
    }
}
