package com.gdg.shop.repository;

import com.gdg.shop.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public class JpaMemberRepository implements MemberRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Member findById(Long id){
        return em.find(Member.class, id);
    }

    @Override
    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    @Override
    public Member findByLoginId(String loginId){
        List<Member> result = em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId).getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public void save(Member member){
        em.persist(member);
    }

    @Override
    public void deleteById(Long id){
        Member member = em.find(Member.class, id);
        em.remove(member);
    }
}

// [질문 정리 1] Repository 계층은 DB 접근을 담당한다.
// 여기서는 EntityManager를 사용해 Member Entity를 저장, 조회, 삭제한다.
//
// [질문 정리 2] findByLoginId의 JPQL은 "Member Entity 중 loginId가 같은 회원을 찾는다"는 의미이다.
// Hibernate는 이 JPQL을 실제 SQL select 문으로 변환해서 실행한다.
//
// [질문 정리 3] save에서 em.persist(member)를 호출하면 새 Entity가 영속성 컨텍스트에 등록되고 DB insert 대상이 된다.
// 그래서 회원 생성 요청 후 Hibernate insert 로그가 출력된다.
