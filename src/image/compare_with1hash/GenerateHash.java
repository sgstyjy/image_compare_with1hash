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
		WritableSheet sheet = workbook.createSheet("Hashtable",0);

		//call hash functions
		int blocknum = 0;
		int position = 0;
		int size = reader.available();     //the total image size in byte
		byte[] bb = new byte[Constant.blocksize];
		String temp = null;
		String tempabs = null;
		long bkdrabs = 0;
		long apabs = 0;
		switch (Constant.HASH_METHOD)
		{
		case 0:
			AP aphasher = new AP();
			while(position<size){
		     	//special tackle the last block
				if((size-position)<Constant.blocksize){
						byte[] lastbf = new byte[(size-position)];
						reader.read(lastbf);
						temp = new String(lastbf);
						apabs = aphasher.aphash(temp);	    	
						tempabs = Long.toString(apabs);
						Label tempcell = new Label (blocknum/Constant.COLUMNS,blocknum%Constant.COLUMNS, tempabs);
						sheet.addCell(tempcell);
						break;
					}
					reader.read(bb);
					temp = new String (bb);
					apabs = aphasher.aphash(temp);	    	
					tempabs = Long.toString(apabs);
					Label tempcell = new Label (blocknum/Constant.COLUMNS,blocknum%Constant.COLUMNS, tempabs);
					sheet.addCell(tempcell);
					position += Constant.blocksize;
					blocknum++;
			}
			//System.out.println("Total block: "+blocknum);
			workbook.write();
			workbook.close();
			reader.close();
			break;
		case 1:
			BKDR bkdrhasher = new BKDR();
			while(position<size){
		     	//special tackle the last block
				if((size-position)<Constant.blocksize){
						byte[] lastbf = new byte[(size-position)];
						reader.read(lastbf);
						temp = new String(lastbf);
						bkdrabs = bkdrhasher.bkdrhash(temp);	    	
						tempabs = Long.toString(bkdrabs);
						Label tempcell = new Label (blocknum/Constant.COLUMNS,blocknum%Constant.COLUMNS, tempabs);
						sheet.addCell(tempcell);
						break;
					}
					reader.read(bb);
					temp = new String (bb);
					bkdrabs = bkdrhasher.bkdrhash(temp);    	
					tempabs = Long.toString(bkdrabs);
					Label tempcell = new Label (blocknum/Constant.COLUMNS,blocknum%Constant.COLUMNS, tempabs);
					sheet.addCell(tempcell);
					position += Constant.blocksize;
					blocknum++;
			}
			//System.out.println("Total block: "+blocknum);
			workbook.write();
			workbook.close();
			reader.close();
			break;
		}
		return blocknum;
	}
}
