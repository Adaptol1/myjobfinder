package com.example.myjobfinder.responder;

import com.example.myjobfinder.model.Vacancy;
import com.example.myjobfinder.model.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class Responder
{
    @Autowired
    private VacancyRepository vacancyRepository;

    private String defaultMessage = "Здравствуйте, %s! Меня заинтересовала вакансия \"%s\" в Вашей компании.\n" +
            "Я заинтересован в профессиональном росте и получении нового опыта как Java-разработчик, " +
            "поэтому хотел бы стать частью Вашей команды. " +
            "Я проживаю в Уфе и поэтому мне подойдёт только удалённый формат работы. " +
            "О себе могу сказать, что я трудолюбивый, всегда довожу свои задачи до логического конца, " +
            " владею технологиями, перечисленными в графе \"стек\", на данный" +
            " момент имею год коммерческого опыта как инженер-программист и 2 года опыта разработки на Java в целом. " +
            "Заинтересован в дальнейшем росте и развитии в рамках профессии.\n" +
            "Вот ссылка на мой GitHub: https://github.com/Adaptol1\n" +
            "Буду благодарен за обратную связь.";

    private String defaultResumeId = "b2164e6aff0b7d7fbf0039ed1f43496e6e6538";

    public String respondVacancy (Vacancy vacancy, String accessToken)
    {
        if (vacancy.getIssent())
            return "this vacancy is already responded";

        String message = String.format(
                defaultMessage,
                vacancy.getEmployer(),
                vacancy.getName());
        String url = String.format(
                "https://api.hh.ru/negotiations?message=%s&resume_id=%s&vacancy_id=%s",
                message,
                defaultResumeId,
                vacancy.getId());
        RequestEntity<Void> request = RequestEntity.post(url)
                .header("Authorization", "Bearer " + accessToken)
                .build();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Void>() {
                });
        vacancy.setIssent(true);
        vacancyRepository.save(vacancy);
        return response.getStatusCode().toString();
    }
}