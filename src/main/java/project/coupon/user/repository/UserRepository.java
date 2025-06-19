package project.coupon.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.coupon.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
