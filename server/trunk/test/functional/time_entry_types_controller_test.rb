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

  test "should not create time_entry_type without material_id" do
    invalid_time_entry_type = TimeEntryType.new
    assert_no_difference('TimeEntryType.count') do
      post :create, :time_entry_type => invalid_time_entry_type.attributes
    end
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

  test "should not update time_entry_type without description" do
    @time_entry_type.description = nil
    put :update, :id => @time_entry_type.to_param, :time_entry_type => @time_entry_type.attributes
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

  test "should add material time_entry_types" do
    get :add_material, :id => @time_entry_type.to_param
    assert_not_nil assigns(:time_entry_type)
    assert_not_nil assigns(:time_entry_type_material)
  end

  test "should synchronize time_entry_types without last_update" do
    get :synchronize
    assert_not_nil assigns(:updated_time_entry_types)
  end
  
  test "should synchronize time_entry_types with last_update" do
    get :synchronize, :last_update => (Time.now.to_i * 1000)
    assert_not_nil assigns(:updated_time_entry_types)
  end
end
