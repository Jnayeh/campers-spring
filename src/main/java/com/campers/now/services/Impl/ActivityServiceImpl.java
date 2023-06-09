package com.campers.now.services.Impl;

import com.campers.now.models.Activity;
import com.campers.now.models.CampingCenter;
import com.campers.now.repositories.ActivityRepository;
import com.campers.now.repositories.CampingCenterRepository;
import com.campers.now.services.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ActivityServiceImpl implements ActivityService {

    ActivityRepository activityRepository;
    CampingCenterRepository campingCenterRepository;

    @Override
    public List<Activity> getAll(Integer pageNumber, String property, Sort.Direction direction) {
        if (pageNumber == null)
            return activityRepository.findAll();
        return activityRepository.findAll(PageRequest.of((pageNumber <= 0 ? 1 : pageNumber) - 1, 10, Sort.by(List.of(Sort.Order.by(StringUtils.hasText(property) ? property : "id").with(direction)))))
                .stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Activity getById(Integer id) {
        return activityRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @Override

    public Activity addActivitybyCampingcenterId(Integer campingcenterId, Activity activity) {
           CampingCenter campingCenter = campingCenterRepository.findById(campingcenterId).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
            activity.setCampingCenter(campingCenter);
            return activityRepository.save(activity);
        }


    @Override
    public Activity update(Activity o) {
        getById(o.getId());
        try {
            return activityRepository.save(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void updateActivityStatus() {
        List<Activity> activities = activityRepository.findAll();
        String currentSeason = getCurrentSeason();

        for (Activity activity : activities) {
            if (activity.getSeason().toString() == currentSeason) {
                activity.setActive(true);
            } else {
                activity.setActive(false);
            }

            activityRepository.save(activity);
        }
    }



    private String getCurrentSeason() {
        LocalDate currentDate = LocalDate.now();
        int month = currentDate.getMonthValue();

        if (month >= 3 && month <= 5) {
            return "spring";
        } else if (month >= 6 && month <= 8) {
            return "summer";
        } else if (month >= 9 && month <= 11) {
            return "autumn";
        } else {
            return "winter";
        }
    }



    public List<Activity> getActiveActivities(Integer pageNumber, String property, Sort.Direction direction) {
        if (pageNumber == null) {
            return activityRepository.findByActiveTrue();
        } else {
            Sort.Order order = StringUtils.hasText(property)
                    ? Sort.Order.by(property).with(direction).ignoreCase()
                    : Sort.Order.by("id").with(direction);

            PageRequest pageRequest = PageRequest.of((pageNumber <= 0 ? 1 : pageNumber) - 1, 10, Sort.by(order));

            return activityRepository.findByActiveTrue(pageRequest).stream()
                    .collect(Collectors.toUnmodifiableList());
        }
    }
}