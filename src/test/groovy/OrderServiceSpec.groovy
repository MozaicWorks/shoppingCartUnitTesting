import spock.lang.Specification


class OrderServiceSpec extends Specification {
	def "it fails"(){
		expect:
		true == false
	}

	def "it passes"(){
		expect:
		true == true
	}
}