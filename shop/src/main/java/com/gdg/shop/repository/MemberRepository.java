package com.gdg.shop.repository;

import com.gdg.shop.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Member findById(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Member findByLoginId(String loginId){
        List<Member> result = em.createQuery("select m from Member m where m.loginId = :loginId", Member.class)
                .setParameter("loginId", loginId).getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    public void save(Member member){
        em.persist(member);
    }

    public void deleteMember(Long id){
        Member member = em.find(Member.class, id);
        em.remove(member);
    }
}
