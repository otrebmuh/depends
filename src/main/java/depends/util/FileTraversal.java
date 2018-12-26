package depends.util;

import java.io.File;
import java.util.ArrayList;

/**
 * Recursively visit every file in the given root path using the 
 * extended IFileVisitor
 *
 */
public class FileTraversal {
	/**
	 * The visitor interface 
	 * Detail operation should be implemented here
	 */
	public interface IFileVisitor {
		void visit(File file);
	}
	
	IFileVisitor visitor;
	private ArrayList<String> extensionFilters = new ArrayList<>();
	boolean shouldVisitDirectory = false;
	boolean shouldVisitFile = true;
	public FileTraversal(IFileVisitor visitor){
		this.visitor = visitor;
	}

	public FileTraversal(IFileVisitor visitor,boolean shouldVisitDirectory,boolean shouldVisitFile){
		this.visitor = visitor;
		this.shouldVisitDirectory = shouldVisitDirectory;
		this.shouldVisitFile = shouldVisitFile;
	}
	
	public void travers(String path) {
		File dir = new File(path);
		travers(dir);
	}

	public void travers(File root) {
		File[] files = root.listFiles();

		if (files == null)
			return;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				travers(files[i]);
				if (shouldVisitDirectory) {
					invokeVisitor(files[i]);
				}
			} else {
				if (shouldVisitFile) {
					invokeVisitor( files[i]);
				}
			}
		}		
	}

	private void invokeVisitor(File f) {
		if (extensionFilters.size()==0) {
			visitor.visit(f);
		}else {
			for (String ext:extensionFilters) {
				if (f.getAbsolutePath().toLowerCase().endsWith(ext.toLowerCase())) {
					visitor.visit(f);
				}
			}
		}
	}

	public FileTraversal extensionFilter(String ext) {
		this.extensionFilters.add(ext.toLowerCase());
		return this;
	}

	public void extensionFilter(String[] fileSuffixes) {
		for (String fileSuffix:fileSuffixes){
			extensionFilter(fileSuffix);
		}
	}
}