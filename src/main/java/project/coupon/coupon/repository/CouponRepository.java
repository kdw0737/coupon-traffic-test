package project.coupon.coupon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import jakarta.persistence.LockModeType;
import project.coupon.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	// @Lock(LockModeType.PESSIMISTIC_WRITE)
	// Optional<Coupon> findCouponById(Long id);

	@Lock(LockModeType.OPTIMISTIC)
	Optional<Coupon> findCouponById(Long id);
}
