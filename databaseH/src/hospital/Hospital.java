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
		System.out.println("���ڸ� �Է��� �ּ���."+'\n'+"1:�α���"+'\n'+"2:���");
		
		int x=scan.nextInt();
		
		switch(x){
			case 1:
				logIn();
				break;
			case 2:
				supplement();
				break;
			default:
				System.out.println("Ʋ�� ����");
				break;
				
		}
	}
	
	public static void dbquery(String q) throws SQLException { //�μ�Ʈ, ���� ���, nurse�� ���� ���	
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
		System.out.println("ȸ�������� ����մϴ�.");
		
		System.out.println("�ǻ�� 1, ��ȣ��� 2, ȯ�ڴ� 3�� �Է��� �ּ���.");
		int dnp=scan.nextInt();		
		String q=null;
		String id=null;
		String dept=null;
		
		if(dnp==1) {
			System.out.println("�ǻ� ���");
			System.out.println("ID�� �Է��� �ּ���.(�ǻ� d, ��ȣ�� n, ȯ�� p�� �����ϴ� �ִ� 4�ڸ� ����)");
			id=scan.next();
			System.out.println("�μ��� �Է��� �ּ���.('Surgery' ,  'Inter.Medi.' , 'Dentist' , 'Pediatric' , 'E.N.T.' �� �Ѱ���)");
			dept=scan.next();
			q="insert into hospital_doctor values('"+id+"','"+dept+"');";
			dbquery(q);			
		}
		else if(dnp==2) {
			System.out.println("��ȣ�� ���");
			System.out.println("ID�� �Է��� �ּ���.(�ǻ� d, ��ȣ�� n, ȯ�� p�� �����ϴ� �ִ� 4�ڸ� ����)");
			id=scan.next();
			System.out.println("�μ��� �Է��� �ּ���.('Surgery' ,  'Inter.Medi.' , 'Dentist' , 'Pediatric' , 'E.N.T.' �� �Ѱ���)");
			dept=scan.next();
			q="insert into hospital_nurse values('"+id+"','"+dept+"');";
			dbquery(q);
		}
		else if(dnp==3){
			System.out.println("ȯ�� ���");
			System.out.println("�ǻ� ���");
			System.out.println("ID�� �Է��� �ּ���.(�ǻ� d, ��ȣ�� n, ȯ�� p�� �����ϴ� �ִ� 4�ڸ� ����)");
			id=scan.next();
			q="insert into hospital_patient values('"+id+"',0);";
			dbquery(q);
		}
		else {
			System.out.println("�߸��� �Է��Դϴ�.");
		}
		
	}

	public static void logIn() throws SQLException {
		System.out.println("�α���");
		System.out.println("ID�� �Է��� �ּ���.");
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
			System.out.println("�߸��� ID�Դϴ�.");
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
					System.out.println("û���ݾ� : "+bill);	
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
				
				System.out.println("���� ������ �ǻ��� �������Դϴ�.");
				while(rs.next()) {
					System.out.println(rs.getString("PID")+" | "+rs.getString("DID")+" | "+rs.getTime("schedule"));
				}
				
				System.out.println("�������� �߰��� �ǻ��� ID�� �Է��� �ּ���.");
				String did=scan.next();
				System.out.println("�ǻ簡 ������ ȯ���� ID�� �Է��� �ּ���.");
				String pid=scan.next();
				System.out.println("���� �ð��� �Է��� �ּ���.(��: 10:30:00)");
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
				
				System.out.println("ó���� �������� ���� ȯ���� ����Դϴ�.");
				while(rs.next()) {
					String PID=rs.getString("PID");
					System.out.println(PID);
				}
				
				System.out.println("ó���� ������ ȯ���� ID�� �Է��� �ּ���.");
				String pid=scan.next();
				System.out.println("û���� �ݾ��� �Է��� �ּ���.");
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
