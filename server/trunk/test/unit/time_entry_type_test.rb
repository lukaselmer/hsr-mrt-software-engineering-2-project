require 'test_helper'

class TimeEntryTypeTest < ActiveSupport::TestCase
  setup do
    @time_entry_type_invalid = time_entry_types :one
    @time_entry_type_valid = time_entry_types :three
  end

  test "correct String representation" do
    expected_String = @time_entry_type_invalid.description + ", " + @time_entry_type_invalid.valid_until.to_s

    assert_equal expected_String,@time_entry_type_invalid.to_s
    assert_equal @time_entry_type_valid.description, @time_entry_type_valid.to_s
  end

  test "return appropriate array for Selection" do
    expected_array = [[@time_entry_type_valid,@time_entry_type_valid.id]]
    assert_equal expected_array, TimeEntryType.for_select
  end
  
end
