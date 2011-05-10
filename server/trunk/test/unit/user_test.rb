require 'test_helper'

class UserTest < ActiveSupport::TestCase
  setup do
    @user = users :field_worker
  end

  test "correct String representation" do
    assert_equal "Field Worker", @user.to_s
  end
end
