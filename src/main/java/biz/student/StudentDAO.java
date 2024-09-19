package biz.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biz.common.JDBCUtil;

public class StudentDAO{
	//JDBC 관련 변수
		private Connection conn;
		private PreparedStatement stmt;
		private ResultSet rs;
		
	//SQL 명령어
	private static String STUDENT_LIST = "select * from student order by name desc";
	private static String STUDENT_INSERT = "insert into student(name,studentid,department,phonenum) values(?,?,?,?);";
	private static String STUDENT_GET = "select *from student where studentid = ?";
	private static String STUDENT_UPDATE = "update student set department = ?, phonenum = ? where studentid = ?";
	private static String STUDENT_COUNT = "select count(*) from student where studentid = ?";
	//전체학생 출력
	public List<StudentVO> getStudentList(StudentVO vo){
		
		List<StudentVO> studentList = new ArrayList<StudentVO>();
		
		try {
			conn = JDBCUtil.getConnection();
			stmt = conn.prepareStatement(STUDENT_LIST);
			
			rs = stmt.executeQuery();
			while(rs.next()) {
				StudentVO student = new StudentVO();
				student.setName(rs.getString("NAME"));
				student.setStudentid(rs.getInt("STUDENTID"));
				student.setDepartment(rs.getString("DEPARTMENT"));
				student.setPhonenum(rs.getString("PHONENUM"));
				
				studentList.add(student);
			} 	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtil.close(rs, stmt, conn);
		}
		return studentList;
		
	}
	//학생 등록
	public void insertStudent(StudentVO vo) {
		// TODO Auto-generated method stub
		
		try {
			conn = JDBCUtil.getConnection();
			stmt = conn.prepareStatement(STUDENT_INSERT);
			
			stmt.setString(1, vo.getName());
			stmt.setInt(2, vo.getStudentid());
			stmt.setString(3, vo.getDepartment());
			stmt.setString(4, vo.getPhonenum());
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtil.close(stmt, conn);
		}
	}
	// 학생 상세 조회
	public StudentVO getStudent(StudentVO vo) {	
		StudentVO student = null;
		
		try {
			conn = JDBCUtil.getConnection();
			stmt = conn.prepareStatement(STUDENT_GET);
			stmt.setInt(1, vo.getStudentid());
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				student = new StudentVO();
				student.setName(rs.getString("NAME"));
				student.setStudentid(rs.getInt("STUDENTID"));
				student.setDepartment(rs.getString("DEPARTMENT"));
				student.setPhonenum(rs.getString("PHONENUM"));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JDBCUtil.close(rs, stmt, conn);
		}	
		return student;
	}
	// 학생 정보 수정
	public void updateStudent(StudentVO vo) {
		try {
			conn = JDBCUtil.getConnection();
			stmt = conn.prepareStatement(STUDENT_UPDATE);
			
			stmt.setString(1,vo.getDepartment());
			stmt.setString(2, vo.getPhonenum());
			stmt.setInt(3,vo.getStudentid());
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			JDBCUtil.close(stmt, conn);
		}
		
		
	}
	public boolean duplicateStudent(StudentVO vo) {
		// TODO Auto-generated method stub
		boolean exists = false;
		try {
			conn = JDBCUtil.getConnection();
			stmt = conn.prepareStatement(STUDENT_COUNT);
			stmt.setInt(1, vo.getStudentid());
			rs = stmt.executeQuery();
			if(rs.next()) {
				//결과가 0보다 크면 exists를 true로 설정(chat-gpt도움을 받음)
				exists = rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return exists;
	}





}