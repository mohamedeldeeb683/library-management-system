package com.code81.library.lms.service;

import com.code81.library.lms.entity.Member;
import com.code81.library.lms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class MemberService {
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }


    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    public Member updateMember(Member memberDetails) {
        return memberRepository.findById(memberDetails.getId())
                .map(existingMember -> {
                    existingMember.setName(memberDetails.getName());
                    existingMember.setAddress(memberDetails.getAddress());
                    existingMember.setPhone(memberDetails.getPhone());
                    existingMember.setEmail(memberDetails.getEmail());
                    existingMember.setRegistrationDate(memberDetails.getRegistrationDate());
                    return memberRepository.save(existingMember);
                })
                .orElse(null);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}