package com.gdg.shop.repository;

import com.gdg.shop.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository {
    Member findById(Long id);
    List<Member> findAll();
    Member findByLoginId(String loginId);
    void save(Member member);
    void deleteById(Long id);
}
