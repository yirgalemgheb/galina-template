package com.sqa.gy.helpers;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;

import org.apache.log4j.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import com.sqa.gy.helpers.exceptions.*;

/**
 * DataHelper Class to handle reading data from different sources.
 */
public class DataHelper {

	private static Logger logger = Logger.getLogger(DataHelper.class);

	/**
	 * displays the data passed in a 2-d object array
	 *
	 * @param data
	 *            array whose data is it be displayed
	 */
	public static void displayData(Object[][] data) {
		// Display the data Object[][] to console using the logger.
		for (int i = 0; i < data.length; i++) {
			String dataLine = "";
			for (int j = 0; j < data[i].length; j++) {
				dataLine += data[i][j];
				dataLine += "  \t";
			}
			logger.info(dataLine.trim());
		}
	}

	/**
	 * Method to read a database table and get data from it Overloads method to
	 * select all columns and include no offset
	 *
	 * @param driverClassString
	 * @param databaseStringUrl
	 * @param username
	 *            - for database
	 * @param password
	 *            - for database
	 * @param tableName
	 * @return data in db as 2-d object array
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws DataTypesMismatchException
	 * @throws DataTypesCountException
	 * @throws DataTypesTypeException
	 */
	public static Object[][] evalDatabaseTable(String driverClassString, String databaseStringUrl, String username,
			String password, String tableName) throws ClassNotFoundException, SQLException, DataTypesMismatchException,
			DataTypesCountException, DataTypesTypeException {
		return evalDatabaseTable(driverClassString, databaseStringUrl, username, password, tableName, "*", 0, 0, null);
	}

	/**
	 * Method to read database table with implementation for a row and column
	 * offset.
	 *
	 * @param driverClassString
	 * @param databaseStringUrl
	 * @param username
	 *            - for database
	 * @param password
	 *            - for database
	 * @param tableName
	 * @param whatToSelect
	 *            * if all or which columns to select
	 * @param rowOffset
	 *            - if you want to skip column headings
	 * @param colOffset
	 *            - if you want to skip row labels
	 * @param dataTypes
	 *            - array of which data type to expect for each column
	 * @return data in db as 2-d object array
	 * @throws DataTypesMismatchException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws DataTypesCountException
	 * @throws DataTypesTypeException
	 */
	public static Object[][] evalDatabaseTable(String driverClassString, String databaseStringUrl, String username,
			String password, String tableName, String whatToSelect, int rowOffset, int colOffset, DataType[] dataTypes)
			throws DataTypesMismatchException, ClassNotFoundException, SQLException, DataTypesCountException,
			DataTypesTypeException {
		// 2D Array of Objects to hold data object to be returned
		Object[][] myData;
		ArrayList<Object> myArrayData = new ArrayList<>();
		Class.forName(driverClassString);
		Connection dbconn = DriverManager.getConnection(databaseStringUrl, username, password);
		Statement stmt = dbconn.createStatement();
		ResultSet rs = stmt.executeQuery("select " + whatToSelect + " from " + tableName);
		int numOfColumns = rs.getMetaData().getColumnCount();
		if (dataTypes != null) {
			if (dataTypes.length != numOfColumns) {
				throw new DataTypesCountException();
			}
		}
		int curRow = 1;
		while (rs.next()) {
			if (curRow > rowOffset) {
				Object[] rowData = new Object[numOfColumns - colOffset];
				for (int i = 0; i < rowData.length; i++) {
					try {
						switch (dataTypes[i]) {
						case STRING:
							rowData[i] = rs.getString(i + colOffset + 1);
							break;
						case INT:
							rowData[i] = rs.getInt(i + colOffset + 1);
							break;
						case FLOAT:
							rowData[i] = rs.getFloat(i + colOffset + 1);
							break;
						case DOUBLE:
							rowData[i] = rs.getDouble(i + colOffset + 1);
							break;
						default:
							break;
						}
					} catch (Exception e) {
						System.out.println("Error in conversion...");
						e.printStackTrace();
						throw new DataTypesTypeException();
					}
				}
				myArrayData.add(rowData);
			}
			curRow++;
		}
		myData = new Object[myArrayData.size()][];
		for (int i = 0; i < myData.length; i++) {
			myData[i] = (Object[]) myArrayData.get(i);
		}

		rs.close();
		stmt.close();
		dbconn.close();
		return myData;
	}

	/**
	 * Method to read an excel file in both the old format of excel and the
	 * newer one.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param hasLabels
	 *            if has labels, then those
	 * @return data in file as 2-d object array
	 * @throws InvalidExcelExtensionException
	 */
	public static Object[][] getExcelFileData(String fileLocation, String fileName, Boolean hasLabels)
			throws InvalidExcelExtensionException {
		Object[][] resultsObject;
		String[] fileNameParts = fileName.split("[.]");
		String extension = fileNameParts[fileNameParts.length - 1];
		ArrayList<Object> results = null;
		if (extension.equalsIgnoreCase("xlsx")) {
			results = getNewExcelFileResults(fileLocation, fileName, hasLabels);
		} else if (extension.equalsIgnoreCase("xls")) {
			results = getOldExcelFileResults(fileLocation, fileName, hasLabels);
		} else {
			throw new InvalidExcelExtensionException();
		}
		resultsObject = new Object[results.size()][];
		results.toArray(resultsObject);
		return resultsObject;
	}

	/**
	 * Overloaded method to read text file formatted in CSV style.
	 *
	 * @param fileName
	 * @return
	 */
	public static Object[][] getTextFileData(String fileName) {
		return getTextFileData("", fileName, TextFormat.CSV, false, null);
	}

	/**
	 * Overloaded method to read text file of various formats
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param textFormat
	 * @return
	 */
	public static Object[][] getTextFileData(String fileLocation, String fileName, TextFormat textFormat) {
		return getTextFileData(fileLocation, fileName, textFormat, false, null);
	}

	/**
	 * Method to read text file of various formats and also allows DataTypes to
	 * be specified
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param textFormat
	 * @param hasLabels
	 * @param dataTypes
	 * @return
	 */
	public static Object[][] getTextFileData(String fileLocation, String fileName, TextFormat textFormat,
			Boolean hasLabels, DataType[] dataTypes) {
		Object[][] data;
		ArrayList<String> lines = openFileAndCollectData(fileLocation, fileName);
		switch (textFormat) {
		case CSV:
			data = parseCSVData(lines, hasLabels, dataTypes);
			break;
		case XML:
			data = parseXMLData(lines, hasLabels);
			break;
		case TAB:
			data = parseTabData(lines, hasLabels);
			break;
		case JSON:
			data = parseJSONData(lines, hasLabels);
			break;
		default:
			data = null;
			break;
		}
		return data;
	}

	/**
	 * Overloaded method to read text file in various format styles and also
	 * allows DataTypes to be specified and also setting no labels.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param textFormat
	 * @param dataTypes
	 * @return
	 */
	public static Object[][] getTextFileData(String fileLocation, String fileName, TextFormat textFormat,
			DataType[] dataTypes) {
		return getTextFileData(fileLocation, fileName, textFormat, false, dataTypes);
	}

	/**
	 * Private method to convert data based on supplied DataType.
	 *
	 * @param parameter
	 * @param dataType
	 * @return
	 * @throws BooleanFormatException
	 * @throws CharacterCountFormatException
	 */
	private static Object convertDataType(String parameter, DataType dataType)
			throws BooleanFormatException, CharacterCountFormatException {
		Object data = null;
		try {
			switch (dataType) {
			case STRING:
				data = parameter;
				break;
			case CHAR:
				if (parameter.length() > 1) {
					throw new CharacterCountFormatException();
				}
				data = parameter.charAt(0);
				break;
			case DOUBLE:
				data = Double.parseDouble(parameter);
				break;
			case FLOAT:
				data = Float.parseFloat(parameter);
				break;
			case INT:
				data = Integer.parseInt(parameter);
				break;
			case BOOLEAN:
				if (parameter.equalsIgnoreCase("true") | parameter.equalsIgnoreCase("false")) {
					data = Boolean.parseBoolean(parameter);
				} else {
					throw new BooleanFormatException();
				}
				break;
			default:
				break;
			}
		} catch (NumberFormatException | BooleanFormatException | CharacterCountFormatException e) {
			System.out.println("Converting data.. to... " + dataType + "(" + parameter + ")");
		}
		return data;
	}

	/**
	 * Private method to get data from a new type of Excel file.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param hasLabels
	 * @return
	 */
	private static ArrayList<Object> getNewExcelFileResults(String fileLocation, String fileName, Boolean hasLabels) {
		ArrayList<Object> results = new ArrayList<>();
		try {
			String fullFilePath = fileLocation + fileName;
			InputStream newExcelFormatFile = new FileInputStream(new File(fullFilePath));
			XSSFWorkbook workbook = new XSSFWorkbook(newExcelFormatFile);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			if (hasLabels) {
				rowIterator.next();
			}
			while (rowIterator.hasNext()) {
				ArrayList<Object> rowData = new ArrayList<>();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue() + "\t\t\t");
						rowData.add(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue() + "\t\t\t");
						rowData.add(cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue() + "\t\t\t");
						rowData.add(cell.getStringCellValue());
						break;
					}
				}
				Object[] rowDataObject = new Object[rowData.size()];
				rowData.toArray(rowDataObject);
				results.add(rowDataObject);
				System.out.println("");
			}
			newExcelFormatFile.close();
			FileOutputStream out = new FileOutputStream(new File("src/main/resources/excel-output.xlsx"));
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * Private method to get data from an old type of Excel file.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @param hasLabels
	 * @return
	 */
	private static ArrayList<Object> getOldExcelFileResults(String fileLocation, String fileName, Boolean hasLabels) {
		ArrayList<Object> results = new ArrayList<>();
		try {
			String fullFilePath = fileLocation + fileName;
			InputStream newExcelFormatFile = new FileInputStream(new File(fullFilePath));
			HSSFWorkbook workbook = new HSSFWorkbook(newExcelFormatFile);
			HSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			if (hasLabels) {
				rowIterator.next();
			}
			while (rowIterator.hasNext()) {
				ArrayList<Object> rowData = new ArrayList<>();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue() + "\t\t\t");
						rowData.add(cell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print((int) cell.getNumericCellValue() + "\t\t\t");
						rowData.add((int) cell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue() + "\t\t\t");
						rowData.add(cell.getStringCellValue());
						break;
					}
				}
				Object[] rowDataObject = new Object[rowData.size()];
				rowData.toArray(rowDataObject);
				results.add(rowDataObject);
				System.out.println("");
			}
			newExcelFormatFile.close();
			FileOutputStream out = new FileOutputStream(new File("src/main/resources/excel-output.xlsx"));
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * Private method to open a text file and collect data lines as an ArrayList
	 * collection of lines.
	 *
	 * @param fileLocation
	 * @param fileName
	 * @return
	 */
	private static ArrayList<String> openFileAndCollectData(String fileLocation, String fileName) {
		String fullFilePath = fileLocation + fileName;
		ArrayList<String> dataLines = new ArrayList<>();
		try {
			FileReader fileReader = new FileReader(fullFilePath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			while (line != null) {
				dataLines.add(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fullFilePath + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fullFilePath + "'");
		}
		return dataLines;
	}

	/**
	 * Private method to get parse data formatted in CSV style.
	 *
	 * @param lines
	 * @param hasLabels
	 * @param dataTypes
	 * @return
	 */
	private static Object[][] parseCSVData(ArrayList<String> lines, boolean hasLabels, DataType[] dataTypes) {
		ArrayList<Object> results = new ArrayList<>();
		if (hasLabels) {
			lines.remove(0);
		}
		String pattern = "(,*)([a-zA-Z0-9\\s-]+)(,*)";
		Pattern r = Pattern.compile(pattern);
		for (int i = 0; i < lines.size(); i++) {
			int curDataType = 0;
			ArrayList<Object> curMatches = new ArrayList<>();
			Matcher m = r.matcher(lines.get(i));
			while (m.find()) {
				if (dataTypes.length > 0) {
					try {
						curMatches.add(convertDataType(m.group(2).trim(), dataTypes[curDataType]));
					} catch (Exception e) {
						System.out.println("DataTypes provided do not match parsed data results.");
					}
				} else {
					curMatches.add(m.group(2).trim());
				}
				curDataType++;
			}
			Object[] resultsObj = new Object[curMatches.size()];
			curMatches.toArray(resultsObj);
			results.add(resultsObj);
		}
		Object[][] resultsObj = new Object[results.size()][];
		results.toArray(resultsObj);
		return resultsObj;
	}

	/**
	 * Private method to get parse data formatted in JSON style.
	 *
	 * @param lines
	 * @param hasLabels
	 * @return
	 */
	private static Object[][] parseJSONData(ArrayList<String> lines, Boolean hasLabels) {
		// TODO Create an implementation to handle JSON formatted documents
		return null;
	}

	/**
	 * Private method to get parse data formatted in Tab style.
	 *
	 * @param lines
	 * @param hasLabels
	 * @return
	 */
	private static Object[][] parseTabData(ArrayList<String> lines, Boolean hasLabels) {
		// TODO Create an implementation to handle Tab formatted documents
		return null;
	}

	/**
	 * Private method to get parse data formatted in XML style.
	 *
	 * @param lines
	 * @param hasLabels
	 * @return
	 */
	private static Object[][] parseXMLData(ArrayList<String> lines, Boolean hasLabels) {
		// TODO Create an implementation to handle XML formatted documents
		return null;
	}
}
