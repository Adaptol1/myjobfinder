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
    @SequenceGenerator(name = "vacancies_id_seq", sequenceName = "vacancies_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vacancies_id_seq")
    private Integer id;
    private String name;
    private Boolean hastest;
    private Integer salaryfrom;
    private Integer salaryto;
    private String salarycurrency;
    private String applyalternateurl;
    private String url;
    private String alternateurl;
    private String requirement;
    private String responsibility;
    private String schedule;
    private String experience;
    private String employment;
    private Boolean issent;
}

