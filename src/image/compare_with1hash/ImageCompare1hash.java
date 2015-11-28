package image.compare_with1hash;

import java.io.IOException;
import java.util.Date;

import jxl.JXLException;
import jxl.write.biff.JxlWriteException;

public class ImageCompare1hash {

	public static void main(String[] args) throws IOException, JxlWriteException, JXLException {
		
		/*
		 * @para file1,    file1 is the main file, that is, the image will be migrated
		 * @para file2 ,	don't use point in the file name
		 * @para blocksize锛�      in the unit of KB
		 * @para hash method,  0 is ap, 1 is bkdr.
		 */
		Constant.file1=args[0];
		System.out.println("The first input file name is: "+Constant.file1);
		Constant.file2=args[1];
		System.out.println("The second input file name is: "+Constant.file2);
		int size = Integer.parseInt(args[2]);
		System.out.println("The blocksize is: "+size+"K");
		Constant.blocksize=size*1024;
		String hashmethod = args[3];
		if(hashmethod.equals("bkdr"))
			Constant.HASH_METHOD = 1;
		System.out.println("The hash method is:"+Constant.HASH_METHOD);
		//build the name of hashtables
		StringBuilder strbuilder1 = new StringBuilder();
		String file1part = Constant.file1.split("\\.")[0];    //cannot use point directly, must use "\\" before it
		strbuilder1.append(file1part);
		strbuilder1.append(".xls");
		Constant.hashtable1=strbuilder1.toString();
		System.out.println("The name for first hashtable is: "+Constant.hashtable1);
		StringBuilder strbuilder2= new StringBuilder();
		String file2part = Constant.file2.split("\\.")[0];
		strbuilder2.append(file2part);
		strbuilder2.append(".xls");
		Constant.hashtable2=strbuilder2.toString();
		System.out.println("The name for second hashtable is: "+Constant.hashtable2);
		//generate the compare result table name
		StringBuilder strbuilder3= new StringBuilder();
		strbuilder3.append(file1part);
		strbuilder3.append("_");
		strbuilder3.append(file2part);
		strbuilder3.append("_");
		strbuilder3.append(String.valueOf(args[2]));
		strbuilder3.append(".xls");
		Constant.compareresult=strbuilder3.toString();
		System.out.println("The name for compare result hashtable is: "+Constant.compareresult);
		//the start time
		Long starttime = System.currentTimeMillis();
		//System.out.println("The start time is: "+starttime);
		
		//generate the hashtables
		Long starttime_hash1 = System.currentTimeMillis();
		GenerateHash hashgenerater = new GenerateHash();  
		Constant.totalblocks1 = hashgenerater.generater(Constant.file1,Constant.hashtable1);
		System.out.println("The  total block numbers of the first image  are: "+Constant.totalblocks1);
		Long endtime_hash1 = System.currentTimeMillis();
		Long hashtime1=endtime_hash1-starttime_hash1;
		System.out.println("The hash time of first image is:"+hashtime1);
		
		Long starttime_hash2 = System.currentTimeMillis();
		Constant.totalblocks2  = hashgenerater.generater(Constant.file2,Constant.hashtable2);
		System.out.println("The  total block numbers of the second image  are: "+Constant.totalblocks2);
		Long endtime_hash2 = System.currentTimeMillis();
		Long hashtime2=endtime_hash2-starttime_hash2;
		System.out.println("The hash time of second image is:"+hashtime2);
				
		//compare hashtables
		Long starttime_compare = System.currentTimeMillis();
		CompareHash comparer = new CompareHash();
		comparer.fastCompareHash(Constant.hashtable1, Constant.hashtable2);
		Long endtime_compare = System.currentTimeMillis();
		Long comparetime=endtime_compare-starttime_compare;
		System.out.println("The similar size is: "+ Constant.similar*size/1024+"MB");
		System.out.println("The compare time is:"+comparetime);
		
		//the end time
		Long endtime = System.currentTimeMillis();
		Long temp = endtime-starttime;
		Long duration;
		if(hashtime1>hashtime2)
			   duration = temp-hashtime2;
		else
			   duration = temp-hashtime1;
		System.out.println("The total time is:"+duration);
	}

}
