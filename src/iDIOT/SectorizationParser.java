package iDIOT;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import iDIOT.OPERATION.SECTORIZATION.BASICWIDEDELIVERY.BASICWIDE;
import iDIOT.OPERATION.SECTORIZATION.WIDESECTORSDELIVERY.WIDESECTORS;

public class SectorizationParser {
	
	public OPERATION oOPERATION;
	public List<BASICWIDE> basicSectors;
	public List<WIDESECTORS> wideSectors;
	public File fileSectorization;
	
	public SectorizationParser( File fileSectorization ) {
		super();
		
		if ( fileSectorization == null ) {
			oOPERATION = null;
			basicSectors = null;
			wideSectors = null;
			fileSectorization = null;			
			return;			
		}					
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(OPERATION.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			oOPERATION = (OPERATION) jaxbUnmarshaller.unmarshal( fileSectorization );
			basicSectors = oOPERATION.sectorization.basicwidedelivery.getBASICWIDE();
			wideSectors = oOPERATION.sectorization.widesectorsdelivery.getWIDESECTORS();			

			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public OPERATION.SECTORIZATION getSectorization () {		
		
		return oOPERATION.getSECTORIZATION();		
	}
	
	public WIDESECTORS getWideSectorbyBasicSectorRefNum (String RefNum )
    {
		WIDESECTORS wideSectorFound = null;
		
		if ( basicSectors != null || wideSectors != null ) {
			
			Integer index = Integer.parseInt(RefNum);
			index = index - 1;

			BASICWIDE basic_wide = basicSectors.get(index);		
			String wideSectorId = basic_wide.widesector;			
			
			for ( WIDESECTORS wideSector : wideSectors)
			{
				if ( wideSector.getWIDESECTOR().equals(wideSectorId) ){
					wideSectorFound = wideSector;
				}
			}
		}
		
		return wideSectorFound;		
	}
	
	public String getWideSectorLabelbyBasicSectorRefNum (String RefNum ) {
		String labelWideSector = "";
		WIDESECTORS wideSector = this.getWideSectorbyBasicSectorRefNum(RefNum);

		if ( wideSector != null) {		
			labelWideSector = (wideSector.getWIDESECTOR() != null ? "Wide Sector: " + wideSector.getWIDESECTOR() + "\n" : "");	
			if (wideSector.getSECTORCONSOLIDATION() != null) {
				labelWideSector +=	(wideSector.getSECTORCONSOLIDATION().getEXECUTIVE() != null ? "Executive: " + wideSector.getSECTORCONSOLIDATION().getEXECUTIVE() + "\n" : "");
				labelWideSector += 	(wideSector.getSECTORCONSOLIDATION().getPLANNER() != null ? "Planner: " + wideSector.getSECTORCONSOLIDATION().getPLANNER() + "\n" : "") ;
				labelWideSector += 	(wideSector.getSECTORCONSOLIDATION().getOPERATOR() != null ? "Operator: " + wideSector.getSECTORCONSOLIDATION().getOPERATOR() + "\n" : ""); 
			}								
			labelWideSector += (wideSector.getCONTROLCENTER() != null ? "Control Center: " + wideSector.getCONTROLCENTER() + "\n" : "") ;
		}

		return labelWideSector;
	}
	
	
}
