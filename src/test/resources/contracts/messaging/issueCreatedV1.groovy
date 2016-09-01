org.springframework.cloud.contract.spec.Contract.make {
	// Human readable description
	description 'Some description'
	// Label by means of which the output message can be triggered
	label 'issue_created_v1'
	// input to the contract
	input {
		// the contract will be triggered by a method
		triggeredBy('createIssue()')
	}
	// output message of the contract
	outputMessage {
		// destination to which the output message will be sent
		sentTo 'messages'
		// the body of the output message
		body('''{"username":"smithapitla","repository":"spring-cloud/spring-cloud-netflix"}''')
	}
}
