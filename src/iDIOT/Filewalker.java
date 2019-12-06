package iDIOT;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

public class Filewalker {
	
	public List<File> listFileSDD_DISTRIBUTION;	
	public List<File> listFileASTERIX62;	
	public File FileSectorization;
	public File FileAdaptation;
	public File FileEnvironment;
	public File FileiTAPCSVs;
	public String targetPath;
	public String AdaptationSource;
	
	public Filewalker( String path ) {				
		listFileSDD_DISTRIBUTION  = new ArrayList<File>();
		listFileASTERIX62 = new ArrayList<File>();
		FileSectorization = null;
		FileAdaptation = null;
		FileEnvironment = null;
		FileiTAPCSVs = null;
		targetPath = path;
		AdaptationSource = null ;
		
		this.walk(path);
	}
	
	public List<File> getListFileSDD_DISTRIBUTION() {
		return listFileSDD_DISTRIBUTION;
	}

	public List<File> getListFileASTERIX62() {
		return listFileASTERIX62;
	}

	public File getFileSectorization() {
		return FileSectorization;
	}
	public File getFileAdaptation() {
		
		if( FileiTAPCSVs != null ) {
			FileAdaptation = FileiTAPCSVs;
			AdaptationSource = "iTAP_CSVs";
		}else if( FileEnvironment != null)  {
			FileAdaptation = FileEnvironment;
			AdaptationSource = "environment.tcl";
		}else {
			File Filelib= new File("./lib/dataset.tgz");
			File destination = new File( this.targetPath + "/adap");
    		Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
    		try {
				archiver.extract( Filelib , destination);
				File fiTAPCSVs = new File(this.targetPath + "/adap/csv");
				if ( fiTAPCSVs.exists() ) {  
					FileAdaptation = fiTAPCSVs;
					AdaptationSource = "libiTAP_CSVs";
				}						
			} catch (IOException e) { e.printStackTrace(); }
		}
		
		return FileAdaptation;
	}
	public String getAdaptationSource() {
		return AdaptationSource;
	}
	
    public void walk( String path ) {
     	
        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
            	walk( f.getAbsolutePath() );
            }
            if ( f.isFile()  ) {
            	
            	String fileName = f.getName();
            	if ( fileName.lastIndexOf(".") == -1) return; 
            	String extension = fileName.substring(fileName.lastIndexOf("."));
            	
            	if ( extension.equals(".txt") && fileName.contains("DISTRIBUTION_") ) {
            		listFileSDD_DISTRIBUTION.add(f);
            	}
            	if ( extension.equals(".txt") && fileName.contains("TRACKS_") ) {
            		listFileASTERIX62.add(f);
            	}
            	if ( extension.equals(".xml") && fileName.contains("SECTORIZATION_DUMP_") ) {
            		FileSectorization = f;
            	}
            	if ( fileName.equals("Builds_Info_For_soda.tgz")) {
            		File destination = new File(f.getParent());
            		Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
            		try {
						archiver.extract( f , destination);
						File fEnvironment = new File(f.getParent() + "/adap/environment.tcl");
						if ( fEnvironment.exists() ) {  
							FileEnvironment = fEnvironment; 
						}
						File fdataset = new File(f.getParent() + "/adap/dataset.tgz");
						if ( fdataset.exists() ) {
							destination = new File(f.getParent() + "/adap");
							archiver.extract( fdataset , destination);
							File fiTAPCSVs = new File(f.getParent() + "/adap/csv");
							if ( fiTAPCSVs.exists() ) {  
								FileiTAPCSVs = fiTAPCSVs; 
							}						

						}
						
					} catch (IOException e) { e.printStackTrace(); }
            	}
            	
            }
        }
    }
}
