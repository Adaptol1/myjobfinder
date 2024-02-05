package com.example.myjobfinder.parser;

import com.example.myjobfinder.model.Vacancy;
import com.example.myjobfinder.model.VacancyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
@Service
public class Parser
{
    private int found;

    private int pages;

    private int currentPage;

    private int perPage;
    @Autowired
    private VacancyRepository vacancyRepository;

    @Transactional
    public void getVacancies ()
    {
        String url = "https://api.hh.ru/vacancies?text=NAME:(Java and NOT JavaScript and NOT Frontend) and DESCRIPTION:(Java) " +
                    "&employment=full&schedule=remote&professional_role=96&currency=RUR&salary=80000" +
                    "&period=30&search_field=name&search_field=description&experience=between1And3&experience=noExperience";
        RestTemplate restTemplate = new RestTemplate();
        try
        {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<String>(){});
            JSONObject responseBody = new JSONObject(response.getBody());
            found = (int) responseBody.get("found");
            pages = (int) responseBody.get("pages");
            currentPage = (int) responseBody.get("page");
            perPage = (int) responseBody.get("per_page");
            JSONArray items = responseBody.getJSONArray("items");
            ArrayList<Vacancy> vacancies = new ArrayList<>();
            for(int i = 0; i < items.length(); i++)
            {
                JSONObject item = items.getJSONObject(i);
                vacancyRepository.save(parseVacancy(item));
            }

            System.out.println(vacancyRepository.findById(0).get().getAlternateurl());
            System.out.println(vacancyRepository.findById(0).get().getHastest());
            System.out.println(vacancyRepository.findById(0).get().getRequirement());
            System.out.println(vacancyRepository.findById(0).get().getSalaryfrom());
            System.out.println(vacancyRepository.findById(0).get().getSalaryto());
            System.out.println(vacancyRepository.findById(0).get().getSalarycurrency());
            System.out.println(vacancyRepository.findById(0).get().getApplyalternateurl());
            System.out.println(vacancyRepository.findById(0).get().getEmployment());
            System.out.println(vacancyRepository.findById(0).get().getExperience());
            System.out.println(vacancyRepository.findById(0).get().getName());
            System.out.println(vacancyRepository.findById(0).get().getResponsibility());
            System.out.println(vacancyRepository.findById(0).get().getSchedule());
            System.out.println(vacancyRepository.findById(0).get().getUrl());
//            System.out.println(new String(vacancies.get(0).requirement().getBytes(), StandardCharsets.UTF_8));
//            System.out.println(found +  "\n" + pages + "\n" + currentPage);
//            System.out.println(items.get(0));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }
    private Vacancy parseVacancy (JSONObject item)
    {
        StringBuilder nameTemp = new StringBuilder();
        StringBuilder salaryCurrencyTemp = new StringBuilder();
        StringBuilder applyAlternateUrlTemp = new StringBuilder();
        StringBuilder urlTemp = new StringBuilder();
        StringBuilder alternateUrlTemp = new StringBuilder();
        StringBuilder requirementTemp = new StringBuilder();
        StringBuilder responsibilityTemp = new StringBuilder();
        StringBuilder scheduleTemp = new StringBuilder();
        StringBuilder experienceTemp = new StringBuilder();
        StringBuilder employmentTemp = new StringBuilder();
        Boolean hasTest = true;
        Integer salaryFrom = null;
        Integer salaryTo = null;
        Boolean isSent = false;
        try {
            if (!item.isNull("name"))
                nameTemp.append(item.get("name"));
            else
                nameTemp.append("null");

            hasTest = (Boolean) item.get("has_test");

            if (!item.isNull("salary")) {
                if (!item.getJSONObject("salary").isNull("from"))
                    salaryFrom = (Integer) (item.getJSONObject("salary").get("from"));

                if (!item.getJSONObject("salary").isNull("to"))
                    salaryTo = (Integer) (item.getJSONObject("salary").get("to"));

                if (!item.getJSONObject("salary").isNull("currency"))
                    salaryCurrencyTemp.append(item.getJSONObject("salary").get("currency"));
                else
                    salaryCurrencyTemp.append("null");
            } else {
                salaryCurrencyTemp.append("null");
            }

            if (!item.isNull("apply_alternate_url"))
                applyAlternateUrlTemp.append(item.get("apply_alternate_url"));
            else
                applyAlternateUrlTemp.append("null");

            if (!item.isNull("url"))
                urlTemp.append(item.get("url").toString());
            else
                urlTemp.append("null");

            if (!item.isNull("alternate_url"))
                alternateUrlTemp.append(item.get("alternate_url"));
            else
                alternateUrlTemp.append(item.get("null"));

            if (!item.isNull("snippet")) {
                if (!item.getJSONObject("snippet").isNull("requirement"))
                    requirementTemp.append(item.getJSONObject("snippet").get("requirement"));
                else
                    requirementTemp.append("null");

                if (!item.getJSONObject("snippet").isNull("responsibility")) {
                    responsibilityTemp.append(item.getJSONObject("snippet").get("responsibility"));
                } else
                    responsibilityTemp.append("null");
            } else {
                requirementTemp.append("null");
                responsibilityTemp.append("null");
            }

            if (!item.getJSONObject("schedule").isNull("name"))
                scheduleTemp.append(item.getJSONObject("schedule").get("name"));
            else
                scheduleTemp.append("null");

            if (!item.getJSONObject("experience").isNull("name"))
                experienceTemp.append(item.getJSONObject("experience").get("name"));
            else
                experienceTemp.append("null");

            if (!item.getJSONObject("employment").isNull("name"))
                employmentTemp.append(item.getJSONObject("employment").get("name"));
            else
                employmentTemp.append("null");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String name = nameTemp.toString();
        String salaryCurrency = salaryCurrencyTemp.toString();
        String applyAlternateUrl = applyAlternateUrlTemp.toString();
        String url = urlTemp.toString();
        String alternateUrl = alternateUrlTemp.toString();
        String requirement = requirementTemp.toString();
        String responsibility = responsibilityTemp.toString();
        String schedule = scheduleTemp.toString();
        String experience = experienceTemp.toString();
        String employment = employmentTemp.toString();
        Vacancy vacancy = new Vacancy ();
        vacancy.setAlternateurl(alternateUrl);
        vacancy.setEmployment(employment);
        vacancy.setExperience(experience);
        vacancy.setApplyalternateurl(applyAlternateUrl);
        vacancy.setHastest(hasTest);
        vacancy.setIssent(isSent);
        vacancy.setName(name);
        vacancy.setRequirement(requirement);
        vacancy.setResponsibility(responsibility);
        vacancy.setSalarycurrency(salaryCurrency);
        vacancy.setSalaryfrom(salaryFrom);
        vacancy.setSalaryto(salaryTo);
        vacancy.setSchedule(schedule);
        vacancy.setUrl(url);
        return vacancy;
    }
}
