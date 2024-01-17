package com.nphc.hr.dao;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "Employee")
public class Employee {

   //id,login,name,salary,startDate
    //e0001,hpotter,Harry Potter,1234.00,16-Nov-01
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "name")
    private String name;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "startDate")
    private LocalDate startDate;


 public String getId() {
  return id;
 }

 public void setId(String id) {
  this.id = id;
 }

 public String getLogin() {
  return login;
 }

 public void setLogin(String login) {
  this.login = login;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public BigDecimal getSalary() {
  return salary;
 }

 public void setSalary(BigDecimal salary) {
  this.salary = salary;
 }

 public LocalDate getStartDate() {
  return startDate;
 }

 public void setStartDate(LocalDate startDate) {
  this.startDate = startDate;
 }

 @Override
 public String toString() {
  return "Employee{" +
          "id='" + id + '\'' +
          ", login='" + login + '\'' +
          ", name='" + name + '\'' +
          ", salary=" + salary +
          ", startDate=" + startDate +
          '}';
 }




}
