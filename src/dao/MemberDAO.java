package dao;

import static db.JDBCUtil.*;

import java.sql.*;
import java.util.ArrayList;

import bean.MemberBean;

public class MemberDAO {
	// singleton
	private static MemberDAO memberDAO;
	private MemberDAO() {
		
	}
	
	// MemberDAO 인스턴스 생성 메소드
	public static MemberDAO getInstance() {
		if(memberDAO==null) {
			memberDAO = new MemberDAO();
		}
		
		return memberDAO;
	}
	
	// db 설정용 필드
	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	// db 연결 메소드
	public void setConnection(Connection con) {
		this.con = con;
	}
	
	public int joinMember(MemberBean mb) {
		String sql = "INSERT INTO MEMBER (MEMBER_ID, MEMBER_PW, MEMBER_EMAIL, MEMBER_DATE) VALUES(?,?,?, SYSDATE)";
		int result = 0;
		
		System.out.println(":: MemberDAO ::");
		System.out.println("id :" + mb.getMEMBER_ID());
		System.out.println("pass :" + mb.getMEMBER_PW());
		System.out.println("email :" + mb.getMEMBER_EMAIL());
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mb.getMEMBER_ID());
			pstmt.setString(2, mb.getMEMBER_PW());
			pstmt.setString(3, mb.getMEMBER_EMAIL());
			
			result = pstmt.executeUpdate();
						
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("MemberDAO에서의 에러 메세지"+e.getMessage());
		} finally {
			try {
				close(pstmt);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	
	/** 이메일 인증 완료 검증 메소드 */
	public boolean getUserEmailChecked(String memberID) {
		String sql = "SELECT MEMBER_CHECKED FROM MEMBER WHERE MEMBER_ID = ?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getInt(1) == 1)
					return true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				rs.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return false; // 데이터베이스 오류
	}

	
	public boolean idCheck(String inputId) {
		String sql = "SELECT MEMBER_ID FROM MEMBER WHERE MEMBER_ID = ?";
		boolean result = false;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, inputId);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				System.out.println(rs.getString(1));
				result = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				close(pstmt);
				close(rs);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	public String getUserEmail(String memberID) {
		String sql = "SELECT MEMBER_EMAIL FROM MEMBER WHERE MEMBER_ID = ?";
		String result = null;
		System.out.println("MemberDAO 로 넘어온 memberID의 값 : "+memberID);
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				System.out.println("이메일 결과 값이 존재");
				result = rs.getString(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				close(pstmt);
				close(rs);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result; // 데이터베이스 오류
	}
	
	
	public int setUserEmailChecked(String memberID) {
		String sql = "UPDATE MEMBER SET MEMBER_CHECKED = 1 WHERE MEMBER_ID = ?";
		System.out.println("setUserEmailChecked에 넘어온 유저 아이디 : "+memberID);
		int result = 0;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberID);
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				close(pstmt);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public MemberBean loginMember(MemberBean mb) {
		String sql = "select * from member where member_id = ? and member_pw = ?";
		System.out.println("로그인 시도한 유저 아이디 : " + mb.getMEMBER_ID());
		MemberBean result = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mb.getMEMBER_ID());
			pstmt.setString(2, mb.getMEMBER_PW());
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = new MemberBean();
				result.setMEMBER_ID(rs.getString("MEMBER_ID"));
				result.setMEMBER_PW(rs.getString("MEMBER_PW"));
				result.setMEMBER_TEMPPASS(rs.getString("MEMBER_TEMPPASS"));
				result.setMEMBER_SETTEMP(rs.getInt("MEMBER_SETTEMP"));
				result.setMEMBER_EMAIL(rs.getString("MEMBER_EMAIL"));
				result.setMEMBER_CHECKED(rs.getInt("MEMBER_CHECKED"));
				result.setMEMBER_DATE(rs.getDate("MEMBER_DATE"));
				result.setMEMBER_SUSPENED(rs.getDate("MEMBER_SUSPENDED"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				close(rs);
				close(pstmt);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}