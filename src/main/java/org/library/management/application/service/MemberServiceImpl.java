package org.library.management.application.service;

import org.library.management.application.MemberService;
import org.library.management.model.Member;

import java.time.Instant;
import java.util.*;

/**
 * Author: Artyom Aroyan
 * Date: 21.10.25
 * Time: 23:11:30
 */
public class MemberServiceImpl implements MemberService {
    private final ServiceHelper serviceHelper;
    private final Map<String, Member> members;

    public MemberServiceImpl(ServiceHelper serviceHelper) {
        this.serviceHelper = serviceHelper;
        this.members = serviceHelper.getMembers();
    }

    @Override
    public String addMember(String name, String email) {
        try {
            UUID id = UUID.randomUUID();
            Member member = new Member(id, name, email, Instant.now());
            members.put(serviceHelper.uuidToString(id), member);
            return serviceHelper.saveData();
        } catch (Exception ex) {
            return "Error creating member" + ex.getMessage();
        }
    }

    @Override
    public boolean existsById(UUID memberId) {
        return members.containsKey(serviceHelper.uuidToString(memberId));
    }

    @Override
    public Collection<Member> listOfMembers() {
        return members.values();
    }

    @Override
    public Optional<Member> findMemberByName(String name) {
        return members.values().stream()
                .filter(member -> member.name().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public boolean deleteMemberById(UUID id) {
        if (members.remove(serviceHelper.uuidToString(id)) != null) {
            serviceHelper.saveData();
            return true;
        }
        return false;
    }
}