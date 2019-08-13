import java.sql.Date;
import java.text.SimpleDateFormat;

//Flight = namedtuple("Flight", ['Index','Plane_index','Plane_num','Flight_serial','Flight_seq','Start','End','StartTime','EndTime','Duration']) 
public class Flight {
	int Index;
	int Plane_index;
	String Plane_serial;
	String Flight_serial;
	int Flight_seq;
	String Start;
	String End;
	long StartTime;
	long EndTime;
	double duration;
	/*
	public Flight(int Index,int Plane_index,String Plane_serial,String Flight_serial,int Flight_seq,String Start,String End,long StartTime,long EndTime){
		this.Index=Index;
		this.Plane_index=Plane_index;
		this.Plane_serial=Plane_serial;
		this.Flight_serial=Flight_serial;
		this.Flight_seq=Flight_seq;
		this.Start=Start;
		this.End=End;
		this.StartTime=StartTime;
		this.EndTime=EndTime;
	}*/
	public Flight(int rest_num,int Index,int Plane_index,String Plane_serial,String Flight_serial,int Flight_seq,String Start,String End,long StartTime,long EndTime){
		this.Index=Index;
		this.Plane_index=Plane_index;
		this.Plane_serial=Plane_serial;
		this.Flight_serial=Flight_serial;
		this.Flight_seq=Flight_seq;
		this.Start=Start;
		this.End=End;
		this.StartTime=StartTime;
		this.EndTime=EndTime;
		if(Index>=rest_num){this.duration=0;}
		else{this.duration=EndTime-StartTime;}
		
	}
	public void get_duration(){
		this.duration=interval_time(this.StartTime,this.EndTime);
	}
	//helper function
	public static double interval_time(long t1,long t2){//输出单位为秒
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        Date date1 = new Date(t1);
        Date date2 = new Date(t2);
        long res=(date2.getTime()-date1.getTime());
        return res;
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
        System.out.println(dateString);
        String min;  
        min = dateString.substring(17, 19);  
        return min;  
    }
}
