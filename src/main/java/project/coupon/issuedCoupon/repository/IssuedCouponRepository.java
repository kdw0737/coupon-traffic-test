package project.coupon.issuedCoupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.coupon.issuedCoupon.entity.IssuedCoupon;

public interface IssuedCouponRepository extends JpaRepository<IssuedCoupon, Long> {
}
