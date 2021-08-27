package com.identity.service;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.identity.VO.VehicleDetailsVO;
import org.apache.commons.lang3.StringUtils;


import com.identity.VO.FileMetadataVO;

public class FileService {


	public static List<String> loadInputData(String inputFolderPath) throws IOException {
		 return readRegistrationNumbersFromInputFiles(inputFolderPath);
	}

	public static HashMap<String, VehicleDetailsVO> loadOutputData(String outputFolderPath) throws IOException {
		return readVehicleDetailsVOsFromOutputFile(outputFolderPath);
	}

	//read the input files for the vehicle registration numbers and make a list of them
	public static List<String> readRegistrationNumbersFromInputFiles(String directoryPath) throws IOException {
		List<File> fileList = retrieveFilesByMimeTypeFromTheDirectory(directoryPath);
		List<String> inputRegistrationNumberList = new ArrayList<String>();
		for (File file : fileList) {
			inputRegistrationNumberList.addAll(parseFileForRegistrationNumbers(file));
		}
		return inputRegistrationNumberList;
	}

	//read the output file for the Vehicle Details and make a Map of their VOs
	public static HashMap<String, VehicleDetailsVO> readVehicleDetailsVOsFromOutputFile(String directoryPath) throws IOException {
		List<File> fileList = retrieveFilesByMimeTypeFromTheDirectory(directoryPath);
		HashMap<String, VehicleDetailsVO> vehilceDetailsVOMap= new HashMap<String, VehicleDetailsVO>();
		for (File file : fileList) {
			vehilceDetailsVOMap.putAll(parseOutputFileForVehicleDetails(file));
		}
		return vehilceDetailsVOMap;
	}

	//service to retrieve the list of files based on the fileExtension in a given directory
	public static List<File> retrieveFilesByMimeTypeFromTheDirectory(String directoryPath) {
		List<File> fileList = new ArrayList<File>();
		try {
			HashMap <String,FileMetadataVO> fileDetailsMap = scanConfiguredDirectoryForFiles(directoryPath);
			//loop through every file metadata to retrieve excel or CSV
			for(FileMetadataVO FileMetadataVO : fileDetailsMap.values()){
				//Read the input or output file only if it is a txt file
				if (FileMetadataVO.getFileExtension().equals("txt")){
					File file = new File(FileMetadataVO.getFilePath().toString());
					fileList.add(file);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileList;
	}


	public static HashMap <String,FileMetadataVO> scanConfiguredDirectoryForFiles(String directoryPath) throws IOException {
		 File parentDirectory = new File(directoryPath);
		 HashMap <String,FileMetadataVO> fileDetailsMap = new HashMap<String,FileMetadataVO>();
		 
		 //go through every entry in the given directory and collect the details
		 for (final File fileEntry : parentDirectory.listFiles()) {
			 
			 //enter the if condition only if the current entry is a file
		        if (!fileEntry.isDirectory()) {
		        	
		              String fileName = fileEntry.getName();
		              long fileLength = fileEntry.length();
		              String fileType = Files.probeContentType(fileEntry.toPath());
		              String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
		              System.out.println("filename, fileLength, fileType and Externsion for the given Directory path are - " + fileName+ "," +fileLength + ","+ fileType +  ","+ fileExtension );
		              
		              //setting the values into the FileMetadataVOObject
		              FileMetadataVO FileMetadataVO = new FileMetadataVO();
		              FileMetadataVO.setFileName(fileName);
		              FileMetadataVO.setFileLength(fileLength);
		              FileMetadataVO.setFileType(fileType);
		              FileMetadataVO.setFileExtension(fileExtension);
		              FileMetadataVO.setFilePath(fileEntry.toPath());
		              
		              //setting the FileMetadataVO object into the HASHMap
		              fileDetailsMap.put(fileName, FileMetadataVO);
		              
		        } else {
		        	//if the entry is a directory, just ignore it.
		        	System.out.println("ignoring since this, as it is a directory -" + fileEntry.getName());
		        }
		        
		    }
		 return fileDetailsMap;
	}
	

	public static List<String> parseFileForRegistrationNumbers(File inputFile) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile));
		List<String> registrationNumberList =  new ArrayList<String>();
		String currentLine = null;
		while ((currentLine=bufferedReader.readLine())!=null) {
			if(StringUtils.isNotBlank(currentLine)) {
				registrationNumberList.addAll(findRegistrationNumberUsingPatternMatcher(currentLine));
			}
		}
		return registrationNumberList;
	}

	public static List<String> findRegistrationNumberUsingPatternMatcher(String currentLine){
		List<String> registrationNumberList =  new ArrayList<String>();
		//Pattern for Registration number is 2 CAP Letters + 2 Numbers + SPACE <optional> + 3 CAPS Letters
		Pattern pattern = Pattern.compile("\\p{Space}+\\p{Upper}{2}\\d{2}\\p{Space}*\\p{Upper}{3}\\p{Space}*\\p{Punct}*");
		Matcher matcher = pattern.matcher(currentLine);
		while(matcher.find()) {
			String registrationNumber = matcher.group();
			System.out.println("Registration number found in the input file -" + registrationNumber);
			registrationNumberList.add(registrationNumber.replaceAll("\\s+","")
					.replaceAll("\\p{Punct}", ""));
		}
		return registrationNumberList;
	}

	public static HashMap<String, VehicleDetailsVO> parseOutputFileForVehicleDetails(File outputFile) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(outputFile));
		HashMap<String, VehicleDetailsVO>  vehilceDetailsVOMap =  new HashMap<String, VehicleDetailsVO>();
		String currentLine = null;
		boolean isHeader = true;
		while ((currentLine=bufferedReader.readLine())!=null) {
			if(!isHeader && StringUtils.isNotBlank(currentLine)) {
				vehilceDetailsVOMap.putAll(parseVehicleDetails(currentLine));
			}
			isHeader = false;
		}
		return vehilceDetailsVOMap;
	}

	public static HashMap<String, VehicleDetailsVO> parseVehicleDetails(String currentLine){
		HashMap<String, VehicleDetailsVO>  vehilceDetailsVOMap =  new HashMap<String, VehicleDetailsVO>();
		String[] vehicleDetailArray = currentLine.split(",");
		VehicleDetailsVO vehicleDetailsVO = new VehicleDetailsVO();
		vehicleDetailsVO.setRegistration(vehicleDetailArray[0]);
		vehicleDetailsVO.setMake(vehicleDetailArray[1]);
		vehicleDetailsVO.setModel(vehicleDetailArray[2]);
		vehicleDetailsVO.setColor(vehicleDetailArray[3]);
		vehicleDetailsVO.setYear(vehicleDetailArray[4]);
		vehilceDetailsVOMap.put(vehicleDetailArray[0].replaceAll("\\s+",""), vehicleDetailsVO);
		return vehilceDetailsVOMap;
	}


	 
	 


	

}
