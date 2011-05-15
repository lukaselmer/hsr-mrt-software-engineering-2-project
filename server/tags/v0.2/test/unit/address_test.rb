require 'test_helper'

class AddressTest < ActiveSupport::TestCase
  setup do
    @address_1 = addresses :one
    @address_2 = addresses :two
    @address_3 = addresses :valid_geocoding_address
    @address_4 = addresses :invalid_geocoding_address
  end
  
  test "return appropriate array for Selection" do
    expected_array = [[@address_2,@address_2.id],
      [@address_3,@address_3.id], [@address_4,@address_4.id],[@address_1,@address_1.id]]
    assert_equal expected_array, Address.for_select
  end

  test "correct String representation" do
    expected_string = [@address_1.line1,@address_1.line2,@address_1.line3].join(", ") +
      ", " + [@address_1.zip,@address_1.place].join(" ")
    assert_equal expected_string,@address_1.to_s
  end
end
