import java.time.LocalDate;
import java.time.Period;

import static java.time.temporal.ChronoUnit.DAYS;


public class Main {
    public static void main(String[] args) {

        // 예시 사용자의 입사일과 기준 일자 설정
        LocalDate hireDate = LocalDate.of(2012, 7, 15);
        LocalDate currentDate = LocalDate.of(2023, 7, 13);

        System.out.println("고용날짜: " + hireDate);
        System.out.println("현재날짜: " + currentDate);

        float fiscalAnnual = fiscalYearMethod(hireDate, currentDate);
        System.out.println("[회계연도 방식] 연차계산 결과 : "+fiscalAnnual);

        System.out.println("====================================");

        float dateJoined = entryDateMethod(hireDate, currentDate);
        System.out.println("[입사일자 방식] 연차계산 결과 : "+dateJoined);
    }

    // 회계연도 방식
    public static float fiscalYearMethod(LocalDate hireDate, LocalDate currentDate) {
        float result = 0.0f;

        int currentYear = currentDate.getYear(); // 현재연도
        int hireDateYear = hireDate.getYear(); // 고용연도

        int workingYears = currentYear - hireDateYear; // 근무년차
        System.out.println("근무년차 : " + workingYears);

        int basicYear = 15; // 기본년차

        if(workingYears < 2) { // 1년 미만자
            System.out.println("1년 미만자");

            if (currentYear > hireDateYear) { // 해를 넘긴자 : 15 * (근무일수 / 365)
                // 예) 2022.07.01 ~ 2023.02.28 => 6개월치만 계산 23년근무는 제외: 7.56일
                // 예) 2022.07.01 ~ 2023.12.31 => 1년 이상자도 6개월치만 계산 23년근무는 제외: 7.56일
                // 계산시 시작일을 포함시킨다.
                System.out.println("고용일이 년도를 넘었습니다.");

                long days = DAYS.between(hireDate, LocalDate.of(hireDateYear, 12, 31))+1;
                System.out.println("고용일 : " + (days) +"일");

                result = basicYear * ((float)days / 365);

            } else { // 해를 안넘긴자 : 한달 만근시 1개의 월차가 생긴다.
                System.out.println("고용일이 아직 년도를 넘지 않았습니다.");

                // 고용일부터 생긴 연차 개수
                Period period = Period.between(hireDate, currentDate); // 고용일부터 현재까지의 기간을 구함
                System.out.println("기준일 기준 고용일로부터 " + period.getMonths() + "개월이 경과하였습니다.");

                result = period.getMonths();
            }

        } else { // 2년차 이상
            System.out.println("2년차 이상");

            int[] additionalYearRange = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22}; // 추가년차범위 2년차 ~
            int additionalYear = 0; // 추가년차

            for(int i = 0; i < additionalYearRange.length; i++) {
                if (workingYears >= additionalYearRange[i]) {
                    additionalYear = i;
                } else {
                    break;
                }
            }
            result = basicYear + additionalYear;
        }

        return result;
    }

    // 입사일자 방식
    public static float entryDateMethod(LocalDate hireDate, LocalDate currentDate) {
        float result = 0.0f;
        hireDate = hireDate.minusDays(1); // 고용일 전날부터로 계산

        Period period = Period.between(hireDate, currentDate); // 고용일부터 현재까지의 기간을 구함
        int workingYears = period.getYears(); // 근무년
        int workingMonths = period.getMonths(); // 근무월
        int workingDays = period.getDays(); // 근무일
        
        System.out.println("근무일수 : " + workingYears + "년 " + (workingMonths+1) + "월 " + workingDays + "일");

        if(workingYears < 1) {
            System.out.println("11개월 30일까지");
            System.out.println("고용일로부터 " + workingYears + "년 " + (workingMonths+1) + "월 " + workingDays + "개월이 경과하였습니다.");

            hireDate = hireDate.plusDays(1);
            period = Period.between(hireDate, currentDate);

            // 고용일부터 생긴 연차 개수
            result = period.getMonths();
        } else if(workingYears == 1 && workingMonths == 0 && workingDays == 0) { // 1년 까지
            System.out.println("근무일 1년 까지");
            System.out.println("고용일로부터 " + workingYears + "년 " + (workingMonths+1) + "월 " + workingDays + "개월이 경과하였습니다.");

            // 고용일부터 1년까지 생긴 연차 최대 갯수는 11개
            result = 11;
        } else { // 1년차 이상
            /*
                예) 2021.7.15 ~ 2022.07.14 => 11개
                예) 2021.7.15 ~ 2022.07.15 => 15개
                예) 2021.7.15 ~ 2023.07.14 => 15개
                예) 2021.7.15 ~ 2023.07.15 => 15개
                예) 2021.7.15 ~ 2024.07.14 => 16개
                예) 2021.7.15 ~ 2024.07.15 => 16개
                예) 2021.7.15 ~ 2028-07-13 => 17개
                예) 2021.7.15 ~ 2028-07-14 => 18개
                예) 2012-07-15 ~ 2023-07-13 => 19개
             */
            System.out.println("근무일 1년 1일 이상자");

            int basicYear = 15; // 기본년차
            int[] yearLabels = new int[] {1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23};

            for(int i = 0; i < yearLabels.length-1; i++) {
                if(workingYears >= 21) {
                    result = 25; //max는 25일
                  break;
                } else if(workingYears < yearLabels[i]) {
                    result = basicYear + (i-1);
                    break;
                }
            }

        }

        return result;
    }



}