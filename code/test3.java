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
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class test3 {
    private static final double RC_EPS = 1.0e-6;
    public static HashSet<Integer> A=new HashSet<Integer>();
    //{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 99, 100, 101, 102, 106, 107, 108, 109, 113, 114, 115, 116, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 167, 168, 169, 170, 171, 175, 176, 182, 183, 188, 221, 228, 235, 236, 242, 243, 252, 255, 260, 261, 262, 264, 265, 267, 268, 269, 271, 272, 275, 283, 284, 344, 348, 351, 353, 354, 358, 365, 372, 373, 374, 375, 413, 414, 420, 421, 509, 516, 523, 529, 530, 533, 535, 540, 542, 544, 547, 549, 550, 551, 552, 561, 562, 571, 609, 611, 612, 614, 620, 627, 646, 650, 653, 654, 656, 657, 658, 660, 661, 662, 663, 664, 669, 672, 676, 679, 683, 684, 690, 691, 694, 695, 696, 698, 699, 716, 720, 721, 723, 724, 904, 906, 964, 967, 971, 972, 973, 974, 975, 976, 977, 980, 981, 983, 984, 988, 995, 1002, 1006, 1008, 1009, 1010, 1011, 1012, 1013, 1034, 1040, 1067, 1068, 1071, 1078, 1087, 1094, 1102, 1209, 1216, 1236, 1247, 1248, 1250, 1251, 1258, 1265, 1413, 1414, 1416, 1417, 1418, 1419, 1423, 1424, 1425, 1432, 1433, 1437, 1451, 1468, 1506, 1508, 1509, 1510};

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
    public static void build_model(boolean expo,int xc,int yc1,int yc2,int whichi,Flight[] candidate,int rest_num,int sum1,int sum2,ArrayList<ArrayList<Integer>> nXlists,ArrayList<ArrayList<Integer>> nYlists,int[] S_locs,int[] E_locs,int[] Flight_time,int[] On_time,
    		ArrayList<Integer> N_slocs,ArrayList<Integer> N_elocs,ArrayList<Integer> N_blocs,ArrayList<Integer> blocs) throws IloException, IOException{
    	int aT=candidate.length;
    	int t_count=6;
    	int variable_count=0;
    	int st_count=0;
    	//int xc=4;int yc1=3;int yc2=1;
    	//int xc=0;int yc1=0;int yc2=0;
    	IloCplex cplex=new IloCplex();
    	
        IloIntVar[][][] x = new IloIntVar[aT][sum1][t_count];
        IloIntVar[][][] y = new IloIntVar[aT][sum2][t_count-1];
        IloIntVar[][] s = new IloIntVar[aT][t_count];
        IloIntVar[][] e = new IloIntVar[aT][t_count];
        IloIntVar[] z = new IloIntVar[3];
         // ---build model---//
         IloLinearNumExpr exprObj1 = cplex.linearNumExpr();
         for (int i = 0; i < aT; i++) {
             for (int j = 0; j < sum1; j++) {
            	 for(int p=0;p<t_count;p++){
            		 if(j<nXlists.get(candidate[i].Index).size()){
                     x[i][j][p] = cplex.intVar(0, 1,"x" + candidate[i].Index + "," + nXlists.get(candidate[i].Index).get(j) + "," + p);}
            		 else{ x[i][j][p] = cplex.intVar(0, 0);}//,"x"+String.valueOf(x11));x11+=1;}
                     variable_count+=1; //dis.add(length(getPoint(knum,knumber,r1),facilities.get(p).location));
                     exprObj1.addTerm(xc,x[i][j][p]);
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
                        	 }
                         else{
                        	 exprObj2.addTerm(yc2,y[i][j][p]);
                        	 }
                         }
                		 else{ y[i][j][p] = cplex.intVar(0, 0);//,"y"+String.valueOf(y11));y11+=1;
                		 variable_count+=1; 
                		 exprObj2.addTerm(yc2,y[i][j][p]);
                		 }}}}
         IloLinearNumExpr exprObj3 = cplex.linearNumExpr();
         for (int i = 0; i < aT; i++) {
            	 for(int p=0;p<t_count;p++){
                     s[i][p] = cplex.intVar(0, 1, "s" + candidate[i].Index + "," + p);
                     variable_count+=1; 
                     exprObj3.addTerm(0,s[i][p]);
                     }}
         IloLinearNumExpr exprObj4 = cplex.linearNumExpr();
         for (int i = 0; i < aT; i++) {
            	 for(int p=0;p<t_count;p++){
                     e[i][p] = cplex.intVar(0, 1, "e" + candidate[i].Index + "," + p);
                     variable_count+=1; 
                     exprObj4.addTerm(0,e[i][p]);
                     }}
         
         IloLinearNumExpr exprObj5 = cplex.linearNumExpr();
         for (int i = 0; i < 3; i++) {
                     z[i] = cplex.intVar(0, 1, "z" +i);
                     variable_count+=1; 
                     exprObj5.addTerm(0,z[i]);
                     }
         /*IloLinearNumExpr exprObj0 = cplex.linearNumExpr();
         IloIntVar[] f = new IloIntVar[1];
         f[0] = cplex.intVar(0, 1, "f0");
         exprObj0.addTerm(0,f[0]);
         cplex.addMaximize(exprObj0);*/

         cplex.addMaximize(cplex.sum(exprObj1,exprObj2,exprObj3,exprObj4,exprObj5));
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
	    		 //constraint15.addTerm((int) (-1*candidate[i].EndTime), y[i][j][p]);
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
	    
	    //fix 
	    IloLinearIntExpr constraint16 = cplex.linearIntExpr();
	    int i=whichi;
	    for(int p=0;p<t_count;p++){
	    	constraint16.addTerm(1, s[i][p]);
	    	constraint16.addTerm(1, e[i][p]);
	    	 for(int j3=0;j3<nXlists.get(candidate[i].Index).size();j3++){constraint16.addTerm(1, x[i][j3][p]);}
	    	 for(int j4=0;j4<aT;j4++){
	    		 if(nXlists.get(candidate[j4].Index).contains(candidate[i].Index)){
	    				 constraint16.addTerm(1, x[j4][nXlists.get(candidate[j4].Index).indexOf(candidate[i].Index)][p]);}}
	    	 
	    	 for(int j1=0;j1<nYlists.get(candidate[i].Index).size();j1++){if(p<t_count-1){constraint16.addTerm(1, y[i][j1][p]);}}
	    	 for(int j2=0;j2<aT;j2++){
	    		 if(nYlists.get(candidate[j2].Index).contains(candidate[i].Index) && p<t_count-1){
	    			 constraint16.addTerm(1, y[j2][nYlists.get(candidate[j2].Index).indexOf(candidate[i].Index)][p]);}
	    		 }
	    }
	    cplex.addGe(constraint16,1);
	    
	    
	   // cplex.setParam(IloCplex.Param.TimeLimit,360);
	    System.out.println("---");
	    st_count+=1;
	    System.out.printf("%s %d","Total variables:",variable_count);
	    System.out.printf("%s %d","Total constraints:",st_count);
        System.out.println();
	    if(expo){
	    cplex.exportModel("/Users/jessicanini/Desktop/AA/lpfiles/fix_i"+String.valueOf(i)+".lp");}
	    String filePath1="/Users/jessicanini/Desktop/AA/log1/log_"+String.valueOf(i)+".txt";
	    FileWriter fw1 = new FileWriter(filePath1);BufferedWriter bw1 = new BufferedWriter(fw1);
	    String filePath2="/Users/jessicanini/Desktop/AA/log2/log_"+String.valueOf(i)+".txt";
	    FileWriter fw2 = new FileWriter(filePath2);BufferedWriter bw2 = new BufferedWriter(fw2);
	    String filePath3="/Users/jessicanini/Desktop/AA/res1/res_"+String.valueOf(i)+".txt";
	    FileWriter fw3 = new FileWriter(filePath3);BufferedWriter bw3 = new BufferedWriter(fw3);
	    
	    ArrayList<HashSet<Integer>> tmp=new ArrayList<HashSet<Integer>>();
	    for(int t=0;t<t_count;t++){tmp.add(new HashSet<Integer>());}
	    boolean flag=false;
	   // assert false;
	    try {
		      cplex.solve();
		      double obj=cplex.getObjValue();
		      //System.out.println("The optimal objValue is "+String.valueOf(obj));
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
		    sol1.add(e2.Index);A.add(e2.Index);
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
	    	System.err.printf("%s %d","执勤期:",whichi);
	    }
	    otime=otime/3600.0;
	    bw1.write("总执勤期耗时: "+String.format("%.2f",otime));bw1.write("\r");
	    
	    bw1.flush();bw1.close();fw1.close();
	    bw2.flush();bw2.close();fw2.close();
	    bw3.flush();bw3.close();fw3.close();}//for flag
	    
	    cplex.clearModel();

    }


    public static void main(String[] args) throws IOException, ParseException, IloException{
    	//int aT=1518;int rest_num=1420;Scanner in1 =new Scanner(Paths.get("/Users/jessicanini/Desktop/AA/data1.txt"));
    	int aT=1987;int rest_num=1420;Scanner in1 =new Scanner(Paths.get("/Users/jessicanini/Desktop/AA/data2.txt"));
   	
    	ArrayList<Integer> N_slocs=new ArrayList<Integer>();ArrayList<Integer> N_elocs=new ArrayList<Integer>();
    	ArrayList<Integer> N_blocs=new ArrayList<Integer>();ArrayList<Integer> blocs=new ArrayList<Integer>();
    	
    	Set<String> citysets=new HashSet<String>();
    	ArrayList<String> citys=new ArrayList<String>();
    	//String[] citys1=new String[]{"HGH","URC","CGQ","PEK","ENY","WNZ","XIY","KMG","CTU","CAN","CSX","HRB","WNZ","SZX"};
    	String[] citys1=new String[]{"NZL","RIZ","CTU","TNA","NNG","BFJ","LJG","XIY","HJJ","KLO","UTP","SHE","MDL","XNN","NBS","CIF","XFN","KMG","WDS","SQD","HGH","NGB","MIG","WUH","NKG","UYN","AVA","CAN","BPE","WUT","DLC","WEH","ENH","INC","DTU","HLH","URC",
    			"JHG","KJH","HDG","CJU","SZX","ENY","DYG","YNJ","MFM","LYA","KWE","LHW","LYG","LYI","YNT","CKG","KWL","HIA","WNZ","TYN",
    			"CSX","CGO","AKU","ZYI","CGQ","ZUH","HUZ","KOS","KOW","JIQ","HAK","HZG","HAN","ACX","HET","DAT","BAV","PEK","HRB","SGN","XUZ","CXR","TEN","NZH"};
    			//{"HGH","SZX","XIY","URC","PEK","NGB","CGQ","KMG","ENY","WNZ","CTU","CNA","HRB","CSX"};
    	//"HGH","URC","CGQ","PEK","ENY","WNZ","XIY","KMG","CTU","CAN","CSX","HRB","WNZ","SZX"

    	for(String e:citys1){citys.add(e);}
    	
    	Flight[] flights=new Flight[aT];
    	for(int i=0;i<aT;i++){
    		String[] temp=in1.nextLine().split(",");
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
    	//System.out.println(flights[1517].EndTime);System.out.println(flights[1517].End);
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
    	//int xc=4;int yc1=3;int yc2=1;
    	int xc=0;int yc1=0;int yc2=0;
    	//boolean expo=true;
    	boolean expo=false;
    	int count=0;

    	
    	int b=273;//1202 1203
    	int[] p=new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396, 397, 398, 399, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 444, 445, 446, 447, 448, 449, 450, 451, 452, 453, 454, 455, 456, 457, 458, 459, 460, 461, 462, 463, 464, 465, 466, 467, 468, 469, 470, 471, 472, 473, 474, 475, 476, 477, 478, 479, 480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 497, 498, 499, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512, 513, 514, 515, 516, 517, 518, 519, 520, 521, 522, 523, 524, 525, 526, 527, 528, 529, 530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547, 548, 549, 550, 551, 552, 553, 554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 567, 568, 569, 570, 571, 572, 573, 574, 575, 576, 577, 578, 579, 580, 581, 582, 583, 584, 585, 586, 587, 588, 589, 590, 591, 592, 593, 594, 595, 596, 597, 598, 599, 600, 601, 602, 603, 604, 605, 606, 607, 608, 609, 610, 611, 612, 613, 614, 615, 616, 617, 618, 619, 620, 621, 622, 623, 624, 625, 626, 627, 628, 629, 630, 631, 632, 633, 634, 635, 636, 637, 638, 639, 640, 641, 642, 643, 644, 645, 646, 647, 648, 649, 650, 651, 652, 653, 654, 655, 656, 657, 658, 659, 660, 661, 662, 663, 664, 665, 666, 667, 668, 669, 670, 671, 672, 673, 674, 675, 676, 677, 678, 679, 680, 681, 682, 683, 684, 685, 686, 687, 688, 689, 690, 691, 692, 693, 694, 695, 696, 697, 698, 699, 700, 701, 702, 703, 704, 705, 706, 707, 708, 709, 710, 711, 712, 713, 714, 715, 716, 717, 718, 719, 720, 721, 722, 723, 724, 725, 726, 727, 728, 729, 730, 731, 732, 733, 734, 735, 736, 737, 738, 739, 740, 741, 742, 743, 744, 745, 746, 747, 748, 749, 750, 751, 752, 753, 754, 755, 756, 757, 758, 759, 760, 761, 762, 763, 764, 765, 766, 767, 768, 769, 770, 771, 772, 773, 774, 775, 776, 777, 778, 779, 780, 781, 782, 783, 784, 785, 786, 787, 788, 789, 790, 791, 792, 793, 794, 795, 796, 797, 798, 799, 800, 801, 802, 803, 804, 805, 806, 807, 808, 809, 810, 811, 812, 813, 814, 815, 816, 817, 818, 819, 820, 821, 822, 823, 824, 825, 826, 827, 828, 829, 830, 831, 832, 833, 834, 835, 836, 837, 838, 839, 840, 841, 842, 843, 844, 845, 846, 847, 848, 849, 850, 851, 852, 853, 854, 855, 856, 857, 858, 859, 860, 861, 862, 863, 864, 865, 866, 867, 868, 869, 870, 871, 872, 873, 874, 875, 876, 877, 878, 879, 880, 881, 882, 883, 884, 885, 886, 887, 888, 889, 890, 891, 892, 893, 894, 895, 896, 897, 898, 899, 900, 901, 902, 903, 904, 905, 906, 907, 908, 909, 910, 911, 912, 913, 914, 915, 916, 917, 918, 919, 920, 921, 922, 923, 924, 925, 926, 927, 928, 929, 930, 931, 932, 933, 934, 935, 936, 937, 938, 939, 940, 941, 942, 943, 944, 945, 946, 947, 948, 949, 950, 951, 952, 953, 954, 955, 956, 957, 958, 959, 960, 961, 962, 963, 964, 965, 966, 967, 968, 969, 970, 971, 972, 973, 974, 975, 976, 977, 978, 979, 980, 981, 982, 983, 984, 985, 986, 987, 988, 989, 990, 991, 992, 993, 994, 995, 996, 997, 998, 999, 1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035, 1036, 1037, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1060, 1061, 1062, 1063, 1064, 1065, 1066, 1067, 1068, 1069, 1070, 1071, 1072, 1073, 1074, 1075, 1076, 1077, 1078, 1079, 1080, 1081, 1082, 1083, 1084, 1085, 1086, 1087, 1088, 1089, 1090, 1091, 1092, 1093, 1094, 1095, 1096, 1097, 1098, 1099, 1100, 1101, 1102, 1103, 1104, 1105, 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1113, 1114, 1115, 1116, 1117, 1118, 1119, 1120, 1121, 1122, 1123, 1124, 1125, 1126, 1127, 1128, 1129, 1130, 1131, 1132, 1133, 1134, 1135, 1136, 1137, 1138, 1139, 1140, 1141, 1142, 1143, 1144, 1145, 1146, 1147, 1148, 1149, 1150, 1151, 1152, 1153, 1154, 1155, 1156, 1157, 1158, 1159, 1160, 1161, 1162, 1163, 1164, 1165, 1166, 1167, 1168, 1169, 1170, 1171, 1172, 1173, 1174, 1175, 1176, 1177, 1178, 1179, 1180, 1181, 1182, 1183, 1184, 1185, 1186, 1187, 1188, 1189, 1190, 1191, 1192, 1193, 1194, 1195, 1196, 1197, 1198, 1199, 1200, 1201, 1203, 1204, 1205, 1206, 1207, 1208, 1209, 1210, 1211, 1212, 1213, 1214, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 1222, 1223, 1224, 1225, 1226, 1227, 1228, 1229, 1230, 1231, 1232, 1233, 1234, 1235, 1236, 1237, 1238, 1239, 1240, 1241, 1242, 1243, 1244, 1245, 1246, 1247, 1248, 1249, 1250, 1251, 1252, 1253, 1254, 1255, 1256, 1257, 1258, 1259, 1260, 1261, 1262, 1263, 1264, 1265, 1266, 1267, 1268, 1269, 1270, 1271, 1272, 1273, 1274, 1275, 1276, 1277, 1278, 1279, 1280, 1281, 1282, 1283, 1284, 1285, 1286, 1287, 1288, 1289, 1290, 1291, 1292, 1293, 1294, 1295, 1296, 1297, 1298, 1299, 1300, 1301, 1302, 1304, 1305, 1307, 1308, 1309, 1310, 1311, 1312, 1313, 1314, 1315, 1316, 1317, 1318, 1319, 1320, 1321, 1322, 1323, 1324, 1325, 1326, 1327, 1328, 1329, 1330, 1331, 1332, 1333, 1334, 1335, 1336, 1337, 1338, 1339, 1340, 1341, 1342, 1343, 1344, 1345, 1346, 1347, 1348, 1349, 1350, 1351, 1352, 1353, 1354, 1355, 1357, 1362, 1363, 1368, 1370, 1371, 1374, 1375, 1377, 1378, 1382, 1383, 1385, 1386, 1387, 1388, 1389, 1390, 1392, 1393, 1394, 1395, 1396, 1397, 1399, 1400, 1401, 1402, 1403, 1404, 1406, 1407, 1408, 1409, 1410, 1411, 1412, 1413, 1414, 1415, 1416, 1417, 1418, 1419, 1421, 1422, 1423, 1424, 1425, 1426, 1432, 1433, 1436, 1437, 1438, 1445, 1446, 1450, 1451, 1452, 1453, 1454, 1465, 1466, 1467, 1468, 1471, 1472, 1475, 1477, 1479, 1480, 1481, 1482, 1486, 1487, 1488, 1489, 1506, 1508, 1509, 1510, 1514, 1515, 1516, 1517, 1536, 1561, 1573, 1605, 1612, 1621, 1622, 1632, 1633, 1641, 1698, 1713, 1751, 1754, 1766, 1779, 1783, 1830, 1842, 1846, 1860, 1866, 1876, 1948, 1962, 1963, 1979};
    			//{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396, 397, 398, 399, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 444, 445, 446, 447, 448, 449, 450, 451, 452, 453, 454, 455, 456, 457, 458, 459, 460, 461, 462, 463, 464, 465, 466, 467, 468, 469, 470, 471, 472, 473, 474, 475, 476, 477, 478, 479, 480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 497, 498, 499, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512, 513, 514, 515, 516, 517, 518, 519, 520, 521, 522, 523, 524, 525, 526, 527, 528, 529, 530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547, 548, 549, 550, 551, 552, 553, 554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 567, 568, 569, 570, 571, 572, 573, 574, 575, 576, 577, 578, 579, 580, 581, 582, 583, 584, 585, 586, 587, 588, 589, 590, 591, 592, 593, 594, 595, 596, 597, 598, 599, 600, 601, 602, 603, 604, 605, 606, 607, 608, 609, 610, 611, 612, 613, 614, 615, 616, 617, 618, 619, 620, 621, 622, 623, 624, 625, 626, 627, 628, 629, 630, 631, 632, 633, 634, 635, 636, 637, 638, 639, 640, 641, 642, 643, 644, 645, 646, 647, 648, 649, 650, 651, 652, 653, 654, 655, 656, 657, 658, 659, 660, 661, 662, 663, 664, 665, 666, 667, 668, 669, 670, 671, 672, 673, 674, 675, 676, 677, 678, 679, 680, 681, 682, 683, 684, 685, 686, 687, 688, 689, 690, 691, 692, 693, 694, 695, 696, 697, 698, 699, 700, 701, 702, 703, 704, 705, 706, 707, 708, 709, 710, 711, 712, 713, 714, 715, 716, 717, 718, 719, 720, 721, 722, 723, 724, 725, 726, 727, 728, 729, 730, 731, 732, 733, 734, 735, 736, 737, 738, 739, 740, 741, 742, 743, 744, 745, 746, 747, 748, 749, 750, 751, 752, 753, 754, 755, 756, 757, 758, 759, 760, 761, 762, 763, 764, 765, 766, 767, 768, 769, 770, 771, 772, 773, 774, 775, 776, 777, 778, 779, 780, 781, 782, 783, 784, 785, 786, 787, 788, 789, 790, 791, 792, 793, 794, 795, 796, 797, 798, 799, 800, 801, 802, 803, 804, 805, 806, 807, 808, 809, 810, 811, 812, 813, 814, 815, 816, 817, 818, 819, 820, 821, 822, 823, 824, 825, 826, 827, 828, 829, 830, 831, 832, 833, 834, 835, 836, 837, 838, 839, 840, 841, 842, 843, 844, 845, 846, 847, 848, 849, 850, 851, 852, 853, 854, 855, 856, 857, 858, 859, 860, 861, 862, 863, 864, 865, 866, 867, 868, 869, 870, 871, 872, 873, 874, 875, 876, 877, 878, 879, 880, 881, 882, 883, 884, 885, 886, 887, 888, 889, 890, 891, 892, 893, 894, 895, 896, 897, 898, 899, 900, 901, 902, 903, 904, 905, 906, 907, 908, 909, 910, 911, 912, 913, 914, 915, 916, 917, 918, 919, 920, 921, 922, 923, 924, 925, 926, 927, 928, 929, 930, 931, 932, 933, 934, 935, 936, 937, 938, 939, 940, 941, 942, 943, 944, 945, 946, 947, 948, 949, 950, 951, 952, 953, 954, 955, 956, 957, 958, 959, 960, 961, 962, 963, 964, 965, 966, 967, 968, 969, 970, 971, 972, 973, 974, 975, 976, 977, 978, 979, 980, 981, 982, 983, 984, 985, 986, 987, 988, 989, 990, 991, 992, 993, 994, 995, 996, 997, 998, 999, 1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035, 1036, 1037, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1060, 1061, 1062, 1063, 1064, 1065, 1066, 1067, 1068, 1069, 1070, 1071, 1072, 1073, 1074, 1075, 1076, 1077, 1078, 1079, 1080, 1081, 1082, 1083, 1084, 1085, 1086, 1087, 1088, 1089, 1090, 1091, 1092, 1093, 1094, 1095, 1096, 1097, 1098, 1099, 1100, 1101, 1102, 1103, 1104, 1105, 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1113, 1114, 1115, 1117, 1119, 1120, 1121, 1123, 1126, 1128, 1129, 1138, 1139, 1145, 1146, 1151, 1152, 1153, 1158, 1159, 1171, 1172, 1173, 1177, 1178, 1182, 1184, 1189, 1190, 1191, 1194, 1196, 1197, 1198, 1203, 1204, 1205, 1206, 1207, 1208, 1209, 1210, 1211, 1212, 1213, 1214, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 1222, 1223, 1224, 1225, 1226, 1227, 1228, 1229, 1230, 1231, 1233, 1234, 1235, 1236, 1237, 1238, 1240, 1241, 1243, 1245, 1246, 1247, 1248, 1249, 1250, 1251, 1254, 1255, 1256, 1257, 1258, 1261, 1262, 1263, 1264, 1265, 1266, 1267, 1268, 1269, 1270, 1271, 1272, 1273, 1274, 1275, 1276, 1277, 1278, 1279, 1280, 1284, 1285, 1289, 1291, 1293, 1295, 1296, 1297, 1299, 1304, 1305, 1307, 1308, 1309, 1310, 1311, 1312, 1313, 1314, 1315, 1316, 1317, 1318, 1319, 1320, 1321, 1322, 1323, 1324, 1325, 1326, 1327, 1328, 1329, 1330, 1331, 1332, 1333, 1334, 1335, 1336, 1337, 1338, 1339, 1340, 1341, 1342, 1343, 1344, 1345, 1346, 1347, 1348, 1349, 1350, 1351, 1352, 1353, 1354, 1355, 1357, 1362, 1363, 1368, 1370, 1371, 1374, 1375, 1377, 1378, 1382, 1383, 1385, 1386, 1387, 1388, 1389, 1390, 1392, 1393, 1394, 1395, 1396, 1397, 1399, 1400, 1401, 1402, 1403, 1404, 1406, 1407, 1408, 1409, 1410, 1411, 1412, 1413, 1414, 1415, 1416, 1417, 1418, 1419, 1421, 1422, 1423, 1424, 1425, 1426, 1432, 1433, 1436, 1437, 1438, 1445, 1446, 1450, 1451, 1452, 1453, 1454, 1465, 1466, 1467, 1468, 1471, 1472, 1475, 1477, 1479, 1480, 1481, 1482, 1486, 1487, 1488, 1489, 1506, 1508, 1509, 1510, 1514, 1515, 1516, 1517, 1536, 1561, 1573, 1605, 1621, 1622, 1632, 1633, 1641, 1698, 1713, 1751, 1754, 1766, 1779, 1783, 1830, 1842, 1846, 1860, 1876, 1948, 1962, 1963};
    			//{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396, 397, 398, 399, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 444, 445, 446, 447, 448, 449, 450, 451, 452, 453, 454, 455, 456, 457, 458, 459, 460, 461, 462, 463, 464, 465, 466, 467, 468, 469, 470, 471, 472, 473, 474, 475, 476, 477, 478, 479, 480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 497, 498, 499, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512, 513, 514, 515, 516, 517, 518, 519, 520, 521, 522, 523, 524, 525, 526, 527, 528, 529, 530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547, 548, 549, 550, 551, 552, 553, 554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 567, 568, 569, 570, 571, 572, 573, 574, 575, 576, 577, 578, 579, 580, 581, 582, 583, 584, 585, 586, 587, 588, 589, 590, 591, 592, 593, 594, 595, 596, 597, 598, 599, 600, 601, 602, 603, 604, 605, 606, 607, 608, 609, 610, 611, 612, 613, 614, 615, 616, 617, 618, 619, 620, 621, 622, 623, 624, 625, 626, 627, 628, 629, 630, 631, 632, 633, 634, 635, 636, 637, 638, 639, 640, 641, 642, 643, 644, 645, 646, 647, 648, 649, 650, 651, 652, 653, 654, 655, 656, 657, 658, 659, 660, 661, 662, 663, 664, 665, 666, 667, 668, 669, 670, 671, 672, 673, 674, 675, 676, 677, 678, 679, 680, 681, 682, 683, 684, 685, 686, 687, 688, 689, 690, 691, 692, 693, 694, 695, 696, 697, 698, 699, 700, 701, 702, 703, 704, 705, 706, 707, 708, 709, 710, 711, 712, 713, 714, 715, 716, 717, 718, 719, 720, 721, 722, 723, 724, 725, 726, 727, 728, 729, 730, 731, 732, 733, 734, 735, 736, 737, 738, 739, 740, 741, 742, 743, 744, 745, 746, 747, 748, 749, 750, 751, 752, 753, 754, 755, 756, 757, 758, 759, 760, 761, 762, 763, 764, 765, 766, 767, 768, 769, 770, 771, 772, 773, 774, 775, 776, 777, 778, 779, 780, 781, 782, 783, 784, 785, 786, 787, 788, 789, 790, 791, 792, 793, 794, 795, 796, 797, 798, 799, 800, 801, 802, 803, 804, 805, 806, 807, 808, 809, 810, 811, 812, 813, 814, 815, 816, 817, 818, 819, 820, 821, 822, 823, 824, 825, 826, 827, 828, 829, 830, 831, 832, 833, 834, 835, 836, 837, 838, 839, 840, 841, 842, 843, 844, 845, 846, 847, 848, 849, 850, 851, 852, 853, 854, 855, 856, 857, 858, 859, 860, 861, 862, 863, 864, 865, 866, 867, 868, 869, 870, 871, 872, 873, 874, 875, 876, 877, 878, 879, 880, 881, 882, 883, 884, 885, 886, 887, 888, 889, 890, 891, 892, 893, 894, 895, 896, 897, 898, 899, 900, 901, 902, 903, 904, 905, 906, 907, 908, 909, 910, 911, 912, 913, 914, 915, 916, 917, 918, 919, 920, 921, 922, 923, 924, 925, 926, 927, 928, 929, 930, 931, 932, 933, 934, 935, 936, 937, 938, 939, 940, 941, 942, 943, 944, 945, 946, 947, 948, 949, 950, 951, 952, 953, 954, 955, 956, 957, 958, 959, 960, 961, 962, 963, 964, 965, 966, 967, 968, 969, 970, 971, 972, 973, 974, 975, 976, 977, 978, 979, 980, 981, 982, 983, 984, 985, 986, 987, 988, 989, 990, 991, 992, 993, 994, 995, 996, 997, 998, 999, 1000, 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009, 1010, 1011, 1012, 1013, 1014, 1015, 1016, 1017, 1018, 1019, 1020, 1021, 1022, 1023, 1024, 1025, 1026, 1027, 1028, 1029, 1030, 1031, 1032, 1033, 1034, 1035, 1036, 1037, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059, 1060, 1061, 1062, 1063, 1064, 1065, 1066, 1067, 1068, 1069, 1070, 1071, 1072, 1073, 1074, 1075, 1076, 1077, 1078, 1079, 1080, 1081, 1082, 1083, 1084, 1085, 1086, 1087, 1091, 1092, 1094, 1097, 1099, 1100, 1102, 1106, 1108, 1112, 1114, 1117, 1119, 1120, 1121, 1123, 1126, 1128, 1129, 1138, 1145, 1152, 1159, 1171, 1172, 1173, 1177, 1178, 1182, 1184, 1190, 1194, 1197, 1203, 1204, 1205, 1206, 1207, 1208, 1209, 1210, 1211, 1212, 1213, 1214, 1215, 1216, 1217, 1218, 1219, 1220, 1221, 1222, 1223, 1224, 1225, 1226, 1227, 1228, 1229, 1230, 1231, 1233, 1234, 1235, 1236, 1237, 1238, 1240, 1241, 1243, 1245, 1246, 1247, 1248, 1249, 1250, 1251, 1254, 1255, 1256, 1257, 1258, 1261, 1262, 1263, 1264, 1265, 1266, 1267, 1268, 1269, 1270, 1271, 1272, 1273, 1274, 1275, 1276, 1277, 1278, 1279, 1280, 1284, 1285, 1289, 1291, 1293, 1295, 1296, 1297, 1299, 1304, 1305, 1307, 1308, 1309, 1310, 1311, 1312, 1313, 1314, 1315, 1316, 1317, 1318, 1319, 1320, 1321, 1322, 1323, 1324, 1325, 1326, 1327, 1328, 1329, 1330, 1331, 1332, 1333, 1334, 1335, 1336, 1337, 1338, 1339, 1340, 1341, 1342, 1343, 1344, 1345, 1346, 1347, 1348, 1349, 1350, 1351, 1352, 1353, 1354, 1355, 1357, 1362, 1363, 1368, 1370, 1371, 1374, 1375, 1377, 1378, 1382, 1383, 1385, 1386, 1387, 1388, 1389, 1390, 1392, 1393, 1394, 1395, 1396, 1397, 1399, 1400, 1401, 1402, 1403, 1404, 1406, 1407, 1408, 1409, 1410, 1411, 1412, 1413, 1414, 1415, 1416, 1417, 1418, 1419, 1421, 1422, 1423, 1424, 1425, 1426, 1432, 1433, 1436, 1437, 1438, 1445, 1446, 1450, 1451, 1452, 1453, 1454, 1465, 1466, 1467, 1468, 1471, 1472, 1475, 1479, 1480, 1481, 1482, 1486, 1487, 1488, 1489, 1506, 1508, 1509, 1510, 1514, 1515, 1516, 1517, 1536, 1561, 1573, 1605, 1621, 1622, 1632, 1633, 1641, 1698, 1713, 1751, 1754, 1766, 1779, 1783, 1830, 1846, 1860, 1876, 1948, 1962, 1963};
    	expo=true;
    	ArrayList<Integer> processed=new ArrayList<Integer>();
    	for(int pp=0;pp<p.length;pp++){
    		processed.add(p[pp]);
    		A.add(p[pp]);
    	}
    	for(;b<274;b++){
    	//for(;b<rest_num;b++){
    		System.err.printf("%s %d","Current b",b);System.err.println(" ");
    		count+=1;
    		//IloCplex cplex1= new IloCplex();
    		if(!A.contains(b)){
    			//expo=true;
    			System.err.printf("%s %d","NOT Processed",b);
    	    build_model(expo,xc,yc1,yc2,b,candidate,rest_num,sum1,sum2,nXlists,nYlists,S_locs,E_locs,Flight_time,On_time,N_slocs,
    			N_elocs,N_blocs,blocs);
    	    }
    		else{System.err.printf("%s %d","FINISH Processed",b);
    		System.err.println();}
    		//System.err.printf("%s %d","Current A.length",A.size());System.err.println(" ");
    		}
    	System.err.println(b);
    	
    	/*for(int i=0;i<aT;i++){
		for(int j=0;j<aT;j++){
			if(Xlists[i][j]==1){System.out.printf("%s,%d,%d","X",i,j);}
			if(Ylists[i][j]==1){System.out.printf("%s,%d,%d","Y",i,j);}
		}}	*/
    	
}
}