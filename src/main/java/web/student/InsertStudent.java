package web.student;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import biz.student.StudentDAO;
import biz.student.StudentVO;

/**
 * Servlet implementation class InsertStudent
 */
@WebServlet("/insertStudent.do")
public class InsertStudent extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String encoding;

    /**
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 인코딩 처리
        ServletContext context = getServletContext();
        encoding = context.getInitParameter("studentEncoding");
        request.setCharacterEncoding(encoding);

        // 사용자 입력 정보 추출
        String name = request.getParameter("name");
        String studentid = request.getParameter("studentid");
        String department = request.getParameter("department");
        String phonenum = request.getParameter("phonenum");

        // HTML 작성
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>학생 등록</title>");
        out.println("<link rel='stylesheet' href='css/insertstyle.css'>");
        out.println("</head>");
        out.println("<body>");

        // 네비게이션 메뉴 추가
        out.println("<div class='nav'>");
        out.println("<ul>");
        out.println("<li><a href='index.html'>홈 화면</a></li>");
        out.println("<li><a href='/insertStudent.do'>학생 등록</a></li>");
        out.println("<li><a href='/getStudentList.do'>학생 전체</a></li>");
        out.println("<li><a href='/getStudent.do'>학생 조회</a></li>");
        out.println("<li><a href='/updateStudent.do'>학생 수정</a></li>");
        out.println("</ul>");
        out.println("</div>");

        // 학생 등록 폼
        out.println("<div class='content'>");
        out.println("<h1>학생 등록</h1>");
        out.println("<form action='insertStudent.do' method='post'>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<td class='label'>이름</td>");
        //값이 null인 경우 빈 문자열을 넣는 삼항연산자
        out.println("<td><input type='text' name='name' value='" + (name != null ? name : "") + "' /></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td class='label'>학번</td>");
        out.println("<td><input type='text' name='studentid' value='" + (studentid != null ? studentid : "") + "' /></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td class='label'>학과</td>");
        out.println("<td><input type='text' name='department' value='" + (department != null ? department : "") + "' /></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td class='label'>전화번호</td>");
        out.println("<td><input type='text' name='phonenum' value='" + (phonenum != null ? phonenum : "") + "' /></td>");
        out.println("</tr>");

        out.println("<tr>");
        out.println("<td colspan='2' class='center'><input type='submit' value='학생등록' /></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</form>");

        // 데이터 검증 및 DB 처리
        try {
        	//이름 글자수 및 한글 확인
            if (name.length() < 2 || name.length() > 31) {
            	out.println("<p align = center>이름 글자수를 2자 이상 30자 이하로 해주세요.</p>");
            	return;
            } else if (!isHangul(name)) {
            	out.println("<p align = center>이름을 한글로 입력해 주세요</p>");
            	return;
            }
            // 학번이 8자리인지 확인
            int id2 = Integer.parseInt(studentid);
            if ((int)(Math.log10(id2) + 1) != 8) {
                out.println("<p align = center>학번은 8자리 숫자여야 합니다.</p>");
                return;
            }
            // 학번 중복 확인
            StudentVO vo = new StudentVO();
            StudentDAO dao = new StudentDAO();
            vo.setStudentid(id2);
            boolean isDuplicate = dao.duplicateStudent(vo);
            if (isDuplicate) {
                out.println("<p align = center>이미 존재하는 학번입니다. 다른 학번을 입력해 주세요.</p>");
                return;
            }
            //학과 글자수 및 한글 확인
            if (department.length() < 2 || department.length() > 31) {
            	out.println("<p align = center>학과 글자수를 2자 이상 30자 이하로 해주세요.</p>");
            	return;
            } else if (!isHangul(department)) {
            	out.println("<p align = center>학과를 한글로 입력해 주세요</p>");
            	return;
            }
            if(!phonenum.matches("^010-\\d{4}-\\d{4}$")) {
            	out.println("<p align = center>전화번호 형식을 맞춰 주세요(형식:010-XXXX-XXXX)</p>");
            	return;
            }
            // DB 연동 처리
            vo.setName(name);
            vo.setStudentid(id2);
            vo.setDepartment(department);
            vo.setPhonenum(phonenum);
            dao.insertStudent(vo);
          //등록되면 전체 출력창으로 이동
            response.sendRedirect("/getStudentList.do");

        } catch (NumberFormatException e) {
        	// TODO Auto-generated catch block
            // 학번이 숫자가 아니거나 오류 발생 시 메시지 출력
            out.println("<p align = center>학번은 8자리 숫자로 입력해 주세요.</p>");
        }
        
        out.println("</div>"); 
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
    
    public static boolean isHangul(String text) {
        //문자열을 char변수에 넣어 유니코드값을 비교한 후 문자열 전부 한글이면 true 아니면 false
        for (char c : text.toCharArray()) {
            if (c < '\uAC00' || c > '\uD7A3') {
                return false;
            }
        }
        return true;
    }
}
