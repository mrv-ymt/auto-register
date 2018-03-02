package tool.auto.readexcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportTagServlet {

	private static final String FILE_PATH = "C:\\Users\\Administrator\\Desktop\\Adastria_AEM_Design_Tag_0207.xlsx";

	public static void main(String[] args) {

		XSSFWorkbook workbook = null;
		FileInputStream excelFile = null;
		DataFormatter formatter = new DataFormatter();
		try {

			// for each sheet in the workbook
			excelFile = new FileInputStream(new File(FILE_PATH));
			workbook = new XSSFWorkbook(excelFile);

			// for each sheet in the workbook
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

				System.out.println("Sheet name: " + workbook.getSheetName(i));
			}

			// Sheet datatypeSheet = workbook.getSheet("Tag-aphoto");
			Sheet datatypeSheet = workbook.getSheet("Tag-item");
			Iterator<Row> iterator = datatypeSheet.iterator();
			String id;
			List<TagEntity> tagInfosList = new ArrayList<TagEntity>();
			TagEntity tagEntity;
			String parentFolder;
			String grandFolder;
			String jpTitle;
			String enTitle;
			String path;

			while (iterator.hasNext()) {

				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				tagEntity = new TagEntity();
				id = null;
				parentFolder = null;
				grandFolder = null;
				jpTitle = null;
				enTitle = null;

				while (cellIterator.hasNext()) {

					Cell currentCell = cellIterator.next();

					if (currentCell.getColumnIndex() == 4) {
						grandFolder = formatter.formatCellValue(currentCell);
					}

					if (currentCell.getColumnIndex() == 5) {
						parentFolder = formatter.formatCellValue(currentCell);
					}
					if (currentCell.getColumnIndex() == 6) {
						id = formatter.formatCellValue(currentCell);
					}
					if (currentCell.getColumnIndex() == 10) {
						//byte[] bytes = currentCell.get
						jpTitle = formatter.formatCellValue(currentCell);
					}
					if (currentCell.getColumnIndex() == 11) {
						enTitle =  currentCell.getRichStringCellValue().getString();
					}
				}

				if (StringUtils.isNotEmpty(id)) {
					tagEntity.setId(id);
					path = "/etc/tags/adastria/" + grandFolder + "/" + parentFolder;
					tagEntity.setPath(path);
					tagEntity.setEnTitle(enTitle);
					tagEntity.setJpTitle(jpTitle);
					tagInfosList.add(tagEntity);
				}
			}

			for (int i = 0; i < tagInfosList.size(); i++) {
				System.out.println((i + 1) + ". " + tagInfosList.get(i).getPath() + "||" + tagInfosList.get(i).getId()
						+ "||" + tagInfosList.get(i).getJpTitle());
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (excelFile != null) {
				try {
					excelFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}
