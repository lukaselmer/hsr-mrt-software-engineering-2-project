require 'test_helper'

class OrderTest < ActiveSupport::TestCase
  setup do
    @order_1 = orders :one
    @order_2 = orders :two
  end

  test "correct String representation" do
    expected_string = "#" + @order_1.id.to_s + ": " +
      @order_1.customer.to_s + "; " + I18n.localize(@order_1.created_at, :format => :short)
    expected_string_2 = "#" + @order_2.id.to_s + ": " +
      @order_2.customer.to_s + "; " + I18n.localize(@order_2.created_at, :format => :short)
    
    assert_equal expected_string, @order_1.to_s
    assert_equal expected_string_2, @order_2.to_s
  end
  
  test "return appropriate array for Selection" do
    assert_equal [[@order_1,@order_1.id],[@order_2,@order_2.id]], Order.for_select
  end
end
