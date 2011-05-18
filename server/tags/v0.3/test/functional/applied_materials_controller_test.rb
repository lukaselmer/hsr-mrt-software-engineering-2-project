require 'test_helper'

class AppliedMaterialsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  setup do
    @applied_material = applied_materials(:one)
    login_with_secretary
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:applied_materials)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create applied_material" do
    valid_entry = AppliedMaterial.new(:order_id => orders(:two).id, :material_id => materials(:one).id, :amount => 3)
    assert_difference('AppliedMaterial.count') do
      post :create, :applied_material => valid_entry.attributes
    end
    assert_response :success
  end

  test "should not create applied_material" do
    invalid_applied_material = AppliedMaterial.new
    assert_no_difference('AppliedMaterial.count') do
      post :create, :applied_material => invalid_applied_material.attributes
    end
    assert_response :success
  end

  #  test "should show applied_material" do
  #    get :show, :id => @applied_material.to_param
  #    assert_response :success
  #  end

  test "should get edit" do
    get :edit, :id => @applied_material.to_param
    assert_response :success
  end

  test "should update applied_material" do
    valid_entry = AppliedMaterial.new(:order_id => orders(:two).id, :material_id => materials(:one).id, :amount => 3)
    put :update, :id => @applied_material.to_param, :applied_material => valid_entry.attributes
    assert_redirected_to applied_material_path(assigns(:applied_material))
  end

  test "should not update applied_material" do
    @applied_material.material_id = ""
    @applied_material.material = nil
    @applied_material.order_id = ""
    @applied_material.order = nil
    put :update, :id => @applied_material.to_param, :applied_material => @applied_material.attributes
    assert_response :success
  end

  test "should destroy applied_material" do
    assert_difference('AppliedMaterial.count', -1) do
      delete :destroy, :id => @applied_material.to_param
    end

    #assert_redirected_to applied_materials_path
  end
end
