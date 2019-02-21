package uno_nasa;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;

public class Nasa_Main {

	private static String classname;
	private static int counter = 0;
	private static double[] fcdlist1;
	static String inputFileName = null;
	static String inputFilePath = null;
	private String inputFileName1;
	private static Properties prop;
	private static String[] classlist1;
	static Map<Object, Set<String>> map3 = new HashMap<Object, Set<String>>();
	private ArrayList<String> stopwordslist;

	// Map for displaying tag words
	public static void setmap(Map<Object, Set<String>> map) {
		// TODO Auto-generated method stub
		map3 = map;
	}

	// List for displaying classes that are defined in the classification
	public static void setclasslist(ArrayList classlist) {
		// TODO Auto-generated method stub
		String[] cl = new String[classlist.size()];
		cl = (String[]) classlist.toArray(cl);

		classlist1 = cl;
	}

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		InputStream propinput = null;
		prop = new Properties();
		propinput = Nasa_Main.class.getResourceAsStream("/resources/" + "propertiesFile.properties");
		prop.load(propinput);
		propinput.close();
		// System.out.println(prop.getProperty("trainingFileName"));

		File absPathFile = new File("");
		File workingDir = new File(absPathFile.getAbsolutePath() + prop.getProperty("NasaWorkingDirectory"));
		try {
			FileUtils.deleteDirectory(workingDir);
			if (!workingDir.exists()) {
				workingDir.mkdirs();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
		
			// For commandline application processing
			// if the application is called from the command line with
			// the file location as arguments then the below code comes
			// into picture

			if (args.length > 0) {
				try {
					Path p = Paths.get(args[0]);
					String file = p.getFileName().toString();
					System.out.println("File name: " + file);

					storefilename(file);
					filepath(p.toString());
					String output_toFile = fromCommandLine();

					Options options = new Options();
					options.addOption("p", false, "display probability");
					options.addOption("o", false, "write to output file");
					options.addOption("t", false, "tag words");
					CommandLineParser parser = new GnuParser();
					CommandLine cmd = parser.parse(options, args);
					if (cmd.hasOption("o") && cmd.hasOption("p")) {
						PrintWriter out = new PrintWriter(new FileWriter("Prediction" + file, true), true);
						out.write(file);
						out.write(output_toFile);

						
						out.close();

					}

				} catch (Exception e) {

					// System.out.println(e.getClass());
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	

	// code for the command line access for the application
	protected static String fromCommandLine() {
		// TODO Auto-generated method stub
		StringBuffer outputReturn = new StringBuffer();
		Nasa_Main gt = new Nasa_Main();

		// once the file is selected it is moved into working directory for
		// converting the input file into arff file
		try {
			TextDirectoryToArff.movefile(inputFilePath, inputFileName);
		} catch (Exception e) {

			// System.out.println(e.getClass());
		}

		/*
		 * counter == 0 implies that the classification is done for the top
		 * level category , once the top level category classification id
		 * completed the counter is set to 1. once the counter is set to one ,
		 * new training file for the process of sub category level
		 * classification is made After the completion of the classification ,
		 * output is displayed on the console screen
		 */

		try {
			if (counter == 0) {
				outputReturn.append("Top category ");
				outputReturn.append(Process.smo(prop.getProperty("trainingFileName")));
				outputReturn.append(System.getProperty("line.separator"));
				counter = 1;
			}
			String filename = Nasa_Main.classname;
			System.out.println(outputReturn);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return outputReturn.toString();
	}



	public static void storefilename(String filename2) {
		// TODO Auto-generated method stub
		inputFileName = filename2;
	}

	public static void filepath(String filepath2) {
		// TODO Auto-generated method stub
		inputFilePath = filepath2;

	}

	public static void setclassname(String classname) {
		// TODO Auto-generated method stub
		Nasa_Main.classname = classname;
		// System.out.println(classname);
	}
}
