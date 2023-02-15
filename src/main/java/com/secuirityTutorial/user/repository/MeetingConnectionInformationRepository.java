package com.secuirityTutorial.user.repository;

import com.secuirityTutorial.user.entity.MeetingConnectInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingConnectionInformationRepository extends JpaRepository<MeetingConnectInformation,Long> {
}
