package com.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.board.domain.BoardDTO;

// 데이터베이스와의 통신 역할을 함

@Mapper // 마이바티스는 인터페이스에 @Mapper만 지정해주면 XML Mapper에서 메서드의 이름과 일치하는 SQL문을 찾아 실행함. Mapper 영역은 디비와 통신 즉 SQL 쿼리를 호출하는 것이 전부
public interface BoardMapper {
	
	public int insertBoard(BoardDTO params); // 게시글 생성. params에는 게시글 정보가 담기게 됨
	public BoardDTO selectBoardDetail(Long idx); // 하나의 게시글을 조회. 쿼리가 실행되면 각 컬럼에 해당하는 결과값이 리턴 타입으로 지정된 BoardDTO 클래스의 멤버 변수에 매핑됨.
	public int updateBoard(BoardDTO params); // 게시글 수정.
	public int deleteBoard(Long idx); // 게시글 삭제. 실제 데이터 삭제 ㄴㄴ. 컬럼의 상태 값을 'Y' 또는 'N'으로 지정
	public List<BoardDTO> selectBoardList(); // 게시글 목록을 조회
	public int selectBoardTotalCount(); //SELECT 쿼리를 호출하는 메서드. 페이징 처리 시 사용

}
