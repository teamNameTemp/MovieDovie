package com.board.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.entity.MemberEntity;
import com.board.entity.RentEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.NotificationRepository;
import com.board.entity.repository.RentRepository;
import com.board.entity.repository.movie.MovieRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
@Log4j2
public class RentServiceImpl implements RentService {

	private final RentRepository rentRepository;
	private final MovieRepository movieRepositoty;
	private final MemberRepository memberRepository;
	private final NotificationRepository notificationRepository;
	private final NotificationService notificationService;

	@Override
	public void updateRent(String email, Long movieId) {
		MemberEntity memE = memberRepository.findById(email)
				.orElseThrow(() -> new RuntimeException("Member not found " + email));
		MovieEntity movieE = movieRepositoty.findById(movieId)
				.orElseThrow(() -> new RuntimeException("Movie not found " + movieId));

		RentEntity rent = rentRepository.findByEmailAndMovieIdAndStatus(memE, movieE,"Y");

		LocalDateTime now = LocalDateTime.now();
		//대여날짜(returndate)가 지났는지 확인하고 rent.setStatus("N")으로 변경하는 코드
		if(rent != null && rent.getReturndate() != null) {
			if(now.isAfter(rent.getReturndate())) {
				rent.setStatus("N");
				rentRepository.save(rent);
			}
		}
	}

	// 영화 대여 시작
	@Transactional
	@Override
	public String rentMovie(String email, Long movieId) {
		String result = "rent";
		MemberEntity memE = memberRepository.findById(email)
				.orElseThrow(() -> new RuntimeException("Member not found " + email));
		MovieEntity movieE = movieRepositoty.findById(movieId)
				.orElseThrow(() -> new RuntimeException("Movie not found " + movieId));

		RentEntity rent = rentRepository.findByEmailAndMovieIdAndStatus(memE, movieE, "Y");

		// 대여 할 시
		if (rent == null) {

			//rent.payment에따라 포인트/결제 처리
			//등급에 따른 마일리지 추가
			//1.포인트계산
			// int mileage = calc.calcMileage(memE.getGrade(), movieE.getPrice());
			// memE.setPoint(memE.getPoint() - movieE.getPrice());
			// memE.setMileage(memE.getMileage() + mileage);
			//2.결제 처리
			
			rent = RentEntity.builder().rentdate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
					.returndate(LocalDateTime.now().plus(3, ChronoUnit.DAYS).truncatedTo(ChronoUnit.MINUTES))
					.status("Y").sales(movieE.getPrice()).email(memE).movieId(movieE).build();
			rentRepository.save(rent);
		} else {
			// 이미 대여 중
			result = "rented";
		}
		return result;

	}
	//매일 자정에 대여 만료 처리
	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void autoReturnOverdueRentals() {
		LocalDateTime now = LocalDateTime.now();

		List<RentEntity> overdueRentals = rentRepository.findAllByReturndateBeforeAndStatus(now, "Y");

		int updatedCount = 0;
		for (RentEntity rent : overdueRentals) {
			rent.setStatus("N");
			rentRepository.save(rent);
			updatedCount++;
		}

		log.info("자동 반환 처리 완료: {} 건의 대여가 반환 처리되었습니다.", updatedCount);
	}

	
	//매일 자정 확인하여 대여가 하루 남은 사람에게 알림 보내기
	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	@Override
	public void autoNotiBeforeReturn() {

		LocalDateTime now = LocalDateTime.now();
	    LocalDateTime tomorrow = now.plusDays(1);

	    List<RentEntity> rentalsToNotify = rentRepository.findAllByStatusAndReturndateBetween("Y", now, tomorrow);

		for (RentEntity rent : rentalsToNotify) {
			
			String receiverEmail = rent.getEmail().getEmail();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
			String formattedDateTime = rent.getReturndate().format(formatter);
			
			String content = "'" + rent.getMovieId().getTitle() + "'가 곧 반납됩니다! (반납일: " + formattedDateTime +")";
			
			notificationService.newRentNotiForUser(receiverEmail, content, "rent", rent.getSeqno());
			
			log.info(receiverEmail + " : " + content);
		}
	}
	
	public Map<String, Object> getMyRentInfo(String email, Long movieId) {
		Map<String, Object> data = new HashMap<>();

		MemberEntity memE = memberRepository.findById(email)
				.orElseThrow(() -> new RuntimeException("Member not found " + email));
		MovieEntity movieE = movieRepositoty.findById(movieId)
				.orElseThrow(() -> new RuntimeException("Movie not found " + movieId));

		RentEntity rent = rentRepository.findByEmailAndMovieIdAndStatus(memE, movieE, "Y");
		if (rent != null) {
			data.put("rentdata", rent.getRentdate());
			data.put("returndata", rent.getReturndate());
			data.put("status", rent.getStatus());
//			data.put("movie",rent.getMovieId());
		}

		return data;
	}

	// 관리자페이지 - 통계 : 대여 수 확인 (startDate에 원하는 날짜, endDate에 원하는 날짜 넣으면 그 사이의 값들이 나옴)
	@Override
	public List<Object[]> RentCount(LocalDateTime startDate, LocalDateTime endDate) {
		return rentRepository.RentCount(startDate, endDate);
	}
	
	//관리자페이지 - 통계 : 매출 확인 (startDate에 원하는 날짜, endDate에 원하는 날짜 넣으면 그 사이의 값들이 나옴)
    @Override
	public List<Object[]> Sales(LocalDateTime startDate, LocalDateTime endDate){
		return rentRepository.Sales(startDate, endDate);
	}

	// 관리자페이지 - 통계 : 월별 대여 수 확인
	@Override
	public List<Object[]> RentCountByMonth(int year) {
		return rentRepository.RentCountByMonth(year);
	}
	
	//관리자페이지 - 통계 : 월별 매출 확인
	@Override	
	public List<Object[]> SalesByMonth(int year){
		return rentRepository.SalesByMonth(year);
	}

	// 관리자 페이지 - 통계 : 올해 대여 status 수 확인
	@Override
	public List<Object[]> RentStatus() {
		return rentRepository.RentStatus();
	}

	// 관리자 페이지 - 통계 : 올해 대여 TOP 회원 확인
	public List<Object[]> findTopRenters() {
		return rentRepository.findTopRenterNickname();
	}

	@Override
	public List<Map<String, Object>> getAllMyRent(String email, String status) {
		List<Map<String,Object>> data = new ArrayList<>();
		
		MemberEntity memE = memberRepository.findById(email)
				.orElseThrow(() -> new RuntimeException("Member not found " + email));
	
		List<RentEntity> list = rentRepository.findAllByEmailAndStatus(memE, status);
		for(RentEntity rent : list) {
			Map<String,Object> map = new HashMap<>();
			
			map.put("id", rent.getMovieId().getMovieId());
			map.put("title", rent.getMovieId().getTitle());
			map.put("returndate", rent.getReturndate());
			map.put("posterPath",rent.getMovieId().getPosterPath());
			map.put("backdrop", rent.getMovieId().getBackdropPath());
			data.add(map);
		}

		//returndate 오름차순
		data.sort((a,b)->((LocalDateTime)a.get("returndate")).compareTo((LocalDateTime)b.get("returndate")));
		return data;
	}

	@Override
	public List<Map<String, Object>> getTopList(int topCount) {
		List<Map<String,Object>> data = new ArrayList<>();
		LocalDateTime startOfToday = LocalDateTime.now().minusDays(6);
		LocalDateTime endOfToday = LocalDateTime.now();
		List<Map<String,Object>> rents = rentRepository.findByRecentWeekList(startOfToday, endOfToday);
		
		int idx = topCount;
		for(Map<String,Object> m : rents) {
			if(idx==0)break;
			Long movieId = (Long) m.get("movieId");
			MovieEntity movie = movieRepositoty.findById(movieId).get();
			Map<String,Object> map = new HashMap<>();
			map.put("info", movie);
			map.put("count", m.get("count"));
			data.add(map);
			idx--;
		}
		
		return data;
	}

	@Override
	public Long getMyAccumulatedPayment(String email) {
		return rentRepository.getTotalSalesByEmail(email);
	}
	
	//관리자 페이지 - 통계 : 월별 영화발매일 별 대여 건수 확인
	@Override	
	public List<Object[]> RentCountByReleaseDateAndMonth(int year){
		return rentRepository.getRentCountByReleaseDateAndMonth(year);
	}
		
	//관리자 페이지 - 통계 : 최근 일주일 간 영화발매일 별 대여 건수 확인
	@Override	
	public List<Object[]> getRentCountByReleaseDateLastSevenDays(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo){
		return rentRepository.getRentCountByReleaseDateLastSevenDays(sevenDaysAgo);
	}
	
	//관리자 페이지 - 통계 : 영화 장르별 매출 현황 YTD 
	@Override	
	public List<Object[]> getYTDSalesByGenre(){
		return rentRepository.getYTDSalesByGenre();
	}
	
}
