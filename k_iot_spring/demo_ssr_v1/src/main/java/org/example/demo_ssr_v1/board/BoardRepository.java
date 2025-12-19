package org.example.demo_ssr_v1.board;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 전체 게시글 조회 (기존에 사용하던 부분 사용 안 할 예정)
    //List<Board> findAllByOrderByCreatedAtDesc();

    // 게시글 전체 조회 (작성자 정보 포함, JOIN FETCH 사용)
//    @Query("SELECT b FROM Board b JOIN FETCH b.user ORDER BY b.createdAt DESC")
//    List<Board> findAllWithUserOrderByCreatedAtDesc();

    // 게시글 전체 죄회(페이지 처리)
    // - 인수값은 우리가 생성한 Pageable 객체를 넣어주면 됨
    // - 리턴 타입은 Page 객체로 반환된다.

    /**
     *
     * @param pageable 페이징 정보(페이지 번호, 페이지 크기, 정렬) // 겁색어 없을때 사용
     * @return 페이징된 BoardList를 가지고 있다. (단 작성자 정보 포함)
     * JOIN FETCH 때문에 하이버 네이트가 쿼리를 이상하게 작성하는 것을 막는 처리
     * select 절에 distinct 를 사용하면 정확한 count를 가져올 수 있음
     * countQuery - 전체 게시글에 개수를 빠르게 가져오기 위해 사용한다. (성능향상 목적)
     */
    @Query(value = "SELECT DISTINCT b FROM Board b JOIN FETCH b.user ORDER BY b.createdAt DESC",
    countQuery = "SELECT COUNT(DISTINCT b) FROM Board b")
    Page<Board> findAllWithUserOrderByCreatedAtDesc(Pageable pageable);
//    Page<Board> findAllWithUserOrderByCreatedAtDesc(@Param("keyword") Pageable pageable);

    /**
     * 게시글 검색(제목 또는 내용, 페이징 포함)
     * @param pageable
     * @return
     */
    @Query(value = "SELECT DISTINCT b FROM Board b JOIN FETCH b.user " +
            "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY b.createdAt DESC",
            countQuery = "SELECT COUNT(DISTINCT b) FROM Board b " +
                    "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                    "   OR LOWER(b.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Board> findByTitleContainingOrContentContaining(@Param("keyword") String keyword, Pageable pageable);

    // 게시글 ID로 조회 (작성자 정보 포함 - JOIN FETCH 사용해야 함)
    @Query("SELECT b FROM Board b JOIN FETCH b.user WHERE b.id = :id")
    Optional<Board> findByIdWithUser(@Param("id") Long id);

}
