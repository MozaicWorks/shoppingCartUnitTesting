class OrderService {

	def printService
	def discountService

	/**
	 * creates the order for the given shopping cart
	 * rules: if total is under 100, there is a shipping cost of 15 which needs to be added to the total
	 *        if total is over 100, the shipping cost is zero
	 *        products may have discount
	 */
	def createOrder(shoppingCart) {
		if (!shoppingCart.products || shoppingCart.products.isEmpty()) return
		def order = new Order(products: shoppingCart.products,
				totalToPay: shoppingCart.products.price.sum()? shoppingCart.products.price.sum() : 0)
		if (order.totalToPay > 0) {
			order.totalToPay = order.totalToPay > 100 ? order.totalToPay : (order.totalToPay + Shipping.cost)
			order.totalToPay = order.totalToPay - discountService.calculateDiscount(order.products)
		}
		return order
	}

	/**
	 * uses an external service to print de order
	 */
	def print(order) {
		printService.print(order)
	}
}
