require 'test_helper'

class CustomerTest < ActiveSupport::TestCase
  setup do
    @customer_1 = customers :one
    @customer_2 = customers :two
  end

  test "return appropriate array for Selection" do
    assert_equal [[@customer_1, @customer_1.id],[@customer_2,@customer_2.id]], Customer.for_select
  end

  test "correct String representation" do
    expected_string = @customer_1.last_name + ", " + @customer_1.first_name
    expected_string_2 = @customer_2.last_name + ", " + @customer_2.first_name

    assert_equal expected_string, @customer_1.to_s
    assert_equal expected_string_2, @customer_2.to_s
  end
end
