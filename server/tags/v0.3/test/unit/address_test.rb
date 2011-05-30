require 'test_helper'

class AddressTest < ActiveSupport::TestCase
  setup do
    @address_1 = addresses :one
    @address_2 = addresses :two
    @address_3 = addresses :valid_geocoding_address
    @address_4 = addresses :invalid_geocoding_address
  end
  
  test "correct String representation" do
    expected_string = [@address_1.line1,@address_1.line2,@address_1.line3].join(", ") +
      ", " + [@address_1.zip,@address_1.place].join(" ")
    assert_equal expected_string,@address_1.to_s
  end
end
