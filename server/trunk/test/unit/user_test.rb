require 'test_helper'

class UserTest < ActiveSupport::TestCase
  setup do
    @field_worker = users :field_worker
    @secretary = users :secretary
  end

  test "correct String representation" do

    expected_string = @field_worker.first_name + " " + @field_worker.last_name
    assert_equal expected_string, @field_worker.to_s
  end

  test "return correct array for Selection" do
    exp_Array = [[Secretary.model_name.human, User::TYPE_SECRETARY], [FieldWorker.model_name.human, User::TYPE_FIELD_WORKER]]
    assert_equal exp_Array, User.type_for_select
  end

  test "return appropriate array for given Type" do

    assert_equal [[@secretary, @secretary.id]], Secretary.for_select
    assert_equal [[@field_worker, @field_worker.id]], FieldWorker.for_select
  end
end
