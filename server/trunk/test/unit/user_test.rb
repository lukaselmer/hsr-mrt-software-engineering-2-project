require 'test_helper'

class UserTest < ActiveSupport::TestCase
  setup do
    @field_worker = users :field_worker
    @secretary = users :secretary
  end

  test "correct String representation" do
    assert_equal "Field Worker", @field_worker.to_s
  end

  test "return correct array for Selection" do
    exp_Array = [[Secretary.model_name.human, User::TYPE_SECRETARY], [FieldWorker.model_name.human, User::TYPE_FIELD_WORKER]]
    assert_equal exp_Array, User.type_for_select
  end

  test "return appropriate array for given Type" do

    exp_array_secretary = [[@secretary, @secretary.id]]
    exp_Array_Field_Worker = [[@field_worker, @field_worker.id]]

    assert_equal exp_array_secretary, Secretary.for_select
    assert_equal exp_Array_Field_Worker, FieldWorker.for_select
  end

end
