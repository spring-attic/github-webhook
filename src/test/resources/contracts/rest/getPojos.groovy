package rest

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'GET'
		url '/'
	}
	response {
		status 200
		body([ data: [
				[
					user:"dsyer",
					repo:"spring-cloud-samples",
					type:"hook",
					action:"updated"
				],
				[
					user:"smithapitla",
					repo:"spring-cloud/spring-cloud-netflix",
					type:"issue",
					action:"created"
				]
			]
		])
		headers {
			header('Content-Type': value(
					producer(regex('application/json.*')),
					consumer('application/json'))
			)
		}
	}
}