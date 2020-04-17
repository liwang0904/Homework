import java.io.*;

public class SimpleObjectReader {
    public static SimpleObjectReader openFileForReading(String filename) {
    	try {
	    return new SimpleObjectReader(new ObjectInputStream(new FileInputStream(filename)));
    	} catch(IOException e) {	
	    return null;
	}	
    }

    /**
     * Reads a single object from the file and returns it. If there are
     * no more objects in the file (i.e, we have reached the end of file),
     * null is returned null is returned. null is also
     * returned on any I/O error.
     */
    public Object readObject () {
	try {
	    return ois.readObject();
    	}
    	catch (IOException e) {
	    e.printStackTrace();
	    return null;
	}
	catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * Closes the file when done reading.  You should close a reader when
     * you are finished to release the OS resources for use by others.
     */
    public void close () {
      	try {
	    ois.close();
      	}
      	catch (IOException e) {}
    }	
	
    /**
     * Constructor is private so that only means to create a new reader
     * is through the static method which does error checking.
     */
    private SimpleObjectReader(ObjectInputStream ois) {
	this.ois = ois;
    }
    
    private ObjectInputStream ois;
}
