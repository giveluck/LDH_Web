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

@WebServlet("/updateStudent.do")
public class UpdateStudent extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String encoding;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 인코딩 설정
        ServletContext context = getServletContext();
        encoding = context.getInitParameter("studentEncoding");
        request.setCharacterEncoding(encoding);
        
        // 응답 설정
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //HTML 출력 시작
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>학생 정보 수정</title>");
        out.println("<link rel='stylesheet' href='css/updatestyle.css'>");
        out.println("</head>");
        out.println("<body>");
        
        // 네비게이션 메뉴 추가
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
        out.println("<h1>학생 정보 수정</h1>");
        out.println("<hr>");
        // 학번 입력 칸 생성
        out.println("<form action='updateStudent.do' method='post'>");
        out.println("<label for='studentid'>학번:</label>");
        out.println("<input type='text' name='studentid' id='studentid' required>");
        out.println("<input type='submit' value='검색'>");
        out.println("</form>");
        out.println("<hr>");

        //학번 파라미터 확인 및 학생 정보 처리
        String studentid = request.getParameter("studentid");
        
        // 학번이 유효하면 처리
        if (studentid != null && !studentid.isEmpty()) {
            try {
                if ((int)(Math.log10(Integer.parseInt(studentid)) + 1) != 8) {
                    out.println("<p>학번은 8자리 숫자여야 합니다.</p>");
                    return;
                }
                StudentVO vo = new StudentVO();
                vo.setStudentid(Integer.parseInt(studentid));
                StudentDAO dao = new StudentDAO();
                StudentVO student = dao.getStudent(vo);

                //학생 정보가 있으면 수정 가능 폼 출력
                if (student != null) {
                    out.println("<form action='updateStudent.do' method='post'>");
                    out.println("<table border='1' cellpadding='10'>");
                    out.println("<tr>");
                    out.println("<td bgcolor = '#B8C6CF'>이름</td>");
                    out.println("<td>" + student.getName() + "<input type='hidden' name='name' value='" + student.getName() + "'></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td bgcolor='#B8C6CF'>학번</td>");
                    out.println("<td>" + student.getStudentid() + "<input type='hidden' name='studentid' value='" + student.getStudentid() + "'></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td bgcolor='#B8C6CF'>학과</td>");
                    out.println("<td><input type='text' name='department' value='" + student.getDepartment() + "'></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td bgcolor='#B8C6CF'>전화번호</td>");
                    out.println("<td><input type='text' name='phonenum' value='" + student.getPhonenum() + "'></td>");
                    out.println("</tr>");
                    out.println("<tr>");
                    out.println("<td colspan='2' class='center'><input type='submit' value='학생 수정' /></td>");
                    out.println("</tr>");
                    out.println("</table>");
                    out.println("</form>");
                } else {
                	// 없는 학번을 입력했을 때 출력
                    out.println("<p>해당 학번의 학생을 찾을 수 없습니다.</p>");
                }
            } catch (NumberFormatException e) {
            	//학번이 숫자가 아닐때 출력
                out.println("<p>올바른 학번을 입력하세요.</p>");
            }
        } else {
        	//학번을 입력하지 않았을 때 출력
            out.println("<p>학번을 입력해 주세요.</p>");
        }

        //입력된 데이터를 이용하여 학생 정보 수정
        String department = request.getParameter("department");
        String phonenum = request.getParameter("phonenum");
        //이름,전화번호 양식에 맞는지 확인
        if (department != null && phonenum != null) {
            if (department.length() < 2 || department.length() > 31) {
                out.println("<p>학과 글자수를 2자 이상 30자 이하로 해주세요.</p>");
                return;
            } else if (!isHangul(department)) {
                out.println("<p>학과를 한글로 입력해 주세요</p>");
                return;
            }
            if (!phonenum.matches("^010-\\d{4}-\\d{4}$")) {
                out.println("<p>전화번호 형식을 맞춰 주세요(형식:010-XXXX-XXXX)</p>");
                return;
            }

            try {
                StudentVO vo = new StudentVO();
                vo.setStudentid(Integer.parseInt(request.getParameter("studentid")));
                vo.setDepartment(department);
                vo.setPhonenum(phonenum);

                StudentDAO dao = new StudentDAO();
                dao.updateStudent(vo);
                //수정되면 전체 출력창으로 이동
                response.sendRedirect("/getStudentList.do");
            } catch (Exception e) {
                out.println("<p>학생 정보 수정 중 오류가 발생했습니다.</p>");
            }
        }

        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    public static boolean isHangul(String text) {
        for (char c : text.toCharArray()) {
            if (c < '\uAC00' || c > '\uD7A3') {
                return false;
            }
        }
        return true;
    }
}
