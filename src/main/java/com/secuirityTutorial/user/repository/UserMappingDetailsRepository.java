package com.secuirityTutorial.user.repository;

import com.secuirityTutorial.user.entity.UserMappingDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMappingDetailsRepository extends JpaRepository<UserMappingDetails,Long> {

    List<UserMappingDetails> findByUserIdAndLinkedToIdIn(Long userId,List<Long> linkedToIds);

    Optional<UserMappingDetails> findByUserIdAndLinkedToIdAndLinkedToStatus(Long userId, Long linkedToId,String status);


    public List<UserMappingDetails> findAll(Specification<UserMappingDetails> specification, Pageable pageable);

    public List<UserMappingDetails> findAll(Specification<UserMappingDetails> specification);

    public List<UserMappingDetails> findAllByUserId(Long userId,Pageable pageable);

    public List<UserMappingDetails> findAllByUserId(Long userId);
}
