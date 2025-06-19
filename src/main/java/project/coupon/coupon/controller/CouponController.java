package project.coupon.coupon.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.coupon.coupon.dto.AddCouponReqDto;
import project.coupon.coupon.dto.CouponReqDto;
import project.coupon.coupon.service.CouponService;

@RestController
@RequiredArgsConstructor
public class CouponController {

	private final CouponService couponService;

	@PostMapping("/coupon/issue")
	public String issueCoupon(@RequestBody CouponReqDto couponReqDto) {
		try {
			Long id = couponService.issueCoupon(couponReqDto);
			return "쿠폰 발급 성공 ID : " + id;
		}catch (Exception e) {
			return "쿠폰 발급 실패 : " + e.getMessage();
		}
	}

	@PostMapping("/coupon")
	public String addCoupon(@RequestBody AddCouponReqDto addCouponReqDto) {
		try {
			Long id = couponService.addCoupon(addCouponReqDto);
			return "쿠폰 생성 ID : " + id;
		} catch (Exception e) {
			return "쿠폰 생성 실패 : " + e.getMessage();
		}
	}
}
