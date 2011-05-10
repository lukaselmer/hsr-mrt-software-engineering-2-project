require 'test_helper'

class TimeEntryTypeMaterialTest < ActiveSupport::TestCase
  setup do
    @time_entry_type_material = time_entry_type_materials :two
  end

  test "correct String representation" do
    assert_equal " (2x)", @time_entry_type_material.to_s
  end
end
