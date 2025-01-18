package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;
import com.example.entity.Account;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{
    List<Message> findMessagesByPostedBy(Integer id);
}
