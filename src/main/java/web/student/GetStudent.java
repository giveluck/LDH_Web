package web.student;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import biz.student.StudentDAO;
import biz.student.StudentVO;

@WebServlet("/getStudent.do")
public class GetStudent extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //응답 설정
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        //HTML 출력
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>학생 정보 검색</title>");
        out.println("<link rel='stylesheet' href='css/getstyle.css'>");
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
        
        out.println("<div class='content'>");
        out.println("<h1>학생 정보 검색</h1>");
        out.println("<hr>");
        
        //학번 입력 칸 생성
        out.println("<form action='getStudent.do' method='post'>");
        out.println("<label for='studentid'>학번:</label>");
        out.println("<input type='text' name='studentid' id='studentid' required>");
        out.println("<input type='submit' value='검색'>");
        out.println("</form>");
        out.println("<hr>");

        // 학번 파라미터 확인
        String studentid = request.getParameter("studentid");

        //학번이 입력되었는지 확인
        if (studentid != null && !studentid.isEmpty()) {
            try {
            	// 학번이 8자리인지 확인
                if ((int)(Math.log10(Integer.parseInt(studentid)) + 1) != 8) {
                    out.println("<p>학번은 8자리 숫자여야 합니다.</p>");
                    System.out.println(Math.log10(Integer.parseInt(studentid)) + 1);
                    return;
                }
            	//DB 연동 처리
                StudentVO vo = new StudentVO();
                vo.setStudentid(Integer.parseInt(studentid));
                StudentDAO dao = new StudentDAO();
                StudentVO student = dao.getStudent(vo);
                
                //학생 정보가 있으면 출력
                if (student != null) {
                    out.println("<table border='1' cellpadding='10'>");
            		out.println("<tr>");
            		out.println("<td bgcolor = '#B8C6CF'>이름</td>");
            		out.println("<td align='left'>" + student.getName() +  "</td>");
            		out.println("</tr>");
            		out.println("<tr>");
            		out.println("<td bgcolor = '#B8C6CF'>학번</td>");
            		out.println("<td align='left'>" + student.getStudentid() +  "</td>");
            		out.println("</tr>");
            		out.println("<tr>");
            		out.println("<td bgcolor = '#B8C6CF'>학과</td>");
            		out.println("<td align='left'>" + student.getDepartment() +  "</td>");
            		out.println("</tr>");
            		out.println("<tr>");
            		out.println("<td bgcolor = '#B8C6CF'>전화번호</td>");
            		out.println("<td align='left'>" + student.getPhonenum() +  "</td>");
            		out.println("</tr>");
                    out.println("</table>");
                } else {
                    //학생 정보가 없을 때 출력
                    out.println("<p>해당 학번의 학생을 찾을 수 없습니다.</p>");
                }
            } catch (NumberFormatException e) {
            	// TODO Auto-generated catch block
                //학번이 숫자가 아닐때 출력
                out.println("<p>올바른 학번을 입력하세요.</p>");
            }
        } else {
        	//학번이 입력되지 않았을 때 출력
        	 out.println("<p>학번을 입력해 주세요.</p>");
        }
        
        //HTML 출력 종료
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
