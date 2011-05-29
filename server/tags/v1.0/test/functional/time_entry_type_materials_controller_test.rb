require 'test_helper'

class TimeEntryTypeMaterialsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  setup do
    @time_entry_type_material = time_entry_type_materials(:one)
    login_with_secretary
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:time_entry_type_materials)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create time_entry_type_material" do
    assert_difference('TimeEntryTypeMaterial.count') do
      entry = TimeEntryTypeMaterial.new(:time_entry_type_id => time_entry_types(:one).id, :material_id => materials(:one).id)
      post :create, :time_entry_type_material => entry.attributes
    end
    
    assert_response :success
  end

  test "should show time_entry_type_material" do
    get :show, :id => @time_entry_type_material.to_param
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @time_entry_type_material.to_param
    assert_response :success
  end

  test "should update time_entry_type_material" do
    entry = TimeEntryTypeMaterial.new(:time_entry_type_id => time_entry_types(:one).id, :material_id => materials(:one).id)
    put :update, :id => @time_entry_type_material.to_param, :time_entry_type_material => entry.attributes
    assert_redirected_to time_entry_type_material_path(assigns(:time_entry_type_material))
  end

  test "should not update time_entry_type_material" do
    put :update, :id => @time_entry_type_material.to_param, :time_entry_type_material => @time_entry_type_material.attributes
    assert_response :success
  end

  test "should destroy time_entry_type_material" do
    assert_difference('TimeEntryTypeMaterial.count', -1) do
      delete :destroy, :id => @time_entry_type_material.to_param
    end
    assert_response :success
  end
end
