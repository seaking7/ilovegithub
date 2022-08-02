package com.poc.ilovegithub.core.repository;

import com.poc.ilovegithub.core.domain.OrgMembers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrgMembersRepository extends JpaRepository<OrgMembers, Long> {
}
