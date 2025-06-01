package com.code81.library.lms.repository;

import com.code81.library.lms.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}