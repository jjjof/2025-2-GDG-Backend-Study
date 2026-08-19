package com.gdg.shop.repository;

import com.gdg.shop.domain.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemoryMemberRepository implements MemberRepository {

    private final Map<Long, Member> store = new HashMap<>();
    private long sequence = 0L;

    @Override
    public Member findById(Long id) {
        return store.get(id);
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Member findByLoginId(String loginId) {
        for (Member member : store.values()) {
            if (member.getLoginId().equals(loginId)) {
                return member;
            }
        }
        return null;
    }

    @Override
    public void save(Member member) {
        if (member.getId() == null) {
            sequence++;
            member.setId(sequence);
        }
        store.put(member.getId(), member);
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}
