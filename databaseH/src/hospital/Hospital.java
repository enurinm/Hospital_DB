package hospital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Hospital {
	static Scanner scan=new Scanner(System.in);
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("숫자를 입력해 주세요."+'\n'+"1:로그인"+'\n'+"2:등록");
		
		int x=scan.nextInt();
		
		switch(x){
			case 1:
				logIn();
				break;
			case 2:
				supplement();
				break;
			default:
				System.out.println("틀린 숫자");
				break;
				
		}
	}
	
	public static void dbquery(String q) throws SQLException { //인서트, 계정 등록, nurse의 일정 등록	
		String dbUrl="jdbc:mysql://117.16.137.108:3306/user_201713074?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
		String dbId="user_201713074";
		String dbPw="201713074";		
		Connection con=null;
		Statement smt=null;
		PreparedStatement psmt=null;		
		{
			try {
				con=DriverManager.getConnection(dbUrl,dbId,dbPw);				
				smt=con.createStatement();
				
				String sql=q;				
				smt.executeUpdate(sql);
				
			}
			catch(SQLException e){
				System.out.println("SQLException handled");
				System.out.println(e.getMessage());
			}
			finally {
				if(smt!=null) {
					smt.close();
				}
			}
		}
	}

	private static void supplement() throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("회원정보를 등록합니다.");
		
		System.out.println("의사는 1, 간호사는 2, 환자는 3을 입력해 주세요.");
		int dnp=scan.nextInt();		
		String q=null;
		String id=null;
		String dept=null;
		
		if(dnp==1) {
			System.out.println("의사 등록");
			System.out.println("ID를 입력해 주세요.(의사 d, 간호사 n, 환자 p로 시작하는 최대 4자리 정수)");
			id=scan.next();
			System.out.println("부서를 입력해 주세요.('Surgery' ,  'Inter.Medi.' , 'Dentist' , 'Pediatric' , 'E.N.T.' 중 한가지)");
			dept=scan.next();
			q="insert into hospital_doctor values('"+id+"','"+dept+"');";
			dbquery(q);			
		}
		else if(dnp==2) {
			System.out.println("간호사 등록");
			System.out.println("ID를 입력해 주세요.(의사 d, 간호사 n, 환자 p로 시작하는 최대 4자리 정수)");
			id=scan.next();
			System.out.println("부서를 입력해 주세요.('Surgery' ,  'Inter.Medi.' , 'Dentist' , 'Pediatric' , 'E.N.T.' 중 한가지)");
			dept=scan.next();
			q="insert into hospital_nurse values('"+id+"','"+dept+"');";
			dbquery(q);
		}
		else if(dnp==3){
			System.out.println("환자 등록");
			System.out.println("의사 등록");
			System.out.println("ID를 입력해 주세요.(의사 d, 간호사 n, 환자 p로 시작하는 최대 4자리 정수)");
			id=scan.next();
			q="insert into hospital_patient values('"+id+"',0);";
			dbquery(q);
		}
		else {
			System.out.println("잘못된 입력입니다.");
		}
		
	}

	public static void logIn() throws SQLException {
		System.out.println("로그인");
		System.out.println("ID를 입력해 주세요.");
		String id=scan.next();		
		String s[]=id.split("");
		String d="d";
		String n="n";
		String p="p";
		
		if(s[0].equals(d)) {
			doctor(id);
		}
		else if(s[0].equals(n)) {
			nurse(id);
		}
		else if(s[0].equals(p)) {
			patient(id);
		}
		else {
			System.out.println("잘못된 ID입니다.");
			return;
		}
	}

	private static void patient(String id) throws SQLException {
		// TODO Auto-generated method stub		
		String dbUrl="jdbc:mysql://117.16.137.108:3306/user_201713074?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
		String dbId="user_201713074";
		String dbPw="201713074";		
		Connection con=null;
		Statement smt=null;
		PreparedStatement psmt=null;		
		{
			try {
				con=DriverManager.getConnection(dbUrl,dbId,dbPw);
				
				smt=con.createStatement();
				String sql="select bill from hospital_patient where PID='"+id+"';";
				ResultSet rs=smt.executeQuery(sql);
				
				if(rs.next()) {
					int bill =rs.getInt("bill");
					System.out.println("청구금액 : "+bill);	
				}
				
			}
			catch(SQLException e){
				System.out.println("SQLException handled");
				System.out.println(e.getMessage());
			}
			finally {
				if(smt!=null) {
					smt.close();
				}
			}
		}
		
	}

	private static void nurse(String id) throws SQLException {
		// TODO Auto-generated method stub
		String dbUrl="jdbc:mysql://117.16.137.108:3306/user_201713074?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
		String dbId="user_201713074";
		String dbPw="201713074";		
		Connection con=null;
		Statement smt=null;
		PreparedStatement psmt=null;		
		{
			try {
				con=DriverManager.getConnection(dbUrl,dbId,dbPw);
				
				smt=con.createStatement();
				
				String sql="select PID,hospital_timeslot.DID,schedule from hospital_timeslot natural join hospital_doctor "
						+"where hospital_doctor.dept_name=(select hospital_nurse.dept_name"
						+ " from hospital_nurse where NID='"+id+"');";
				ResultSet rs=smt.executeQuery(sql);
				
				System.out.println("현재 결정된 의사의 스케쥴입니다.");
				while(rs.next()) {
					System.out.println(rs.getString("PID")+" | "+rs.getString("DID")+" | "+rs.getTime("schedule"));
				}
				
				System.out.println("스케쥴을 추가할 의사의 ID를 입력해 주세요.");
				String did=scan.next();
				System.out.println("의사가 진료할 환자의 ID를 입력해 주세요.");
				String pid=scan.next();
				System.out.println("진료 시간을 입력해 주세요.(예: 10:30:00)");
				String sch=scan.next();
				
				sql="insert into hospital_timeslot values('"+pid+"','"+did+"','"+sch+"','No')";
				smt.executeUpdate(sql);
			}
			catch(SQLException e){
				System.out.println("SQLException handled");
				System.out.println(e.getMessage());
			}
			finally {
				if(smt!=null) {
					smt.close();
				}
			}
		}
	}

	private static void doctor(String id) throws SQLException {
		// TODO Auto-generated method stub
		String dbUrl="jdbc:mysql://117.16.137.108:3306/user_201713074?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
		String dbId="user_201713074";
		String dbPw="201713074";		
		Connection con=null;
		Statement smt=null;
		PreparedStatement psmt=null;		
		{
			try {
				con=DriverManager.getConnection(dbUrl,dbId,dbPw);
				
				smt=con.createStatement();
				
				String sql="select PID from hospital_timeslot where DID='"+id+"' and prescription='No';";
				ResultSet rs=smt.executeQuery(sql);
				
				System.out.println("처방을 발행하지 않은 환자의 목록입니다.");
				while(rs.next()) {
					String PID=rs.getString("PID");
					System.out.println(PID);
				}
				
				System.out.println("처방을 발행할 환자의 ID를 입력해 주세요.");
				String pid=scan.next();
				System.out.println("청구서 금액을 입력해 주세요.");
				int b=scan.nextInt();
				
				sql="update hospital_patient set bill="+b+" where PID='"+pid+"';";
				smt.executeUpdate(sql);
				sql="update hospital_timeslot set prescription='Yes' where PID='"+pid+"';";
				smt.executeUpdate(sql);
				
				
				
			}
			catch(SQLException e){
				System.out.println("SQLException handled");
				System.out.println(e.getMessage());
			}
			finally {
				if(smt!=null) {
					smt.close();
				}
			}
		}
	}

}
