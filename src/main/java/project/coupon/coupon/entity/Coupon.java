package project.coupon.coupon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.coupon.common.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class Coupon extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Long totalQuantity; // 총 발급 가능 수량

	private Long issuedQuantity = 0L; // 현재 발급된 수량

	@Version
	private Long version = 0L;

	// 메소드
	public void issueCoupon() {
		this.issuedQuantity++;
	}

	public boolean isCouponIssueAvailable() {
		return this.issuedQuantity < this.totalQuantity;
	}

	public Long getCouponStock() {
		return this.totalQuantity - this.issuedQuantity;
	}
}
