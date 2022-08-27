package com.poc.ilovegithub.core.repository;

import com.poc.ilovegithub.core.domain.MailSender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailSenderRepository extends JpaRepository<MailSender, Long> {

    Page<MailSender> findByStatusEquals(String Status, Pageable pageable);
}
