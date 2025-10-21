package org.library.management.application;

import org.library.management.model.Member;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Author: Artyom Aroyan
 * Date: 21.10.25
 * Time: 23:10:28
 */
public interface MemberService {
    String addMember(String name, String email);
    Collection<Member> listOfMembers();
    Optional<Member> findMemberByName(String name);
    boolean deleteMemberById(UUID id);
}