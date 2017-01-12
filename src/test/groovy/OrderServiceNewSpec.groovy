import spock.lang.Specification

class OrderServiceNewSpec extends Specification {

	def orderService

	void setup() {
		orderService = new OrderService()
		orderService.discountService = Mock(DiscountService)
		orderService.dateService = Mock(DateService)
		orderService.printService = Mock(PrintService)
	}

	void "created order has totalToPay minus the calculated discount"() {
		given:
		def givenShoppingCart = [products: [new Product(name: 'X', price: 120)]]
		def expectedDiscount = 20
		def expectedTotalToPayWithoutDiscount = 120
		orderService.discountService.calculateDiscount(givenShoppingCart.products) >> expectedDiscount

		when:
		def actualOrder = orderService.createOrder(givenShoppingCart)
		def actualTotalToPay = actualOrder.totalToPay

		then:
		expectedTotalToPayWithoutDiscount - expectedDiscount == actualTotalToPay
	}

	void "order is created with today date"() {
		given:
		def givenShoppingCart = [products: [new Product(name: 'P', price: 100)]]
		def expectedDate = new Date()
		orderService.discountService.calculateDiscount(*_) >> 20
		orderService.dateService.today() >> expectedDate

		when:
		def actualOrder = orderService.createOrder(givenShoppingCart)

		then:
		expectedDate == actualOrder.createdDate
	}

	void "printing the order is using printService method"() {
		given:
		def givenOrder = new Order()

		when:
		orderService.print(givenOrder)

		then:
		1 * orderService.printService.print(givenOrder)
	}
}
