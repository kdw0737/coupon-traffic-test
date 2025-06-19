package project.coupon.coupon.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.coupon.coupon.dto.CouponReqDto;
import project.coupon.coupon.entity.Coupon;
import project.coupon.coupon.repository.CouponRepository;
import project.coupon.issuedCoupon.entity.IssuedCoupon;
import project.coupon.issuedCoupon.repository.IssuedCouponRepository;
import project.coupon.user.entity.User;
import project.coupon.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponIssueService {

	private final CouponRepository couponRepository;
	private final UserRepository userRepository;
	private final IssuedCouponRepository issuedCouponRepository;

	// 실제 쿠폰 발급 로직 - 새로운 트랜잭션에서 실행
	@Transactional
	public Long attemptIssueCoupon(CouponReqDto couponReqDto) {
		// 1. 사용자 먼저 조회 (데드락 순서 일관성을 위해)
		User findUser = userRepository.findById(couponReqDto.getUserId())
			.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
		log.info("사용자 조회 완료: {}", findUser.getId());

		// 2. 쿠폰 조회 (낙관적 락 적용)
		Coupon findCoupon = couponRepository.findCouponById(couponReqDto.getCouponId())
			.orElseThrow(() -> new RuntimeException("쿠폰을 찾을 수 없습니다."));
		log.info("쿠폰 조회 완료: {}, 현재 수량: {}/{}",
			findCoupon.getId(), findCoupon.getIssuedQuantity(), findCoupon.getTotalQuantity());

		// 3. 재고 확인
		if (!findCoupon.isCouponIssueAvailable()) {
			log.info("선착순 쿠폰이 모두 소진되었습니다.");
			throw new RuntimeException("선착순 쿠폰이 모두 소진되었습니다.");
		}

		// 4. 쿠폰 발급 (여기서 낙관적 락이 적용됨)
		findCoupon.issueCoupon();
		log.info("쿠폰 수량 증가: {}/{}", findCoupon.getIssuedQuantity(), findCoupon.getTotalQuantity());

		// 5. 발급된 쿠폰 기록 저장
		IssuedCoupon issuedCoupon = IssuedCoupon.builder()
			.coupon(findCoupon)
			.user(findUser)
			.build();

		Long issuedCouponId = issuedCouponRepository.save(issuedCoupon).getId();
		log.info("발급된 쿠폰 저장 완료: {}", issuedCouponId);

		return issuedCouponId;
	}
}
