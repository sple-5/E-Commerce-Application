package com.app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.config.AppConstants;
import com.app.entites.*;
import com.app.repositories.*;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@SecurityScheme(name = "E-Commerce Application", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class ECommerceApplication implements CommandLineRunner {
	@Autowired
	private RoleRepo roleRepo;

	// @Autowired
	// private UserRepo userRepo;

	// @Autowired
	// private AddressRepo addressRepo;

	// @Autowired
	// private CartRepo cartRepo;

	// @Autowired
	// private CartItemRepo cartItemRepo;

	// @Autowired
	// private CategoryRepo categoryRepo;

	// @Autowired
	// private BrandRepo brandRepo;

	// @Autowired
	// private OrderRepo orderRepo;

	// @Autowired
	// private OrderItemRepo orderItemRepo;

	// @Autowired
	// private PaymentRepo paymentRepo;

	// @Autowired
	// private ProductRepo productRepo;

	// @Autowired
	// private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			Role adminRole = new Role();
			adminRole.setRoleId(AppConstants.ADMIN_ID);
			adminRole.setRoleName("ADMIN");

			Role userRole = new Role();
			userRole.setRoleId(AppConstants.USER_ID);
			userRole.setRoleName("USER");

			List<Role> roles = List.of(adminRole, userRole);

			List<Role> savedRoles = roleRepo.saveAll(roles);

			savedRoles.forEach(System.out::println);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// // Edward Dummy Data 
	// @Override
	// public void run(String... args) throws Exception {
	// 	try {
	// 		Role adminRole = new Role(AppConstants.ADMIN_ID, "ADMIN");
	// 		Role userRole = new Role(AppConstants.USER_ID, "USER");
	// 		List<Role> roles = List.of(adminRole, userRole);
	// 		roleRepo.saveAll(roles);

	// 		// Membuat dummy users
	// 		List<User> users = new ArrayList<>();
	// 		for (int i = 1; i <= 11; i++) {
	// 			Set<Role> userRoles;
	// 			if (i == 1) {
	// 				userRoles = new HashSet<>(List.of(adminRole)); // First user dapat role ADMIN
	// 			} else {
	// 				userRoles = new HashSet<>(List.of(userRole));
	// 			}
	// 			User user = new User(
	// 					(long) i,
	// 					"FirstName" + (char) (64 + i), // Generates "FirstNameA", "FirstNameB", dll.
	// 					"LastName" + (char) (64 + i),
	// 					"123456789" + (i % 10), // Generates valid 10-digit
	// 					"user" + i + "@example.com",
	// 					passwordEncoder.encode("password" + i), // Encrypts password
	// 					userRoles,
	// 					new ArrayList<>(),
	// 					null);
	// 			users.add(user);
	// 		}

	// 		// Membuat dummy addresses
	// 		List<Address> addresses = new ArrayList<>();
	// 		for (int i = 1; i <= 11; i++) {
	// 			Address address = new Address(
	// 					"Country" + i,
	// 					"State" + i,
	// 					"City" + i,
	// 					"123456" + i,
	// 					"Street" + i,
	// 					"Building" + i);
	// 			addresses.add(address);
	// 		}
	// 		addressRepo.saveAll(addresses);

	// 		// Assign addresses ke users
	// 		for (int i = 0; i < 11; i++) {
	// 			users.get(i).getAddresses().add(addresses.get(i));
	// 		}

	// 		// Membuat dummy categories
	// 		List<Category> categories = new ArrayList<>();
	// 		for (int i = 1; i <= 11; i++) {
	// 			categories.add(new Category((long) i, "Category" + i, new ArrayList<>()));
	// 		}
	// 		categoryRepo.saveAll(categories);

	// 		// Membuat dummy brands
	// 		List<Brand> brands = new ArrayList<>();
	// 		for (int i = 1; i <= 11; i++) {
	// 			brands.add(new Brand((long) i, "Brand" + i, null));
	// 		}
	// 		brandRepo.saveAll(brands);

	// 		// Membuat dummy products
	// 		List<Product> products = new ArrayList<>();
	// 		for (int i = 1; i <= 11; i++) {
	// 			Product product = new Product(
	// 					(long) i,
	// 					"Product" + i,
	// 					"Image" + i,
	// 					"Description" + i,
	// 					10,
	// 					100.0 * i,
	// 					0.0,
	// 					90.0 * i,
	// 					categories.get(i - 1),
	// 					brands.get(i - 1),
	// 					new ArrayList<>(),
	// 					new ArrayList<>());
	// 			products.add(product);
	// 		}
	// 		productRepo.saveAll(products);

	// 		// Assign products ke categories
	// 		for (int i = 0; i < 11; i++) {
	// 			categories.get(i).getProducts().add(products.get(i));
	// 		}

	// 		// Membuat dummy carts dan cart items
	// 		List<Cart> carts = new ArrayList<>();
	// 		List<CartItem> cartItems = new ArrayList<>();
	// 		for (int i = 1; i <= 11; i++) {
	// 			Cart cart = new Cart((long) i, users.get(i - 1), new ArrayList<>(), 0.0);
	// 			carts.add(cart);
	// 			users.get(i - 1).setCart(cart);

	// 			CartItem cartItem = new CartItem((long) i, cart, products.get(i - 1), 1, 5.0,
	// 					products.get(i - 1).getPrice());
	// 			cartItems.add(cartItem);
	// 			cart.getCartItems().add(cartItem);
	// 		}

	// 		// Membuat dummy orders
	// 		List<Order> orders = new ArrayList<>();
	// 		for (int i = 1; i <= 11; i++) {
	// 			Order order = new Order(
	// 					(long) i,
	// 					"user" + i + "@example.com",
	// 					new ArrayList<>(),
	// 					LocalDate.now(),
	// 					null,
	// 					100.0 * i,
	// 					"Pending");
	// 			orders.add(order);
	// 		}

	// 		// Membuat dummy order items
	// 		List<OrderItem> orderItems = new ArrayList<>();
	// 		for (int i = 1; i <= 11; i++) {
	// 			OrderItem orderItem = new OrderItem(
	// 					(long) i,
	// 					products.get(i - 1),
	// 					orders.get(i - 1),
	// 					1,
	// 					5.0,
	// 					products.get(i - 1).getPrice());
	// 			orderItems.add(orderItem);
	// 			orders.get(i - 1).getOrderItems().add(orderItem);
	// 		}

	// 		// Membuat dummy payments
	// 		List<Payment> payments = new ArrayList<>();
	// 		for (int i = 1; i <= 11; i++) {
	// 			Payment payment = new Payment((long) i, orders.get(i - 1), "Credit Card");
	// 			payments.add(payment);
	// 			paymentRepo.saveAll(payments);
	// 			orders.get(i - 1).setPayment(payment);
	// 		}

	// 		// Menyimpan semua entities
	// 		userRepo.saveAll(users);
	// 		cartRepo.saveAll(carts);
	// 		cartItemRepo.saveAll(cartItems);
	// 		orderRepo.saveAll(orders);
	// 		orderItemRepo.saveAll(orderItems);

	// 		System.out.println("Dummy data berhasil dimasukkan!");

	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// }

}
