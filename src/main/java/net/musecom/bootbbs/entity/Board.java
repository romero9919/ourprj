package net.musecom.bootbbs.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="board")
public class Board {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY) 
   private long id;

   @Column(nullable=false, columnDefinition = "int default 0")
   private long refid;

   @Column(nullable=false, columnDefinition = "int default 0")
   private int renum;

   @Column(nullable=false, columnDefinition = "int default 0")
   private int depth;

   @Column(nullable=false, length=255)
   private String title;

   @Column(nullable = false, columnDefinition = "TEXT")
   private String content;

   private String writer;
   private String pass;

   @Column(nullable=false, columnDefinition = "int default 0")
   private int hit;

   @Column(nullable=false, columnDefinition = "datetime default current_timestamp")
   private LocalDateTime wdate = LocalDateTime.now();
}
