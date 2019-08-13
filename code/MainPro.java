import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import ilog.concert.IloColumn;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLPMatrix;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloObjective;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

public class MainPro {
	 private static final double RC_EPS = 1.0e-6;

	 //public static HashSet<Integer> A=new HashSet<Integer>();

		public static double interval_time(long t1,long t2){//输出单位为秒
	        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	        Date date1 = new Date(t1);
	        Date date2 = new Date(t2);
	        long res=(date2.getTime()-date1.getTime());
	        return res;
	    }
		public static String getDay(long t1) {  
	        Date currentTime = new Date(t1* 1000L);  
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        String dateString = formatter.format(currentTime);  
	        String day;  
	        day = dateString.substring(8, 10);  
	        return day;  
	    }  
		public static String getHour(long t1) {  
	        Date currentTime = new Date(t1* 1000L);  
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        String dateString = formatter.format(currentTime);  
	        String hour;  
	        hour = dateString.substring(11, 13);  
	        return hour;  
	    }  
	    public static String getMinute(long t1) {  
	        Date currentTime = new Date(t1*1000L);  
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        String dateString = formatter.format(currentTime); 
	        //System.out.println(dateString);
	        String min;  
	        min = dateString.substring(14, 16);  
	        return min;  
	    } 
	    public static String getSecond(long t1) {  
	        Date currentTime = new Date(t1*1000L);  
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	        String dateString = formatter.format(currentTime); 
	        //System.out.println(dateString);
	        String second;  
	        second = dateString.substring(17, 19);  
	        return second;  
	    }
	    public static int transcity(Set<String> citysets,String c){
	    	String[] d=new String[citysets.size()];
	    	citysets.toArray(d);
	    	int k=-1;
	    	for(int i=0;i<d.length;i++){
	    		if(d[i].equals(c)){
	    			k=i+1;
	    			break;
	    		}
	    	}
	    	return k;
	    }
	    public static String getTime(long t1){
	    	 Date currentTime = new Date(t1* 1000L);  
	         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	         String dateString = formatter.format(currentTime);
	         return dateString;
	    }

	static class IloNumVarArray {
	      int _num           = 0;
	      IloNumVar[] _array = new IloNumVar[32];

	      void add(IloNumVar ivar) {
	         if ( _num >= _array.length ) {
	            IloNumVar[] array = new IloNumVar[2 * _array.length];
	            System.arraycopy(_array, 0, array, 0, _num);
	            _array = array;
	         }
	         _array[_num] = ivar;
	         _array[_num].setName("x"+String.valueOf(_num));
	         _num+=1;
	      }

	      IloNumVar getElement(int i) { return _array[i]; }
	      int       getSize()         { return _num; }
	   }
	 public static void main(String[] args) throws IOException, ParseException, IloException{
	    	//int aT=1518;int rest_num=1420;int init=100;int t_count=6;
	    	int aT=1987;int rest_num=1420;
	    	int init=664;int t_count=6;
	    	
	    	IloCplex cutSolver = new IloCplex();
	    	IloObjective RollsUsed = cutSolver.addMinimize();
	    	IloRange[]   Fill = new IloRange[aT];
	        for (int f = 0; f < rest_num; f++ ) {
	            Fill[f] = cutSolver.addRange(1, Double.MAX_VALUE,"c"+String.valueOf(f));
	            }
	        for (int f=rest_num;f<aT; f++ ) {
	        	Fill[f] = cutSolver.addRange(0, Double.MAX_VALUE,"c"+String.valueOf(f));
	        	}
	        System.out.println("-----");
	        int[][] initials=new int[init][aT];
	        for(int i=0;i<init;i++){for(int j=0;j<aT;j++){initials[i][j]=0;}}
	        @SuppressWarnings("resource")
			Scanner in1 =new Scanner(Paths.get("/Users/jessicanini/Desktop/AA/Main/initial_1.txt"));
	        for(int i=0;i<init;i++){
	        	String[] temp=in1.nextLine().split(",");
	        	//for(String e:temp){System.out.print(e+",");}
	        	
	        	for(String e1:temp){
	        		initials[i][Integer.parseInt(e1)]=1;
	        	}
	        	//System.out.println("--");
	        }
	       // for(int i=0;i<init;i++){for(int j=0;j<aT;j++){System.out.printf("%d,%s",initials[i][j]," ");}
	      //  System.out.println();}
	        IloNumVarArray Cut = new IloNumVarArray();
	        //IloColumn column = cutSolver.column(RollsUsed, 1.);//System.out.println(column);//System.out.println(11);
	        System.out.println(11);
	        //initial variable
	        for ( int p = 0; p < init; p++){
	        	IloColumn column = cutSolver.column(RollsUsed, 1.);
	        	for (int f=0; f<aT; f++ ) {
	        		column = column.and(cutSolver.column(Fill[f], initials[p][f]));
	        	}
	        	//System.out.println("----");
	            Cut.add(cutSolver.numVar(column, 0., Double.MAX_VALUE));
	        }
	        cutSolver.exportModel("/Users/jessicanini/Desktop/AA/Main/lps/First_main"+String.valueOf(init)+".lp");
	        System.out.println("----");
	        //Finish initial main	  
	        
	        //=============build sub==============
	        //IloCplex patSolver = new IloCplex();
	        // patSolver=build_sub();//patSolver.importModel("/Users/jessicanini/Desktop/AA/Main/sub0.lp");
	        @SuppressWarnings("resource")
			Scanner in11 =new Scanner(Paths.get("/Users/jessicanini/Desktop/AA/data2.txt"));
	    	//int aT=Integer.parseInt(in1.nextLine().split("\\s+")[0]);
	   	
	    	ArrayList<Integer> N_slocs=new ArrayList<Integer>();ArrayList<Integer> N_elocs=new ArrayList<Integer>();
	    	ArrayList<Integer> N_blocs=new ArrayList<Integer>();ArrayList<Integer> blocs=new ArrayList<Integer>();
	    	
	    	Set<String> citysets=new HashSet<String>();
	    	ArrayList<String> citys=new ArrayList<String>();
	    	//String[] citys1=new String[]{"HGH","URC","CGQ","PEK","ENY","WNZ","XIY","KMG","CTU","CAN","CSX","HRB","WNZ","SZX"};
            String[] citys1=new String[]{"NZL","RIZ","CTU","TNA","NNG","BFJ","LJG","XIY","HJJ","KLO","UTP","SHE","MDL","XNN","NBS","CIF","XFN","KMG","WDS","SQD","HGH","NGB","MIG","WUH","NKG","UYN","AVA","CAN","BPE","WUT","DLC","WEH","ENH","INC","DTU","HLH","URC","JHG","KJH","HDG","CJU","SZX","ENY","DYG","YNJ","MFM","LYA","KWE","LHW","LYG","LYI","YNT","CKG","KWL","HIA","WNZ","TYN","CSX","CGO","AKU","ZYI","CGQ","ZUH","HUZ","KOS","KOW","JIQ","HAK","HZG","HAN","ACX","HET","DAT","BAV","PEK","HRB","SGN","XUZ","CXR","TEN","NZH"};

	    	for(String e:citys1){citys.add(e);}
	    	
	    	Flight[] flights=new Flight[aT];
	    	for(int i=0;i<aT;i++){
	    		String[] temp=in11.nextLine().split(",");
	    		int Index=i;
	    		int Plane_index=Integer.parseInt(temp[0]);
	    		String Plane_serial=temp[1];
	    		String Flight_serial=temp[2];
	    		int Flight_seq=Integer.parseInt(temp[3]);
	    		String Start=temp[4];
	    		String End=temp[5];
	    		if(citys.contains(Start) && citys.contains(End)){blocs.add(i);}
	    		if(!citys.contains(Start) && citys.contains(End)){N_slocs.add(i);}
	    		if(citys.contains(Start) && !citys.contains(End)){N_elocs.add(i);}
	    		if(!citys.contains(Start) && !citys.contains(End)){N_blocs.add(i);}
	    		citysets.add(Start);citysets.add(End);
	    		long StartTime=Long.parseLong(temp[6]);
	    		long EndTime=Long.parseLong(temp[7]);
	    		flights[i]=new Flight(rest_num,Index, Plane_index,Plane_serial,Flight_serial,Flight_seq,Start,End,StartTime,EndTime);
	    	}System.out.println("-");
	    	//for(int i=0;i<10;i++){
	    		//System.out.println(flights[i].Start);//System.out.println(flights[i].duration);
	    	//}
	    	//Xlists=[]#[[index],[],[]] NXlists=[] Ylists=[] NYlists=[] S_locs=[] E_locs=[]
	    	//N_slocs=[] N_elocs=[] N_blocs=[] blocs=[]
	    	int[][] Xlists=new int[aT][aT];int[][] NXlists=new int[aT][aT];
	    	int[][] Ylists=new int[aT][aT];int[][] NYlists=new int[aT][aT];
	       	for(int i=0;i<aT;i++){
	    		for(int j=0;j<aT;j++){
	    			Xlists[i][j]=0;Ylists[i][j]=0;
	    			NXlists[i][j]=1;NYlists[i][j]=1;
	    		}}System.out.println("--");
	    	//for Xlists System.out.println("---");
	    	for(int i=0;i<aT;i++){
	    		for(int j=0;j<aT;j++){
	    			if(j!=i && flights[i].Index<rest_num && flights[j].Index<rest_num){
	    				int day1=Integer.parseInt(getDay(flights[i].EndTime));
	    				int day2=Integer.parseInt(getDay(flights[j].StartTime));
	    				if(day1==day2){
	    					if(flights[i].End.equals(flights[j].Start)){
	    					if(flights[i].Plane_index==flights[j].Plane_index){
	    						if(interval_time(flights[i].EndTime,flights[j].StartTime)>=0){
	    							Xlists[i][j]=1;}}
	    					else{if(interval_time(flights[i].EndTime,flights[j].StartTime)>=1.5*3600){Xlists[i][j]=1;}
	    					}}
	    				}
	    			}
	    			}}System.out.println("---");
	        //for NXlists
	    	for(int i=0;i<aT;i++){
	    		for(int j=0;j<aT;j++){
	    			if(Xlists[i][j]==1){NXlists[i][j]=0;}
	    		}}
	    	//for Ylists for rest?
	    	for(int i=0;i<aT;i++){
	    		for(int j=0;j<aT;j++){
	    			if(j!=i){
	    				if(flights[i].Index>=rest_num || flights[j].Index>=rest_num){
	    					int day1=Integer.parseInt(getDay(flights[i].EndTime));
	        				int day2=Integer.parseInt(getDay(flights[j].StartTime));
	        				if(day1+1==day2){
	        					if(flights[i].End.equals(flights[j].Start)){
	        						if(citys.contains(flights[i].End)){Ylists[i][j]=1;}}
	        					}
	    				}
	    				else{
	    				int day3=Integer.parseInt(getDay(flights[i].EndTime));
	    				int day4=Integer.parseInt(getDay(flights[j].StartTime));
	    				if(day3+1==day4){
	    					if(flights[i].End.equals(flights[j].Start)){
	    						if(citys.contains(flights[i].End)){
	    							if(interval_time(flights[i].EndTime,flights[j].StartTime)>=(10+1.5)*3600){
	    							Ylists[i][j]=1;}}}}
	    				}
	    			}
	    			}}//for NYlists
	    	System.out.println("----");
	    	for(int i=0;i<aT;i++){
	    		for(int j=0;j<aT;j++){
	    			if(Ylists[i][j]==1){NYlists[i][j]=0;}
	    		}}
	    	//for start and end city
	    	int[] S_locs=new int[aT];int[] E_locs=new int[aT];
	    	for(int i=0;i<aT;i++){
	    		S_locs[i]=transcity(citysets,flights[i].Start);
	    		E_locs[i]=transcity(citysets,flights[i].End);
	    	}
	    	int[] Flight_time=new int[aT];int[] On_time=new int[aT];
	    	for(int i=0;i<aT;i++){
	    		int h1=Integer.parseInt(getHour(flights[i].StartTime));
	    		int m1=Integer.parseInt(getMinute(flights[i].StartTime));
	    		double t1=h1+1.0*m1/60.0-1.5;
	    		if(t1<0){t1=t1+24;}
	    		if(t1>=0 && t1<=(4+1.0*59/60.0)){Flight_time[i]=8*3600;On_time[i]=12*3600;}
	    		if(t1>=5 && t1<=(19+1.0*59/60.0)){Flight_time[i]=9*3600;On_time[i]=14*3600;}
	    		if(t1>=20 && t1<=(23+1.0*59/60.0)){Flight_time[i]=8*3600;On_time[i]=13*3600;}
	    	}
	    	for(int i=rest_num;i<aT;i++){
	    		Flight_time[i]=9*3600;On_time[i]=14*3600;
	    	}
	    	int sum1=0;int sum2=0;
	    	for(int i=0;i<aT;i++){
	    		int tmp1=0;
	    		for(int j=0;j<aT;j++){
	    			//if(Xlists[i][j]==1){System.out.printf("%d , %d",i,j);}
	    			//System.out.print(Xlists[i][j]);System.out.print(" ");
	    		tmp1+=Xlists[i][j];
	    		//System.out.println(tmp1);
	    		}
	    		//System.out.println("next");
	    		if(tmp1>sum1){sum1=tmp1;}}
	    	for(int i=0;i<aT;i++){
	    		int tmp2=0;
	    		for(int j=0;j<aT;j++){
	    		tmp2+=Ylists[i][j];}
	    		//System.out.println(tmp2);
	    		if(tmp2>sum2){sum2=tmp2;}}
	    	
	    	ArrayList<ArrayList<Integer>> nXlists=new ArrayList<ArrayList<Integer>>();
	    	ArrayList<ArrayList<Integer>> nYlists=new ArrayList<ArrayList<Integer>>();
	    	for(int i=0;i<aT;i++){
	    		ArrayList<Integer> tmp=new ArrayList<Integer>();
	    		for(int j=0;j<aT;j++){
	    		if(Xlists[i][j]==1){tmp.add(j);}}
	    		nXlists.add(tmp);}
	    	for(int i=0;i<aT;i++){
	    		ArrayList<Integer> tmp=new ArrayList<Integer>();
	    		for(int j=0;j<aT;j++){
	    		if(Ylists[i][j]==1){tmp.add(j);}}
	    		nYlists.add(tmp);}
	    	
	    	Flight[] candidate=new Flight[aT];
	    	for(int i=0;i<aT;i++){
	    		candidate[i]=flights[i];
	    	}
	    	//System.out.printf("%d, %d",sum1,sum2);
	    	System.out.printf("%d,%d",sum1,sum2);
	    	System.out.println();
	    	System.out.println("Start X");
	    	for(int i=0;i<nXlists.size();i++){
	    		for(int j=0;j<nXlists.get(i).size();j++){
	    			//System.out.printf("%d, %d",i,nXlists.get(i).get(j));
	    		}//System.out.println();
	    	}
	    	System.out.println("Start Y");
	    	for(int i=0;i<nYlists.size();i++){
	    		for(int j=0;j<nYlists.get(i).size();j++){
	    			//System.out.printf("%d, %d",i,nYlists.get(i).get(j));
	    		}//System.out.println();
	    	}
	        int variable_count=0;
	    	int st_count=0;
	    	//int xc=4;int yc1=3;int yc2=1;
	    	int xc=0;int yc1=0;int yc2=0;
	    	IloCplex cplex=new IloCplex();
	    	//IloObjective ReducedCost = cplex.getObjective();
	    	IloObjective ReducedCost = cplex.addMinimize();////****************
	    	
	        IloIntVar[][][] x = new IloIntVar[aT][sum1][t_count];
	        IloIntVar[][][] y = new IloIntVar[aT][sum2][t_count-1];
	        IloIntVar[][] s = new IloIntVar[aT][t_count];
	        IloIntVar[][] e = new IloIntVar[aT][t_count];
	        IloIntVar[] z = new IloIntVar[3];
	         // ---build model---//
	         IloLinearNumExpr exprObj0 = cplex.linearNumExpr();
	         IloLinearNumExpr exprObj1 = cplex.linearNumExpr();
	         for (int i = 0; i < aT; i++) {
	             for (int j = 0; j < sum1; j++) {
	            	 for(int p=0;p<t_count;p++){
	            		 if(j<nXlists.get(candidate[i].Index).size()){
	                     x[i][j][p] = cplex.intVar(0, 1,"x" + candidate[i].Index + "," + nXlists.get(candidate[i].Index).get(j) + "," + p);}
	            		 else{ x[i][j][p] = cplex.intVar(0, 0);}//,"x"+String.valueOf(x11));x11+=1;}
	                     variable_count+=1; //dis.add(length(getPoint(knum,knumber,r1),facilities.get(p).location));
	                     exprObj1.addTerm(xc,x[i][j][p]);
	                     exprObj0.addTerm(xc,x[i][j][p]);
	                     }}}
	         
	         IloLinearNumExpr exprObj2 = cplex.linearNumExpr();
	         for (int i = 0; i < aT; i++) {
	             for (int j = 0; j < sum2; j++) {
	            	 for(int p=0;p<t_count-1;p++){
	            		 //System.err.println(nYlists.get(candidate[i].Index).size());
	            		 if(j<nYlists.get(candidate[i].Index).size()){
	                         y[i][j][p] = cplex.intVar(0, 1,"y" + candidate[i].Index + "," + nYlists.get(candidate[i].Index).get(j) + "," + p);
	                         //System.out.println(y[i][j][p]);
	                         variable_count+=1; 
	                         if(candidate[i].Index<rest_num && nYlists.get(candidate[i].Index).get(j)<rest_num){
	                        	 exprObj2.addTerm(yc1,y[i][j][p]);
	                        	 exprObj0.addTerm(yc1,y[i][j][p]);
	                        	 }
	                         else{
	                        	 exprObj2.addTerm(yc2,y[i][j][p]);
	                        	 exprObj0.addTerm(yc2,y[i][j][p]);
	                        	 }
	                         }
	                		 else{ y[i][j][p] = cplex.intVar(0, 0);//,"y"+String.valueOf(y11));y11+=1;
	                		 variable_count+=1; 
	                		 exprObj2.addTerm(yc2,y[i][j][p]);
	                		 exprObj0.addTerm(yc2,y[i][j][p]);
	                		 }}}}
	         IloLinearNumExpr exprObj3 = cplex.linearNumExpr();
	         for (int i = 0; i < aT; i++) {
	            	 for(int p=0;p<t_count;p++){
	                     s[i][p] = cplex.intVar(0, 1, "s" + candidate[i].Index + "," + p);
	                     variable_count+=1; 
	                     exprObj3.addTerm(0,s[i][p]);
	                     exprObj0.addTerm(0,s[i][p]);
	                     }}
	         IloLinearNumExpr exprObj4 = cplex.linearNumExpr();
	         for (int i = 0; i < aT; i++) {
	            	 for(int p=0;p<t_count;p++){
	                     e[i][p] = cplex.intVar(0, 1, "e" + candidate[i].Index + "," + p);
	                     variable_count+=1; 
	                     exprObj4.addTerm(0,e[i][p]);
	                     exprObj0.addTerm(0,e[i][p]);
	                     }}
	         
	         IloLinearNumExpr exprObj5 = cplex.linearNumExpr();
	         for (int i = 0; i < 3; i++) {
	                     z[i] = cplex.intVar(0, 1, "z" +i);
	                     variable_count+=1; 
	                     exprObj5.addTerm(0,z[i]);
	                     exprObj0.addTerm(0,z[i]);
	                     }
	        // System.out.println(exprObj0);
	         ReducedCost.setExpr(exprObj0);
	       //  System.out.println(ReducedCost);
	         
	         //System.out.printf("%s %d","Total variables:",variable_count);
	         System.out.println();
	         
	         System.out.println("Start constraint");
	         // ---s&e constraint 1 & 2---//
	         IloLinearIntExpr constraint1 = cplex.linearIntExpr();
	         for (int i = 0; i < aT; i++) {
	        	 for(int p=0;p<t_count;p++){
	        		 constraint1.addTerm(1, s[i][p]);}}
		     cplex.addEq(constraint1,1);
		     st_count+=1;System.out.println(st_count);
		     IloLinearIntExpr constraint2 = cplex.linearIntExpr();
	         for (int i = 0; i < aT; i++) {
	        	 for(int p=0;p<t_count;p++){
	        		 constraint2.addTerm(1, e[i][p]);}}
		     cplex.addEq(constraint2,1);
		     st_count+=1;System.out.println(st_count);
		     // ---s&e constraint 3---//
		     IloLinearIntExpr constraint4 = cplex.linearIntExpr();
		     for(int i=0;i<candidate.length;i++){
		    	 for(int p=0;p<t_count;p++){
		    		 constraint4.addTerm(S_locs[i], s[i][p]);
		    		 constraint4.addTerm(-E_locs[i], e[i][p]);}
		     }
		     cplex.addEq(constraint4,0);
		    // System.out.println("-1");
		     st_count+=1;//System.out.println(st_count);
	         // ---assignment constraint ---//
		     for(int p=0;p<t_count;p++){
		    	 for(int i=0;i<aT;i++){
		    		 IloLinearIntExpr constraint3 = cplex.linearIntExpr();
		    		 constraint3.addTerm(1, s[i][p]);
		    		 constraint3.addTerm(-1, e[i][p]);
		    		 for(int j3=0;j3<nXlists.get(candidate[i].Index).size();j3++){constraint3.addTerm(-1, x[i][j3][p]);}
		    		 for(int j4=0;j4<aT;j4++){//System.err.println(j4);
		    			 if(nXlists.get(candidate[j4].Index).contains(candidate[i].Index)){
		    				// System.err.printf("%d, %d",j4,nXlists.get(candidate[j4].Index).indexOf(candidate[i].Index));
		    				 constraint3.addTerm(1, x[j4][nXlists.get(candidate[j4].Index).indexOf(candidate[i].Index)][p]);}}
			    		
		    		 for(int j1=0;j1<nYlists.get(candidate[i].Index).size();j1++){if(p<t_count-1){constraint3.addTerm(-1, y[i][j1][p]);}}
		    		 for(int j2=0;j2<aT;j2++){if(nYlists.get(candidate[j2].Index).contains(candidate[i].Index) && p<t_count-1){
	    				if(p>=1){constraint3.addTerm(1, y[j2][nYlists.get(candidate[j2].Index).indexOf(candidate[i].Index)][p-1]);}}}
		    		 cplex.addEq(constraint3,0);
		    		// System.out.println("-2");
		    		 st_count+=1;//System.out.println(st_count);
		    	 }}
		  // ---Flight_time constraint ---//
		     for(int p=0;p<t_count;p++){
		    	 IloLinearIntExpr constraint5 = cplex.linearIntExpr();
		    	 for(int i=0;i<candidate.length;i++){
		    		 constraint5.addTerm(-1*Flight_time[i], s[i][p]);
		    		 constraint5.addTerm((int) candidate[i].duration, e[i][p]); 
		    		 for(int j=0;j<nXlists.get(candidate[i].Index).size();j++){
		    			 if(i!=nXlists.get(candidate[i].Index).get(j)){
		    				 constraint5.addTerm((int) candidate[i].duration, x[i][j][p]);//-1,xi,j,t
		    			 }
		    		 }//for j
		    		 for(int j1=0;j1<nYlists.get(candidate[i].Index).size();j1++){
		    			 if(i!=nYlists.get(candidate[i].Index).get(j1)){
		    				 if(p<t_count-1){constraint5.addTerm((int) candidate[i].duration, y[i][j1][p]);
		    					 if(p>=1){constraint5.addTerm(-1*Flight_time[j1], y[i][j1][p-1]);}}//-1,yi,j,t
		    			 }
		    		 }//for j1
		    	 }//for i
		    	 cplex.addLe(constraint5,0);
		    	// System.out.println("-3");
		    	 st_count+=1;//System.out.println(st_count);
		    	 }
		 
		  // ---variables equals 0---//
		    IloLinearIntExpr constraint8 = cplex.linearIntExpr();
		    for(int i=0;i<candidate.length;i++){
		    	for(int p=0;p<t_count;p++){
		    		if(N_slocs.contains(candidate[i].Index) || N_blocs.contains(candidate[i].Index)){
		    			constraint8.addTerm(1, s[i][p]);
		    			st_count+=1;//System.out.println(st_count)
		    		}}}
		    cplex.addEq(constraint8,0);
		    
		    IloLinearIntExpr constraint9 = cplex.linearIntExpr();
		    for(int i=0;i<candidate.length;i++){
		    	if(N_blocs.contains(candidate[i].Index)){
		    		 for(int j=0;j<nYlists.get(candidate[i].Index).size();j++){
		    			 for(int p=0;p<t_count-1;p++){
		    				 constraint9.addTerm(1, y[i][j][p]);
		    				 }}
		    		 for(int j1=0;j1<aT;j1++){
		    			 for(int p=0;p<t_count-1;p++){
		    				 if(nYlists.get(candidate[j1].Index).contains(candidate[i].Index)){
		    				 constraint9.addTerm(1, y[j1][nYlists.get(candidate[j1].Index).indexOf(candidate[i].Index)][p]);}
		    				 st_count+=1;//System.out.println(st_count)
		    				 }}
		    		 }
		    	
		    	if(N_slocs.contains(candidate[i].Index)){
		    		 for(int j=0;j<aT;j++){
		    			 for(int p=0;p<t_count-1;p++){
		    				 if(nYlists.get(candidate[j].Index).contains(candidate[i].Index)){
		    				 constraint9.addTerm(1, y[j][nYlists.get(candidate[j].Index).indexOf(candidate[i].Index)][p]);
		    				 st_count+=1;//System.out.println(st_count)
		    				 }}}}
		    	if(N_elocs.contains(candidate[i].Index)){
		    		for(int j=0;j<nYlists.get(candidate[i].Index).size();j++){
		    			 for(int p=0;p<t_count-1;p++){
		    				 constraint9.addTerm(1, y[i][j][p]);
		    				 st_count+=1;//System.out.println(st_count)
		    				 }}}
		    }cplex.addEq(constraint9,0);
		    
		    // ---Work_time constraint ---//
			     for(int p=0;p<t_count;p++){
			    	 IloLinearIntExpr constraint6 = cplex.linearIntExpr();
			    	 IloLinearIntExpr constraint7 = cplex.linearIntExpr();
			    	 for(int i=0;i<candidate.length;i++){
			    		 constraint6.addTerm(-1*3600, e[i][p]); //-1,ei,p航班段数
			    		 
			    		 constraint6.addTerm((int) (candidate[i].StartTime)+On_time[i], s[i][p]);//开始时间
			    		 constraint6.addTerm((int) (-1*candidate[i].EndTime), e[i][p]); //结束时间
			    		 constraint6.addTerm((int) (-1.5*3600), e[i][p]);//是否有报到时间
			    		 
			    		 constraint7.addTerm((int) (candidate[i].StartTime)+On_time[i], s[i][p]);//开始时间
			    		 constraint7.addTerm((int) (-1*candidate[i].EndTime), e[i][p]); //结束时间
			    		 constraint7.addTerm((int) (-1.5*3600), e[i][p]);//是否有报到时间
			    		 
			    		 for(int j=0;j<nXlists.get(candidate[i].Index).size();j++){
			    			 if(i!=nXlists.get(candidate[i].Index).get(j)){
			    				 constraint6.addTerm(-1*3600, x[i][j][p]);//-1,xi,j,p航班段数
			    				 }}//for j
			    		 
			    		 for(int j1=0;j1<nYlists.get(candidate[i].Index).size();j1++){
			    			 if(i!=nYlists.get(candidate[i].Index).get(j1) ){
			    				 if(p<t_count-1){
			    				 constraint6.addTerm(-1*3600, y[i][j1][p]);//-1 yi,j,p航班段数
			    				 constraint6.addTerm((int) (-1*candidate[i].EndTime), y[i][j1][p]); //结束时间
			    				 constraint6.addTerm((int) (-1.5*3600), y[i][j1][p]);//1 yi,j,p是否有报到时间
			    				 
			    				 constraint7.addTerm((int) (-1*candidate[i].EndTime), y[i][j1][p]);//结束时间
			    				 constraint7.addTerm((int) (-1.5*3600), y[i][j1][p]);//是否有报到时间
			    				 
			    					 if(p>=1){
			    					 constraint6.addTerm((int) (candidate[nYlists.get(candidate[i].Index).get(j1)].StartTime+On_time[nYlists.get(candidate[i].Index).get(j1)]), y[i][j1][p-1]);//开始时间
			    					 constraint7.addTerm((int) (candidate[nYlists.get(candidate[i].Index).get(j1)].StartTime+On_time[nYlists.get(candidate[i].Index).get(j1)]), y[i][j1][p-1]);}//开始时间
			    					 }//-1,yi,j,t
			    				 }
			    		 }//for j1
			    		 
			    	 }//for i
			    	// System.out.println("-4");
			    	 cplex.addGe(constraint6,-4*3600);
			    	 st_count+=1;
			    	// System.out.println("-5");
			    	// if(p==4){
			    	//	 System.err.println(constraint7);}
			    	 cplex.addGe(constraint7,0);//+1.5*3600);
			    	 st_count+=1;
			    	 }

		    //for rest 
			IloLinearIntExpr constraintz1 = cplex.linearIntExpr();
		    constraintz1.addTerm(1, z[0]);constraintz1.addTerm(1, z[1]);constraintz1.addTerm(1, z[2]);
		    cplex.addGe(constraintz1,1);
				    	
		    IloLinearIntExpr constraint13 = cplex.linearIntExpr();//z0
		    IloLinearIntExpr constraint14 = cplex.linearIntExpr();//z1
		    IloLinearIntExpr constraint15 = cplex.linearIntExpr();//z2
		    int M=10000*3600;int k1=(int) ((96-1.5)*3600);
		    for(int p=0;p<t_count;p++){
		    	for(int i=0;i<candidate.length;i++){
		    		 constraint13.addTerm((int) (-1*candidate[i].StartTime), s[i][p]);
		    		 constraint13.addTerm((int) candidate[i].EndTime, e[i][p]);
		  
		    	}}
		    constraint13.addTerm(M-k1, z[0]);
		    cplex.addLe(constraint13,M);st_count+=1;
		    
		    for(int p=1;p<t_count-1;p++){
		    	for(int i=0;i<candidate.length;i++){
		    		for(int j=0;j<nYlists.get(candidate[i].Index).size();j++){
		    			if(candidate[i].Index>=rest_num && nYlists.get(candidate[i].Index).get(j)<rest_num && candidate[i].Index!=nYlists.get(candidate[i].Index).get(j)){
		    		 constraint14.addTerm((int) candidate[nYlists.get(candidate[i].Index).get(j)].StartTime, y[i][j][p]);
		    		 }
		    		}}}
		    
		    for(int p=0;p<t_count-2;p++){
		    	for(int i=0;i<candidate.length;i++){
		    		for(int j=0;j<nYlists.get(candidate[i].Index).size();j++){
		    			if(candidate[i].Index>=rest_num && nYlists.get(candidate[i].Index).get(j)<rest_num && candidate[i].Index!=nYlists.get(candidate[i].Index).get(j)){
		    		 constraint14.addTerm((int) (-1*candidate[i].EndTime), y[i][j][p]);
		    		 }
		    		}}}	
		    constraint14.addTerm((int) (-48-1.5)*3600, z[1]);
		    cplex.addGe(constraint14,0);st_count+=1;
		    
		    for(int p=0;p<t_count-1;p++){
		    	for(int i=0;i<candidate.length;i++){
		    		for(int j=0;j<nYlists.get(candidate[i].Index).size();j++){
		    			if(candidate[i].Index>=rest_num && nYlists.get(candidate[i].Index).get(j)>=rest_num && candidate[i].Index!=nYlists.get(candidate[i].Index).get(j)){
		    		 constraint15.addTerm(1, y[i][j][p]);
		    		 }
		    		}}}
		    constraint15.addTerm(-1, z[2]);
		    cplex.addGe(constraint15,0);st_count+=1;

		    cplex.setParam(IloCplex.Param.TimeLimit,360);
		    System.out.println("---");
		    System.out.printf("%s %d","Total variables:",variable_count);
		    System.out.printf("%s %d","Total constraints:",st_count);
	        System.out.println();
	        //Finish sub


	        /*
	        IloLPMatrix lp1 = (IloLPMatrix)patSolver.LPMatrixIterator().next();
		  	IloNumVar[] subvars = lp1.getNumVars();*/

	    	int num=1;
	    	System.out.printf("%s %d","Current cut number",Cut._num);System.out.println();
	        while(num<200) {
	        	String filePath1="/Users/jessicanini/Desktop/AA/Main/newcolumn/log"+String.valueOf(num)+".txt";
	    	    FileWriter fw1 = new FileWriter(filePath1);BufferedWriter bw1 = new BufferedWriter(fw1);
	    	    String filePath2="/Users/jessicanini/Desktop/AA/Main/newcheck/log_"+String.valueOf(num)+".txt";
	    	    FileWriter fw2 = new FileWriter(filePath2);BufferedWriter bw2 = new BufferedWriter(fw2);
	    	    String filePath3="/Users/jessicanini/Desktop/AA/Main/newres/res_"+String.valueOf(num)+".txt";
	    	    FileWriter fw3 = new FileWriter(filePath3);BufferedWriter bw3 = new BufferedWriter(fw3);
	            /// OPTIMIZE OVER CURRENT PATTERNS ///
	        	//cutSolver.exportModel("/Users/jessicanini/Desktop/AA/Main/cut"+String.valueOf(num)+".lp");
	            cutSolver.solve();
	            /// FIND AND ADD A NEW PATTERN ///
	            double[] price = cutSolver.getDuals(Fill);
	            double[] newPatt = new double[aT];
	            for(int i=0;i<aT;i++){newPatt[i]=0;}
	            ///////
	            IloLinearNumExpr exprObj = cplex.linearNumExpr();

	            for (int i = 0; i < aT; i++) {
	                for (int j = 0; j < sum1; j++) {
	               	 for(int p=0;p<t_count;p++){
	               		 if(j<nXlists.get(candidate[i].Index).size()){
	               			 exprObj.addTerm(price[candidate[i].Index]+price[nXlists.get(candidate[i].Index).get(j)],x[i][j][p]);}
	               		 else{exprObj.addTerm(0,x[i][j][p]);}
	                        }}}
	            
	            for (int i = 0; i < aT; i++) {
	                for (int j = 0; j < sum2; j++) {
	               	 for(int p=0;p<t_count-1;p++){
	               		 //System.err.println(nYlists.get(candidate[i].Index).size());
	               		 if(j<nYlists.get(candidate[i].Index).size()){
	               			 exprObj.addTerm(price[candidate[i].Index]+price[nYlists.get(candidate[i].Index).get(j)],y[i][j][p]);
	                           	 }
	                   		 else{ 
	                   		 exprObj.addTerm(0,y[i][j][p]);
	                   		 }}}}
	            for (int i = 0; i < aT; i++) {
	               	 for(int p=0;p<t_count;p++){
	                        exprObj.addTerm(0,s[i][p]);
	                        }}
	            for (int i = 0; i < aT; i++) {
	               	 for(int p=0;p<t_count;p++){
	               		 exprObj.addTerm(0,e[i][p]);
	                        }}
	            for (int i = 0; i < 3; i++) {
	            	exprObj.addTerm(0,z[i]);
	                        }
	            
	            
	            ReducedCost.clearExpr();
			  	ReducedCost.setExpr(cplex.diff(0.,exprObj));
	            cplex.exportModel("/Users/jessicanini/Desktop/AA/Main/lps/sub"+String.valueOf(num)+".lp");
	            
	            num+=1;
	            
	           // if ( cplex.getObjValue() > -RC_EPS )
	              // break;
	            
	            //get new pat
	            ArrayList<Integer> solu=new  ArrayList<Integer>();
	            boolean flag=false;
	    	    ArrayList<HashSet<Integer>> tmp=new ArrayList<HashSet<Integer>>();
	    	    for(int t=0;t<t_count;t++){tmp.add(new HashSet<Integer>());}
	    	    try {
	  		      cplex.solve();
	  		      double obj=cplex.getObjValue();
	  		      System.out.println("The optimal objValue is "+String.valueOf(obj));
	  		      boolean feasible=cplex.solve();
	  		      if(feasible){
	  		    	  flag=true;
	  		    	  System.err.println(obj);
	  		    	  System.out.println();
	  		    	  for (int a = 0; a< aT; a++) {
	  		              for (int j = 0; j < sum1; j++) {
	  		             	 for(int p=0;p<t_count;p++){
	  		             		//System.out.println(a);
	  		             		if(cplex.getValue(x[a][j][p])>=1-RC_EPS){
	  		             			System.out.println("XName: "+x[a][j][p].getName()+" Value: "+cplex.getValue(x[a][j][p]));
	  		             			 String[] arr = x[a][j][p].getName().split(",");
	  		             			// for(String a1:arr){System.out.print(a1+" ");}
	  		             			//System.out.println();
	  		        				int d=Integer.parseInt(arr[2]);
	  		        				tmp.get(d).add(Integer.parseInt(arr[0].substring(1)));
	  		        				tmp.get(d).add(Integer.parseInt(arr[1]));
	  		        				}
	  		             	 }}}
	  		    	
	  		    	 // System.out.println("YName: "+y[0][1][2].getName()+"Value: "+cplex.getValue(y[0][1][2]));
	  		       	  for (int a = 0; a < aT; a++) {
	  		              for (int j = 0; j < sum2; j++) {
	  		             	 for(int p=0;p<t_count-1;p++){
	  		             		 if(cplex.getValue(y[a][j][p])>=1-RC_EPS){
	  		             			System.out.println("YName: "+y[a][j][p].getName()+" Value: "+cplex.getValue(y[a][j][p]));
	  		             			 String[] arr = y[a][j][p].getName().split(",");
	  		             			//for(String a1:arr){System.out.print(a1+" ");}System.out.println();
	  		        				if(arr[0].substring(0,1).equals("y")){
	  		        					int d=Integer.parseInt(arr[2]);
	  			        				tmp.get(d).add(Integer.parseInt(arr[0].substring(1)));
	  			        				//tmp.get(d).add(Integer.parseInt(arr[1]));
	  			        				}
	  		             		 }}}}
	  		     	  for (int a = 0; a < aT; a++) {
	  		             	 for(int p=0;p<t_count;p++){
	  		             		 if(cplex.getValue(s[a][p])>=1-RC_EPS){
	  		             			System.out.println("SName: "+s[a][p].getName()+" Value: "+cplex.getValue(s[a][p]));
	  		             			 String[] arr = s[a][p].getName().split(",");
	  		        				if(arr[0].substring(0,1).equals("s")){
	  		        					int d=Integer.parseInt(arr[1]);
	  		        					tmp.get(d).add(Integer.parseInt(arr[0].substring(1)));
	  		             		 }
	  		        				}}}
	  		    	  for (int a = 0; a < aT; a++) {
	  		             	 for(int p=0;p<t_count;p++){
	  		             		 if(cplex.getValue(e[a][p])>=1-RC_EPS){
	  		             			System.out.println("EName: "+e[a][p].getName()+"Value: "+cplex.getValue(e[a][p]));
	  		             			 String[] arr = e[a][p].getName().split(",");
	  		             			if(arr[0].substring(0,1).equals("e")){
	  		             				int d=Integer.parseInt(arr[1]);
	  		        					tmp.get(d).add(Integer.parseInt(arr[0].substring(1)));
	  		        				}
	  		             		 }}}
	  		    	for(int j=0;j<tmp.size();j++){
	  		    		for(Integer h: tmp.get(j)){
	  		    			System.err.print(h+",");
	  		    		}System.err.println();
	  		    		}
	  		    	
	  		      }// if(feasible)
	  	    }//try
	  		   catch (IloException ex) {
	  			      System.err.println("\n!!!Unable to solve the model:\n"
	  			                         + ex.getMessage() + "\n!!!");
	  			      flag=false;
	  			      //System.exit(2);
	  			    }
	  	    if(flag){
	  	    ArrayList<Integer> sol1=new  ArrayList<Integer>();
	  	    for(int j=0;j<tmp.size();j++){
	  	    	double ftime=0;double ztime=0;int fcount=tmp.get(j).size();
	  	    	//需要排序的list
	  	    	if(fcount>=1){
	  		    ArrayList<Flight> list1= new ArrayList<Flight>();
	  		    for(Integer e1: tmp.get(j)){list1.add(candidate[e1]);}
	  		    //开始排序
	  		    Collections.sort(list1, new Comparator<Flight>(){
	  		                public int compare(Flight o1, Flight o2) {
	  		                    //排序属性
	  		                    if(o1.StartTime < o2.StartTime){
	  		                        return -1;
	  		                    }
	  		                    if(o1.StartTime == o2.StartTime){
	  		                        return 0;
	  		                    }
	  		                    return 1;
	  		                }
	  		            }); 
	  		    ztime=list1.get(list1.size()-1).EndTime-list1.get(0).StartTime;
	  		    for(Flight e2:list1){
	  		    	ftime+=e2.EndTime-e2.StartTime;
	  		    bw1.write("执勤期"+String.valueOf(j)+" 航段："+String.valueOf(e2.Index)+" 飞机："+String.valueOf(e2.Plane_index)+" 起飞:"+e2.Start+" 落地:"+e2.End+"起飞时间："+getTime(e2.StartTime)+"落地时间:"+getTime(e2.EndTime));
	  		    bw1.write("\r");
	  		    bw3.write(String.valueOf(e2.Index)+",");
	  		    sol1.add(e2.Index);solu.add(e2.Index);
	  		    }
	  		    ftime=ftime/3600.0;ztime=ztime/3600.0;
	  		    double ft1=Flight_time[list1.get(0).Index];
	  		    double tt1=On_time[list1.get(0).Index];
	  		    ft1=ft1/3600.0;tt1=tt1/3600.0;double diff=Math.max(fcount-4,0);
	  		    bw2.write("执勤期"+String.valueOf(j)+" 航段数目:"+String.valueOf(fcount)+" 总飞行限制："+String.format("%.2f",ft1)+" 总执勤限制:"+String.format("%.2f",tt1-diff)+" 计算: "+String.format("%.2f",tt1)+"-"+String.format("%.2f",diff));
	  		    bw2.write("\r");
	  		    bw2.write("执勤期"+String.valueOf(j)+" 航段数目:"+String.valueOf(fcount)+" 总飞行时间："+String.format("%.2f",ftime)+" 总执勤时间:"+String.format("%.2f",ztime+1.5)+" 计算: "+String.format("%.2f",ztime)+"+"+String.format("%.2f",1.5)+"H报到时间");
	  		    bw2.write("\r");}
	  	    	}
	  	    double otime=candidate[sol1.get(sol1.size()-1)].EndTime-candidate[sol1.get(0)].StartTime;
	  	    if(otime>3600*94.5){
	  	    	System.err.printf("%s %d","执勤期:",num);
	  	    }
	  	    otime=otime/3600.0;
	  	    bw1.write("总执勤期耗时: "+String.format("%.2f",otime));bw1.write("\r");
	  	    
	  	    bw1.flush();bw1.close();fw1.close();
	  	    bw2.flush();bw2.close();fw2.close();
	  	    bw3.flush();bw3.close();fw3.close();}//for flag
	  	    
	  	    for(Integer e1:solu){
	  	    	newPatt[e1]=1;
	  	    }
           //finish get new pat
	  	    
	  	   //Start add column
	            IloColumn newcolumn = cutSolver.column(RollsUsed, 1.);
	            for ( int p = 0; p < newPatt.length; p++ ){
	               newcolumn = newcolumn.and(cutSolver.column(Fill[p], newPatt[p]));
	               }
	            System.out.println(Cut._num);
	            Cut.add(cutSolver.numVar(newcolumn, 0., Double.MAX_VALUE) );
	            System.out.println(Cut._num);
	            //System.out.println(Cut);
	            cutSolver.exportModel("/Users/jessicanini/Desktop/AA/Main/lps/Main"+String.valueOf(num)+".lp");
	           }


}
	
}
