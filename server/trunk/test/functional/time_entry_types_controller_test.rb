require 'test_helper'

class TimeEntryTypesControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  setup do
    @time_entry_type = time_entry_types(:one)
    login_with_secretary
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:time_entry_types)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create time_entry_type" do
    assert_difference('TimeEntryType.count') do
      post :create, :time_entry_type => @time_entry_type.attributes
    end

    assert_redirected_to time_entry_type_path(assigns(:time_entry_type))
  end

  test "should show time_entry_type" do
    get :show, :id => @time_entry_type.to_param
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @time_entry_type.to_param
    assert_response :success
  end

  test "should update time_entry_type" do
    put :update, :id => @time_entry_type.to_param, :time_entry_type => @time_entry_type.attributes
    assert_redirected_to time_entry_type_path(assigns(:time_entry_type))
  end

  test "should destroy time_entry_type" do
    assert_difference('TimeEntryType.count', -1) do
      delete :destroy, :id => @time_entry_type.to_param
    end

    assert_redirected_to time_entry_types_path
  end
end
