require 'test_helper'

class MaterialsControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  setup do
    @material = materials(:one)
    login_with_secretary
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:materials)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should not create material without description" do
    invalid_material = Material.new
    assert_no_difference('Material.count') do
      post :create, :material => invalid_material.attributes
    end
    assert_response :success
  end

  test "should create material" do
    assert_difference('Material.count') do
      post :create, :material => @material.attributes
    end

    assert_redirected_to material_path(assigns(:material))
  end

  test "should show material" do
    get :show, :id => @material.to_param
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @material.to_param
    assert_response :success
  end

  test "should not update material without description" do
    @material.description = nil
    put :update, :id => @material.to_param, :material => @material.attributes
    assert_response :success
  end

  test "should update material" do
    put :update, :id => @material.to_param, :material => @material.attributes
    assert_redirected_to material_path(assigns(:material))
  end

  test "should destroy material" do
    assert_difference('Material.count', -1) do
      delete :destroy, :id => @material.to_param
    end

    assert_redirected_to materials_path
  end
end
