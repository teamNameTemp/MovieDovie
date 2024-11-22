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
}
