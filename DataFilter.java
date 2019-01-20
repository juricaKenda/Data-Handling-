import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;



/**
 * A class designed to take care of data extraction process
 * & for providing filtered versions of data
 *
 */
public class DataFilter implements Runnable {

	
	private List<DataBundle> list;
	private File dataFile;
	
	/**
	 * A basic class constructor
	 * @param dataFile - represents a file object holding the reference
	 * to the file containing the relevant data
	 */
	public DataFilter(File dataFile) {
		this.dataFile = dataFile;
	}
	
	/**
	 * 
	 * @return Internal list of data extracted from the file
	 * given at initialization
	 */
	public List<DataBundle> getList(){
		return this.list;
	}


	@Override
	public void run() {
		Stream<String> dataInput;
		try {
			//Get a stream of data from the input file
			dataInput = Files.lines(dataFile.toPath());
			
			/*Map each line of data to a separate object containing the data
			 * & collect the newly created objects to a list
			 */
			this.list = dataInput.map(eachLine -> new DataBundle(eachLine))
					 			 .collect(Collectors.toList());
			
			dataInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		
	}
}
