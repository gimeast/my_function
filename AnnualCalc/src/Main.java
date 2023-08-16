import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {

        // 예시 사용자의 입사일과 기준 일자 설정
        LocalDate hireDate = LocalDate.of(2000, 1, 1);
        LocalDate currentDate = LocalDate.now();

        System.out.println("고용날짜: " + hireDate);
        System.out.println("현재날짜: " + currentDate);

        float fiscalAnnual = 회계연도방식(hireDate, currentDate);
        System.out.println("회계연도 방식 연차계산 결과 : "+fiscalAnnual);
    }

    //회계연도 방식
    public static float 회계연도방식(LocalDate hireDate, LocalDate currentDate) {
        float result = 0.0f;

        int currentYear = currentDate.getYear(); // 현재연도
        int hireDateYear = hireDate.getYear(); // 고용연도

        int 근무년차 = currentYear - hireDateYear;
        System.out.println("근무년차 : " + 근무년차);

        int 기본년차 = 15;

        if(근무년차 < 2) { // 1년 미만자
            System.out.println("1년 미만자");

            if (currentYear > hireDateYear) { // 해를 넘긴자 : 15 * (근무일수 / 365)
                // 예) 2022.07.01 ~ 2023.02.28 => 6개월치만 계산 23년근무는 제외: 7.56일
                // 예) 2022.07.01 ~ 2023.12.31 => 1년 이상자도 6개월치만 계산 23년근무는 제외: 7.56일
                // 계산시 시작일을 포함시킨다.
                System.out.println("고용일이 년도를 넘었습니다.");

                long days = ChronoUnit.DAYS.between(hireDate, LocalDate.of(hireDateYear, 12, 31))+1;
                System.out.println("고용일 : " + (days) +"일");

                result = 기본년차 * ((float)days / 365);

            } else { // 해를 안넘긴자 : 한달 만근시 1개의 월차가 생긴다.
                System.out.println("고용일이 아직 년도를 넘지 않았습니다.");

                // 고용일부터 생긴 연차 개수
                Period period = Period.between(hireDate, currentDate); // 고용일부터 현재까지의 기간을 구함
                System.out.println("기준일 기준 고용일로부터 " + period.getMonths() + "개월이 경과하였습니다.");

                result = period.getMonths();
            }

        } else {
            int[] 추가년차_범위 = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22};
            int 추가년차 = 0;
            for(int i = 0; i < 추가년차_범위.length; i++) {
                if (근무년차 >= 추가년차_범위[i]) {
                    추가년차 = i;
                } else {
                    break;
                }
            }
            result = 기본년차 + 추가년차;
        }

        return result;
    }



}