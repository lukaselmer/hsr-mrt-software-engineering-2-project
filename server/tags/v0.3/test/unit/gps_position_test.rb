require 'test_helper'

class LocationTest < ActiveSupport::TestCase
  setup do
    @gps_position_1 = gps_positions :one
    @gps_position_2 = gps_positions :two
  end

  test "correct String representation" do
    assert_equal [@gps_position_1.latitude,@gps_position_1.longitude].join(", "), @gps_position_1.to_s
    assert_equal [@gps_position_2.latitude,@gps_position_2.longitude].join(", ") , @gps_position_2.to_s
  end
end
