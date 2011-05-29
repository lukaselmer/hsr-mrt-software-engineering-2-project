require 'test_helper'

class MaterialTest < ActiveSupport::TestCase
  setup do
    @material_1 = materials :one
    @material_2 = materials :two
    @material_3 = materials :three
  end

  test "return appropriate array for Selection" do
    assert_equal [[@material_3,@material_3.id]], Material.for_select
  end

  test "correct String representation" do
    expected_string_1 = [@material_1.catalog_number,@material_1.description, @material_1.dimensions].join(", ")
    expected_string_2 = [@material_2.catalog_number,@material_2.description, @material_2.dimensions].join(", ")
    expected_string_3 = @material_3.description

    assert_equal expected_string_1,@material_1.to_s
    assert_equal expected_string_2,@material_2.to_s
    assert_equal expected_string_3,@material_3.to_s
  end
end
