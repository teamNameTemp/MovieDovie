package com.board.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

@Component
public class DataCalculate {
	
	//개봉날짜에 따른 대여 비용 계산
	public int calcRentPrice(LocalDate release) {
        LocalDate today = LocalDate.now();
        long daysSinceRelease = ChronoUnit.DAYS.between(release, today);
        
        int basePrice = 5000; // 기본 대여 가격
        int result=0;

        if (daysSinceRelease < 30) {
            // 개봉 1개월 이내: 기본 가격의 150%
            result = (int)(basePrice * 2);
            // 개봉 1-3개월: 기본 가격의 120%
            result = (int)(basePrice * 1.2);
        } else if (daysSinceRelease < 180) {
            // 개봉 3-6개월: 기본 가격
            result = basePrice;
        } else if (daysSinceRelease < 365) {
            // 개봉 6개월-1년: 기본 가격의 80%
            result = (int)(basePrice * 0.8);
        } else {
            // 개봉 1년 이상: 기본 가격의 50%
            result = (int)(basePrice * 0.2);
        }

        return result;
    }

    //등급에 따른 마일리지 추가
    public int calcMileage(String grade, int price) {
        int result = 0;

        switch (grade) {
            case "DIAMOND":
                result = (int)(price * 0.05);
            break;
            case "PLATINUM":
                result = (int)(price * 0.04);
            break;  
            case "GOLD":                    
                result = (int)(price * 0.03);
            break;
            case "SILVER":
                result = (int)(price * 0.02);
            break;
            default:
                result = (int)(price * 0.01);
                break;
        }
        return result;
    }

    //나이 계산
    public boolean calcAge(String ssn) {
        int year = Integer.parseInt(ssn.substring(0, 2));
        int age = 2024 - year;

        return age >= 18;
    }
}
