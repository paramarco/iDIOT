package iDIOT;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Filewalker {
	
	public List<File> listFileSDD_DISTRIBUTION;	
	public List<File> listFileASTERIX62;	
	public File FileSectorization;
	public File FileAdaptation;

	
	public Filewalker( String path ) {				
		listFileSDD_DISTRIBUTION  = new ArrayList<File>();
		listFileASTERIX62 = new ArrayList<File>();
		FileSectorization = null;
		FileAdaptation = new File("./lib/adap");
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
		return FileAdaptation;
	}
	
    public void walk( String path ) {
     	
        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
            	if (f.getName().equals("adap") )
            		FileAdaptation = f;
            	else
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
            	
            }
        }
    }
}
