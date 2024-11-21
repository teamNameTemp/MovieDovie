package com.board.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.entity.MemberEntity;
import com.board.entity.RentEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.repository.MemberRepository;
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
			// 대여 할 시 돈 차감
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

	@Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
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

	// 관리자페이지 - 통계 : 월별 대여 수 확인
	@Override
	public List<Object[]> RentCountByMonth(int year) {
		return rentRepository.RentCountByMonth(year);
	}

	// 관리자 페이지 - 통계 : 올해 대여 status 수 확인
	@Override
	public List<Object[]> RentStatus() {
		return rentRepository.RentStatus();
	}

	// 관리자 페이지 - 통계 : 올해 대여 TOP 회원 확인
	public List<Object[]> findTopRenters() {
		return rentRepository.findTopRenters();
	}

	@Override
	public List<Map<String, Object>> getAllMyRent(String email, String status) {
		List<Map<String,Object>> data = new ArrayList<>();
		
		MemberEntity memE = memberRepository.findById(email)
				.orElseThrow(() -> new RuntimeException("Member not found " + email));
	
		List<RentEntity> list = rentRepository.findAllByEmailAndStatus(memE, "Y");
		for(RentEntity rent : list) {
			Map<String,Object> map = new HashMap<>();
			
			map.put("id", rent.getMovieId().getMovieId());
			map.put("title", rent.getMovieId().getTitle());
			map.put("returndate", rent.getReturndate());
			map.put("posterPath",rent.getMovieId().getPosterPath());
			map.put("backdrop", rent.getMovieId().getBackdropPath());
			data.add(map);
		}
		return data;
	}

}
