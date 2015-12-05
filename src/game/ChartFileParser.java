package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ChartFileParser {

	private static String parse(String filePath) {
		try {
			File file = new File(filePath);
			System.out.println("Reading notechart from file "+filePath);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			fileReader.close();
			return stringBuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static ArrayList<Note> getChartFromFile(String filePath) {
		ArrayList<Note> chart = new ArrayList<Note>();
		String textChart = parse(filePath);
		String[] lines = textChart.split("\n");
		for(String line : lines){
			String[] parts = line.split(":");
			String laneString = parts[1];
			String beatString = parts[0];
			String[] multipleLanes = laneString.split(",");
			float beat = Float.parseFloat(beatString);
			for(String l : multipleLanes) {
				int lane = Integer.parseInt(l);
				chart.add(new Note(lane, beat));				
			}
		}
		return chart;
	}
}
