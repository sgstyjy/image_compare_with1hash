package image.compare_with1hash;

import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class CompareHash {

	public void fastCompareHash(String table1, String table2) throws BiffException, IOException, RowsExceededException, WriteException{
		   //read the first hashtable
		    File file_in1 = new File(table1);
		    //System.out.println("The first input compare table is:  "+file_in1.getName());
			Workbook book1 = Workbook.getWorkbook(file_in1);
			Sheet sheet1 = book1.getSheet(0);
			
			//read the second hashtable, and generate the node list
			File file_in2 = new File(table2);
			//System.out.println("The second input compare table is:  "+file_in2.getName());
			Workbook book2 = Workbook.getWorkbook(file_in2);
			Sheet sheet2 = book2.getSheet(0);
			
			//generate node list according to the hashtables
			Node[] hashlinks = new Node[Constant.PRIME]; 
			hashlinks = generateArray(sheet2, Constant.nodenumlist);
			
			//the compare result table
			File compareresult = new File(Constant.compareresult);
			OutputStream file_out = new FileOutputStream(compareresult);		
			WritableWorkbook workbook = Workbook.createWorkbook(file_out);
			WritableSheet writer = workbook.createSheet("compare_result",0);
			
			int i =0;
			String temps1=null;
			long templ1= 0;
			while(i< Constant.totalblocks1){
				       //read data from the first hashtable
				       //System.out.println(i);
						temps1 = sheet1.getCell(i/Constant.COLUMNS, i%Constant.COLUMNS).getContents();
						templ1 = Long.parseLong(temps1);
						//if the both hash values are same, then the data block is same
						if(findHash(hashlinks, Constant.nodenumlist, templ1))
						{
							    Constant.similar++;
							    i++;
							   Label temp = new Label(i/Constant.COLUMNS, i%Constant.COLUMNS, "A");
								writer.addCell(temp);
							    //break;
						}
						else {
							i++;      
						}
			}
			workbook.write();
			workbook.close();
			//System.out.println("The block number of the first image is: "+Constant.totalblocks1);
			System.out.println("The compared block amount is: "+i);
			System.out.println("The similar block number is: "+Constant.similar);
			double similarity = (double) Constant.similar / Constant.totalblocks1; 
			System.out.println("The similarity ratio is: "+similarity);
			return;
	   }
     
	   //generate node list according to hashtable
	    public Node[]  generateArray(Sheet sheet, int[] nodenumlist){
	    	     int i = 0;
	    	     //initialize the nodenumlist[]
	    	      for (int j=0; j<Constant.PRIME;j++)
	    	    	      nodenumlist[j]=0;
	    	     
	    	     Node[] nodelist = new Node[Constant.PRIME];
	    	     while (i< Constant.totalblocks2){
	    	    	        String tempstr = sheet.getCell(i/Constant.COLUMNS, i%Constant.COLUMNS).getContents();
	    	    	        long abst = Long.parseLong(tempstr);
	    	    	        int position = (int) (Math.abs(abst)%Constant.PRIME);                   //calculate the position of the hash value in the node list
	    	    	        
	    	    	        //put new node into the position calculated
	    	    	        Node tempnode = new Node();
	    	    	        tempnode.setBlocknum(i);
	    	    	        tempnode.setHashvalue(abst);
	    	    	        tempnode.next = null;
	    	    	        if(nodenumlist[position]==0){
	    	    	        	    nodelist[position] = tempnode;
	    	    	        }
	    	    	        else {
	    	    	        	 Node temp = nodelist[position];
 	    	        	     while(temp.getNext()!=null){
 	    	        	    	        temp = temp.getNext();
 	    	        	     }
 	    	        	     temp.setNext(tempnode);
	    	    	        }
	    	    	        nodenumlist[position]++;
	    	    	        i++;
	    	     }
	    	     
				return nodelist;	 
	    }
	    
	    public boolean findHash(Node[] nodelist, int[] nodenumlist, long hashvalue){
	    				int position =(int) (Math.abs(hashvalue)%Constant.PRIME);
	    				//if this position does not have a node
	    				if (nodenumlist[position]==0)
	    					    return false;
	    				//if this position has  nodes
	    				Node tempnode = nodelist[position];
	    				//String tempstr1 = Long.toString(hashvalue);
	    				while(tempnode!=null){
	    					     long templong = tempnode.getHashvalue();
	    					     if(templong == hashvalue)
	    					    	    return true;
	    					     else 
	    					    	    tempnode = tempnode.getNext();
	    				}
						return false;
	    }
}
