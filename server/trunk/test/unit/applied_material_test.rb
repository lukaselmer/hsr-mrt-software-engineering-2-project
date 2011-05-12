require 'test_helper'

class AppliedMaterialTest < ActiveSupport::TestCase
  setup do
    @applied_material_1 = applied_materials :one
    @applied_material_2 = applied_materials :two
  end

  test "correct String representation" do
    assert_equal " (1x)", @applied_material_1.to_s
    assert_equal " (2x)", @applied_material_2.to_s
  end
end
