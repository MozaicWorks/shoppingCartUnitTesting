import spock.lang.Specification


class OrderServiceSpec extends Specification {

	def service

	def setupSpec(){

	}

	def setup() {
		service = new OrderService()
		service.discountService = Mock(DiscountService)
		service.printService = Mock(PrintService)
	}

	def cleanup(){

	}

	def cleanupSpec() {

	}

	def "order is not created if the shopping card is empty"(){
		given:
		def emptyShoppingCart = new Cart(products: [])

		when:
		def actualOrder = service.createOrder(emptyShoppingCart)

		then:
		null == actualOrder
	}

	def "order is created with same list of products from the cart"() {
		given:
		def cartProducts = [new Product(name: "lego")]
		def cartWithProducts = new Cart(products: cartProducts)

		when:
		def actualOrder = service.createOrder(cartWithProducts)

		then:
		cartProducts == actualOrder.products
	}

	def "order with created with a totalToPay which has the shipping cost when total price of products is under 100"(){
		given:
		def cartProducts = [new Product(name: "lego", price: 90)]
		def productsTotalPrice = 90
		def shippingCost = 15
		def cartWithProducts = new Cart(products: cartProducts)
		stubNoDiscount()

		when:
		def actualOrder = service.createOrder(cartWithProducts)

		then:
		productsTotalPrice + shippingCost == actualOrder.totalToPay
	}

	def "order with created with a totalToPay which doesn't have the shipping cost when total price of products is over 100"(){
		given:
		def cartProducts = [new Product(name: "lego", price: 110)]
		def productsTotalPrice = 110
		def cartWithProducts = new Cart(products: cartProducts)
		stubNoDiscount()

		when:
		def actualOrder = service.createOrder(cartWithProducts)

		then:
		productsTotalPrice == actualOrder.totalToPay
	}

	def "order is created with total to pay taking in consideration the discount for the products"(){
		given:
		def cartProducts = [new Product(name: "lego", price: 110)]
		def productsTotalPrice = 110
		def cartWithProducts = new Cart(products: cartProducts)
		def discount = 20
		1 * service.discountService.calculateDiscount(cartProducts) >> 20

		when:
		def actualOrder = service.createOrder(cartWithProducts)

		then:
		productsTotalPrice - discount == actualOrder.totalToPay
	}

	def "print uses printService to print the given order"(){
		given:
		def order = new Order()

		when:
		service.print(order)

		then:
		1 * service.printService.print(order)
	}

	private def stubNoDiscount() {
		service.discountService.calculateDiscount(*_) >> 0
	}
}