package rest

org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'GET'
		url '/'
	}
	response {
		status 200
		body([
			[
				username:"dsyer",
				repository:"spring-cloud-samples",
				type:"hook",
				action:"updated"
			],
			[
				username:"smithapitla",
				repository:"spring-cloud/spring-cloud-netflix",
				type:"issue",
				action:"created"
			]
		])
	}
}