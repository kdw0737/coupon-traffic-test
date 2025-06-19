package project.coupon.coupon.service;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.coupon.coupon.dto.AddCouponReqDto;
import project.coupon.coupon.dto.CouponReqDto;
import project.coupon.coupon.entity.Coupon;
import project.coupon.coupon.repository.CouponRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;
	private final CouponIssueService couponIssueService; // 새로운 서비스 주입

	// 재시도 로직만 담당 - 트랜잭션 없음
	public Long issueCoupon(CouponReqDto couponReqDto) {
		int maxRetries = 10;
		int retryCount = 0;

		while (retryCount < maxRetries) {
			try {
				return couponIssueService.attemptIssueCoupon(couponReqDto);
			} catch (OptimisticLockingFailureException e) {
				retryCount++;
				log.warn("낙관적 락 충돌 발생 - 재시도 {}/{}", retryCount, maxRetries);

				if (retryCount >= maxRetries) {
					log.error("최대 재시도 횟수 초과 - 쿠폰 발급 실패");
					throw new RuntimeException("쿠폰 발급에 실패했습니다. 다시 시도해주세요.");
				}

				// Exponential Backoff + Jitter
				try {
					long delay = (long) (Math.pow(2, retryCount) * 10 + Math.random() * 100);
					Thread.sleep(delay);
				} catch (InterruptedException ie) {
					Thread.currentThread().interrupt();
					throw new RuntimeException("쿠폰 발급이 중단되었습니다.");
				}
			}
		}

		throw new RuntimeException("쿠폰 발급에 실패했습니다.");
	}

	@Transactional
	public Long addCoupon(AddCouponReqDto addCouponReqDto) {
		Coupon coupon = Coupon.builder()
			.name(addCouponReqDto.getCouponName())
			.issuedQuantity(addCouponReqDto.getIssuedQuantity())
			.totalQuantity(addCouponReqDto.getTotalQuantity())
			.build();

		return couponRepository.save(coupon).getId();
	}
}
