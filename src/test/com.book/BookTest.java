

public BookTest extends Specification{


class ProtobufParsingSpec extends Specification {

  def "test parser"()

  {
    expect:
    GroovyEmbeddedApp.of {
    registryOf {
      r ->
        r.add(new ProtobufParser())
    }
    handlers {
      post {
        parse(Protobuf.fromProtobuf(CustomerProto.CustomerResponse)).then {
          r ->
            render(r.toString())
        }
      }
    }
  } test {
    httpClient ->
      CustomerProto.CustomerResponse.Builder customerResponseBuilder = CustomerProto.CustomerResponse.newBuilder()
    (1. .10).each {
      i ->
        customerResponseBuilder.addCustomer(CustomerProto.Customer.newBuilder().setId(i).setFirstName('Some').setLastName('Book').build())
    }
    CustomerProto.CustomerResponse customerResponse = customerResponseBuilder.build()
    ReceivedResponse response = httpClient.request {
      r ->
        r.method(HttpMethod.POST.name())
      r.body {
        body ->
          body.type(Protobuf.X_PROTOBUF_MEDIA_TYPE)
        body.bytes(customerResponse.toByteArray())
      }
    }
  }
  }

}
