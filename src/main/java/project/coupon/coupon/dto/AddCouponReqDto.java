package project.coupon.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddCouponReqDto {
	private String couponName;

	private Long totalQuantity;

	private Long issuedQuantity;
}
