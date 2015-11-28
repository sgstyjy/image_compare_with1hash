package image.compare_with1hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jxl.JXLException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.biff.JxlWriteException;

public class GenerateHash {

	public int generater (String file, String hashtable) throws IOException, JxlWriteException, JXLException{
		//the input image file
		File file_in = new File(file);
		InputStream reader = new FileInputStream(file_in);
		
		//the output hashtable
		File file_out = new File(hashtable);
		OutputStream writer = new FileOutputStream(file_out);
		WritableWorkbook workbook = Workbook.createWorkbook(writer);
		WritableSheet sheet1 = workbook.createSheet("BKDR",0);
		WritableSheet sheet2 = workbook.createSheet("AP",1);

		//call hash functions
		BKDR bkdrhasher = new BKDR();
		AP aphasher = new AP();
		
		int blocknum = 0;
		int position = 0;
		int size = reader.available();     //the total image size in byte
		byte[] bb = new byte[Constant.blocksize];
		String temp = null;
		long bkdrabs = 0;
		long apabs = 0;
		while(position<size){
		     	//special tackle the last block
					if((size-position)<Constant.blocksize){
								byte[] lastbf = new byte[(size-position)];
								reader.read(lastbf);
								temp = new String(lastbf);
								bkdrabs = bkdrhasher.bkdrhash(temp);
								apabs = aphasher.aphash(temp);	    	
								String temp1 = Long.toString(bkdrabs);
								String temp2 = Long.toString(apabs);
								Label tempcell1 = new Label (blocknum/Constant.COLUMNS,blocknum%Constant.COLUMNS, temp1);
								Label tempcell2 = new Label (blocknum/Constant.COLUMNS,blocknum%Constant.COLUMNS, temp2);
								sheet1.addCell(tempcell1);
								sheet2.addCell(tempcell2);
								break;
					}
					reader.read(bb);
					temp = new String (bb);
					bkdrabs = bkdrhasher.bkdrhash(temp);
					apabs = aphasher.aphash(temp);	    	
					String temp1 = Long.toString(bkdrabs);
					String temp2 = Long.toString(apabs);
					Label tempcell1 = new Label (blocknum/Constant.COLUMNS,blocknum%Constant.COLUMNS, temp1);
					Label tempcell2 = new Label (blocknum/Constant.COLUMNS,blocknum%Constant.COLUMNS, temp2);
					sheet1.addCell(tempcell1);
					sheet2.addCell(tempcell2);
					position += Constant.blocksize;
					blocknum++;
		}
		//System.out.println("Total block: "+blocknum);
		workbook.write();
		workbook.close();
		reader.close();
		return blocknum;
	}
}
