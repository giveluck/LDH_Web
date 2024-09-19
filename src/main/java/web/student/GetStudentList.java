package web.student;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import biz.student.StudentVO;
import biz.student.StudentDAO;

/**
 * Servlet implementation class GetStudentList
 */
@WebServlet("/getStudentList.do")
public class GetStudentList extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // DB 연동 처리
        StudentVO vo = new StudentVO();
        StudentDAO studentDAO = new StudentDAO();
        List<StudentVO> studentList = studentDAO.getStudentList(vo);

        //응답 화면 구성
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>학생 목록</title>");
        out.println("<link rel='stylesheet' href='css/getliststyle.css'>");
        out.println("</head>");
        out.println("<body>");

        // 네비게이션 메뉴 추가 (왼쪽에 세로로 배치)
        out.println("<div class='nav'>");
        out.println("<ul>");
        out.println("<li><a href='index.html' style='font-size:20px;'>홈 화면</a></li>");
        out.println("<li><a href='/insertStudent.do'>학생 등록</a></li>");
        out.println("<li><a href='/getStudentList.do'>학생 전체</a></li>");
        out.println("<li><a href='/getStudent.do'>학생 조회</a></li>");
        out.println("<li><a href='/updateStudent.do'>학생 수정</a></li>");
        out.println("</ul>");
        out.println("</div>");

        // 학생 목록 테이블
        out.println("<div class='content'>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th width='100'>이름</th>");
        out.println("<th width='200'>학번</th>");
        out.println("<th width='150'>학부</th>");
        out.println("<th width='150'>전화번호</th>");
        out.println("</tr>");
        for (StudentVO student : studentList) {
            out.println("<tr>");
            out.println("<td>" + student.getName() + "</td>");
            out.println("<td>" + student.getStudentid() + "</td>");
            out.println("<td>" + student.getDepartment() + "</td>");
            out.println("<td>" + student.getPhonenum() + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
        out.close();
    }

}
