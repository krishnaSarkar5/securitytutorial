package com.secuirityTutorial.user.repository;

import com.secuirityTutorial.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    public Optional<User> findByEmail(String email);

    public List<User> findByIdInAndStatus(List<Long> userIds,String status);

    public List<User> findAll(Specification<User> specification,Pageable pageable);

    public List<User> findAll(Specification<User> specification);

    public Page<User> findAll(Pageable pageable);

    @Query(value = "select * from user u where month(u.dob)=month(?1) and day(u.dob)=day(?1)",nativeQuery = true)
    public List<User> findAllByDob(LocalDate time);

}
