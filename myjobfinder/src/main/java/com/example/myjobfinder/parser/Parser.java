package com.example.myjobfinder.parser;

import com.example.myjobfinder.model.Vacancy;
import com.example.myjobfinder.model.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;


@Service
public class Parser
{
    private int found;

    private int pages = 1;

    private int currentPage;

    private int perPage;
    @Autowired
    private VacancyRepository vacancyRepository;

    @Transactional
    public void getVacancies ()
    {
        String defaultUrl = "https://api.hh.ru/vacancies?page=%s&text=NAME:(Java and NOT JavaScript and NOT Frontend) and DESCRIPTION:(Java) " +
                    "&employment=full&schedule=remote&professional_role=96&currency=RUR&salary=80000" +
                    "&period=30&search_field=name&search_field=description&experience=between1And3&experience=noExperience";
        RestTemplate restTemplate = new RestTemplate();
        for (currentPage = 0; currentPage < pages; currentPage++)
        {
            try
            {
                String url = String.format(defaultUrl, currentPage);
                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<String>(){});
                JSONObject responseBody = new JSONObject(response.getBody());

                if (currentPage == 0)
                {
                    found = (int) responseBody.get("found");
                    pages = (int) responseBody.get("pages");
                }

                JSONArray items = responseBody.getJSONArray("items");
                for(int i = 0; i < items.length(); i++)
                {
                    JSONObject item = items.getJSONObject(i);
                    Vacancy vacancy = parseVacancy(item);

                    if (!vacancyRepository.existsById(vacancy.getId()))
                        vacancyRepository.save(vacancy);

                }
                System.out.println(currentPage);

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    private Vacancy parseVacancy (JSONObject item)
    {
        Integer id = null;
        StringBuilder employerTemp = new StringBuilder();
        StringBuilder nameTemp = new StringBuilder();
        StringBuilder salaryCurrencyTemp = new StringBuilder();
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
        try
        {
            if (!item.getJSONObject("employer").isNull("name"))
                employerTemp.append(item.getJSONObject("employer").get("name"));
            else
                employerTemp.append("null");

            if (!item.isNull("name"))
                nameTemp.append(item.get("name"));
            else
                nameTemp.append("null");

            hasTest = (Boolean) item.get("has_test");

            if (!item.isNull("salary"))
            {
                if (!item.getJSONObject("salary").isNull("from"))
                    salaryFrom = Integer.parseInt(item.getJSONObject("salary").get("from").toString());

                if (!item.getJSONObject("salary").isNull("to"))
                    salaryTo = Integer.parseInt(item.getJSONObject("salary").get("to").toString());

                if (!item.getJSONObject("salary").isNull("currency"))
                    salaryCurrencyTemp.append(item.getJSONObject("salary").get("currency"));
                else
                    salaryCurrencyTemp.append("null");
            }
            else
            {
                salaryCurrencyTemp.append("null");
            }

            if (!item.isNull("id"))
                id = Integer.parseInt(item.get("id").toString());

            if (!item.isNull("alternate_url"))
                alternateUrlTemp.append(item.get("alternate_url"));
            else
                alternateUrlTemp.append(item.get("null"));

            if (!item.isNull("snippet"))
            {
                if (!item.getJSONObject("snippet").isNull("requirement"))
                    requirementTemp.append(item.getJSONObject("snippet").get("requirement"));
                else
                    requirementTemp.append("null");

                if (!item.getJSONObject("snippet").isNull("responsibility"))
                    responsibilityTemp.append(item.getJSONObject("snippet").get("responsibility"));
                else
                    responsibilityTemp.append("null");
            }
            else
            {
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

        String employer = employerTemp.toString();
        String name = nameTemp.toString();
        String salaryCurrency = salaryCurrencyTemp.toString();
        String alternateUrl = alternateUrlTemp.toString();
        String requirement = requirementTemp.toString();
        String responsibility = responsibilityTemp.toString();
        String schedule = scheduleTemp.toString();
        String experience = experienceTemp.toString();
        String employment = employmentTemp.toString();
        Vacancy vacancy = new Vacancy ();
        vacancy.setId(id);
        vacancy.setEmployer(employer);
        vacancy.setAlternateurl(alternateUrl);
        vacancy.setEmployment(employment);
        vacancy.setExperience(experience);
        vacancy.setHastest(hasTest);
        vacancy.setIssent(isSent);
        vacancy.setName(name);
        vacancy.setRequirement(requirement);
        vacancy.setResponsibility(responsibility);
        vacancy.setSalarycurrency(salaryCurrency);
        vacancy.setSalaryfrom(salaryFrom);
        vacancy.setSalaryto(salaryTo);
        vacancy.setSchedule(schedule);
        return vacancy;
    }
}
