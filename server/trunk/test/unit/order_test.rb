require 'test_helper'

class OrderTest < ActiveSupport::TestCase
  setup do
    @order_1 = orders :one
    @order_2 = orders :two
  end

  test "correct String representation" do
    expected_string = "#" + @order_1.id.to_s + ": " +
      @order_1.customer.to_s + "; " + @order_1.created_at.to_s(:short);
    expected_string_2 = "#" + @order_2.id.to_s + ": " +
      @order_2.customer.to_s + "; " + @order_2.created_at.to_s(:short);
    
    assert_equal expected_string, @order_1.to_s
    assert_equal expected_string_2, @order_2.to_s
  end
end
