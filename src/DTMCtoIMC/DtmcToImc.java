package DTMCtoIMC;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class DtmcToImc {
	
	public static String dtmcFile;
	public static double delta;
	public static String imcFile;
	public static double delta_min;
	static List<Double> currentState = new ArrayList<>();
	static List<Double> nextState = new ArrayList<>();
	static List<Double> dtmcElements = new ArrayList<>();
	static List<Double> dtmcElements_withoutSelfLoop = new ArrayList<>(); //avoid sink states
	
	public static void main(String[] args) throws Exception{
		
			Options options = new Options();
	
		    options.addOption("f", true, "Name of DTMC text file");
		    options.addOption("d", true, "Value for Delta");
		    options.addOption("i", true, "Name for IMC text file");
		    options.addOption(OptionBuilder.withLongOpt("help").create('h'));
		    
		    CommandLineParser parser = new DefaultParser();
		    CommandLine cmd = parser.parse( options, args );
		      
		    if(cmd.getOptionValue("f") == null) {
		    	System.err.println("input DTMC file names");
		    } else{
		    	dtmcFile = cmd.getOptionValue("f");
		    }
		    
		    if(cmd.hasOption("d")) {
		    	delta = Double.valueOf(cmd.getOptionValue("d"));
		    }
		    
		    if(cmd.getOptionValue("i") == null) {
		    	System.err.println("input DTMC file names");
		    } else{
		    	imcFile = cmd.getOptionValue("i");
		    }
		    
		    if(cmd.hasOption("h")) {
		    	HelpFormatter formatter = new HelpFormatter();
		    	formatter.printHelp("Command line options", options);
		   }
		   
		   dtmcElements();  ////////////////////////////read dtmc
			
		   if(cmd.hasOption("d") && delta < calculateDelta()){
			   delta_min = delta;
		   }else{
			   delta_min = calculateDelta();
		   }
		    
		    File f = new File("./src/DTMCtoIMC/"+imcFile+".txt");
			
			if(f.exists() && !f.isDirectory()){
				System.err.println("Output file already exists, Please write outputs in another file");
			}else{
				
	            FileWriter writer = new FileWriter("./src/DTMCtoIMC/output/"+imcFile+".txt", true);
	            BufferedWriter bufferedWriter = new BufferedWriter(writer);
	            
	            bufferedWriter.write(numberOfStates()+"");
	            bufferedWriter.newLine();
	            
	            for(int r=0; r < currentState.size(); r++){
	            	
		            if(dtmcElements.get(r) == 1){
		            	bufferedWriter.write(Math.round(currentState.get(r))+" "+Math.round(nextState.get(r))+" "+ "1" +" "+ "1");
		            	bufferedWriter.newLine();
					}else{
						bufferedWriter.write(Math.round(currentState.get(r))+" "+Math.round(nextState.get(r))+" "+ (dtmcElements.get(r)-delta_min) +" "+ (dtmcElements.get(r)+delta_min));
		            	bufferedWriter.newLine();
					}
	            }
	            bufferedWriter.close();
	            
			}
		
	}
	
	public static int numberOfStates() throws Exception{
		
		File file = new File("./src/DTMCtoIMC/inputs/"+dtmcFile+".txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		 
		  String line = br.readLine();;
		  String[] firstVal = line.split(" ");
          int states=Integer.valueOf(firstVal[0]);
          return states;
	}
	
	public static void dtmcElements() throws Exception{
		
		List<Double>[] matrix = new List[4];
				
			File file = new File("./src/DTMCtoIMC/inputs/"+dtmcFile+".txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
		    String[] values = new String[3]; 
		    String line = br.readLine();
		    
		    while ((line = br.readLine()) != null)	
			{
		    	values = line.split(" ");
				
				currentState.add(Double.valueOf(values[0]));
				nextState.add(Double.valueOf(values[1]));
				dtmcElements.add(Double.valueOf(values[2]));
				if(Double.valueOf(values[2]) != 1){
					dtmcElements_withoutSelfLoop.add(Double.valueOf(values[2]));
				}
			}
			
		
	}
	
	public static double calculateDelta() throws Exception{
		
		double deltaMin = Collections.min(dtmcElements_withoutSelfLoop);
		double deltaMax = Collections.max(dtmcElements_withoutSelfLoop);
		double delta_cal = 0;
		
		if(deltaMin < 1-deltaMax){
			delta_cal = deltaMin/2;
		}else{
			delta_cal = (1-deltaMax)/2;
		}
		
		return delta_cal;
	}


}