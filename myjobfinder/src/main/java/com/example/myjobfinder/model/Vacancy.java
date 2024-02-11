package com.example.myjobfinder.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vacancies", schema="public")
public class Vacancy
{
    @Id
    private Integer id;
    private String employer;
    private String name;
    private Boolean hastest;
    private Integer salaryfrom;
    private Integer salaryto;
    private String salarycurrency;
    private String alternateurl;
    private String requirement;
    private String responsibility;
    private String schedule;
    private String experience;
    private String employment;
    private Boolean issent;
}

