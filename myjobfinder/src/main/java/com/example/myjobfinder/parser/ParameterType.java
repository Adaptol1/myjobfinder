package com.example.myjobfinder.parser;

public enum ParserParameter
{
    TEXT ("text"),
    EMPLOYMENT ("employment"),
    SCHEDULE ("schedule"),
    ROLE ("professional_role"),
    CURRENCY ("currency"),
    SALARY ("salary"),
    PERIOD ("period");
    private String parameter;
    ParserParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getTitle() {
        return parameter;
    }
}
