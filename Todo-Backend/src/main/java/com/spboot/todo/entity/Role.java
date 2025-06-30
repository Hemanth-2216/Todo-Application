package com.spboot.todo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Roles")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter

public class Role {
	
	
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int  id ;
	 
	 @Column(nullable = false)
	 private String  name;
	
	 
}
